package com.example.verdaapp.ui.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.verdaapp.api.Article
import com.example.verdaapp.api.Module
import com.example.verdaapp.datastore.UserPreferenceKeys
import com.example.verdaapp.datastore.dataStore
import com.example.verdaapp.ui.theme.VerdaAppTheme
import com.example.verdaapp.ui.theme.poppinsFontFamily
import com.example.verdaapp.ui.view.artikel.ArticleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(navController: NavHostController) {
    val viewModel: ModuleViewModel = viewModel()
    val modules by viewModel.modules.collectAsState()
    var searchText by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val articleViewModel: ArticleViewModel = viewModel()
    val articles by articleViewModel.articles.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchModules()
        articleViewModel.fetchArticles()
    }

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .background(Brush.horizontalGradient(listOf(Color(0xFF27B07A), Color(0xFF86CFAC))))
        ) {
            TopBar(navController)
            SearchBar(searchText = searchText, onSearchChange = { searchText = it })
            Spacer(modifier = Modifier.height(16.dp))
            BannerSection()
            Spacer(modifier = Modifier.height(25.dp))

            val filteredModules = modules.filter { module ->
                module.judul.contains(searchText, ignoreCase = true) ||
                        module.deskripsi.contains(searchText, ignoreCase = true)
            }
            CourseSection(modules = filteredModules, articles = articles)
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
fun TopBar(navController: NavHostController) {
    val context = LocalContext.current
    val userNameFlow = context.dataStore.data.map { it[UserPreferenceKeys.USER_NAME] ?: "User" }
    val userName by userNameFlow.collectAsState(initial = "User")
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Welcome Home, $userName!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.width(180.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "Profile",
            modifier = Modifier
                .size(50.dp)
                .background(Color.LightGray, CircleShape)
                .padding(8.dp)
                .clickable {
                    CoroutineScope(Dispatchers.IO).launch {
                        context.dataStore.edit {
                            it.clear()
                        }
                        withContext(Dispatchers.Main) {
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }
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
fun BannerSection() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD6F5E1)),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Start Your Green Journey Today!",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Explore course", color = Color(0xFF6CB30B), fontFamily = poppinsFontFamily, fontWeight = FontWeight.Bold)
                }
            }

            Image(
                painter = painterResource(id = R.drawable.banner_image),
                contentDescription = "Nature",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
fun CourseSection(modules: List<Module>, articles: List<Article>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            ArticleSection(articles = articles)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Courses",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))
            if (modules.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(modules.size) { index ->
                        val module = modules[index]
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF86CFAC)),
                            modifier = Modifier
                                .height(150.dp)
                                .width(160.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = module.judul,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = module.deskripsi,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleSection(articles: List<Article>) {
    Column(modifier = Modifier) {
        Text(
            text = "Today's Read",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (articles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No article found",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(articles.size) { index ->
                    val article = articles[index]
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .width(260.dp)
                            .height(120.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        ) {
//                            Image(
//                                painter = painterResource(id = R.drawable.placeholder_article),
//                                contentDescription = "Article Image",
//                                modifier = Modifier
//                                    .width(260.dp)
//                                    .height(145.dp)
//                                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
//                                    .align(Alignment.TopCenter)
//                            )
                            Text(
                                text = article.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                            ) {
//                                Text(
//                                    text = article.title,
//                                    fontWeight = FontWeight.SemiBold,
//                                    fontSize = 14.sp,
//                                    color = Color.Black
//                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Author",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Source", color = Color.Gray, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(" - ", color = Color.Gray, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Time", color = Color.Gray, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun HomePrev() {
    VerdaAppTheme {
        val navController = rememberNavController()
        HomeScreen(navController)
    }
}
