package com.example.verdaapp.ui.view.modul

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.verdaapp.BottomBar
import com.example.verdaapp.R
import com.example.verdaapp.api.Module
import com.example.verdaapp.datastore.UserPreference
import com.example.verdaapp.datastore.UserPreferenceKeys
import com.example.verdaapp.datastore.dataStore
import com.example.verdaapp.navigation.Screen
import com.example.verdaapp.ui.theme.VerdaAppTheme
import com.example.verdaapp.ui.view.home.ModuleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CourseScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    val viewModel: ModuleViewModel = viewModel()
    val modules by viewModel.modules.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val userIdFlow = context.dataStore.data.map { preferences -> preferences[UserPreferenceKeys.USER_ID_KEY] ?: "" }
    val userId by userIdFlow.collectAsState(initial = "")


    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            viewModel.fetchModules(userId)
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Konfirmasi Logout") },
            text = { Text("Apakah Anda yakin ingin logout?") },
            confirmButton = {
                Button(onClick = {
                    showLogoutDialog = false
                    CoroutineScope(Dispatchers.IO).launch {
                        context.dataStore.edit { it.clear() }
                        withContext(Dispatchers.Main) {
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }
                }) {
                    Text("Ya")
                }
            },
            dismissButton = {
                Button(onClick = { showLogoutDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(Color(0xFF27B07A), Color(0xFF86CFAC))))
                .padding(paddingValues)
        ) {
            Header() {
                showLogoutDialog = true
            }
            SearchBar(searchText = searchText, onSearchChange = { searchText = it })
            Spacer(modifier = Modifier.height(16.dp))
            val filteredModules = modules.filter { module ->
                module.judul.contains(searchText, ignoreCase = true)
            }
            CourseList(modules = filteredModules, navController)
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

@Composable
fun Header(onLogoutClicked: () -> Unit) {
    val currentDate = remember {
        val dateFormat = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault())
        dateFormat.format(Date())
    }
    val context = LocalContext.current
    val userNameFlow = context.dataStore.data.map { it[UserPreferenceKeys.USER_NAME] ?: "User" }
    val userName by userNameFlow.collectAsState(initial = "User")
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = userName,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = currentDate,
                color = Color.Black.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_logout),
            contentDescription = "Logout",
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, CircleShape)
                .padding(10.dp)
                .clickable {
                    onLogoutClicked()
                }
        )
    }
}

@Composable
fun SearchBar(searchText: String, onSearchChange: (String) -> Unit) {
    TextField(
        value = searchText,
        onValueChange = onSearchChange,
        placeholder = { Text("Search", color = Color.White) },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        },
        textStyle = TextStyle(color = Color.White),
        modifier = Modifier.padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .border(1.dp, Color.White, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun CourseList(modules: List<Module>, navController: NavHostController) {
    val viewModel: ModuleViewModel = viewModel()
    val sectionCounts by viewModel.sectionCounts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "For You",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (modules.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No module found",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(modules.size) { index ->
                    val module = modules[index]
                    val sectionCount = sectionCounts[module.module_id] ?: 0
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        CourseItem(
//                            locked = module.urutan != 1,
                            locked = false,
                            title = module.judul,
                            moduleId = module.module_id.toString(),
                            lessonCount = sectionCount,
                            navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun CourseItem(locked: Boolean, title: String, moduleId: String, lessonCount: Int, navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.course_image),
            contentDescription = "Course Image",
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title ?: "Understanding Sustainability and Our Role",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.Black,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lesson),
                        contentDescription = "Lessons",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("$lessonCount Lessons", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_time),
                        contentDescription = "Time",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("~", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (locked) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.LightGray)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_locked),
                            contentDescription = "Locked",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Locked",
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Text(
                        text = "Learn Now",
                        color = Color(0xFF27B07A),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.DetailCourse.createRoute(moduleId))
                        }
                    )
                }
            }
        }
    }
}



@Preview
@Composable
private fun CousePrev() {
    VerdaAppTheme {
        val navController = rememberNavController()
        CourseScreen(navController)
    }
}
