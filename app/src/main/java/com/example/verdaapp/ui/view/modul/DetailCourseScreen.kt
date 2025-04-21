package com.example.verdaapp.ui.view.modul

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.verdaapp.BottomBar
import com.example.verdaapp.R
import com.example.verdaapp.api.DetailCourse
import com.example.verdaapp.api.Module
import com.example.verdaapp.api.Section
import com.example.verdaapp.ui.theme.VerdaAppTheme
import com.example.verdaapp.ui.view.home.ModuleViewModel

@Composable
fun DetailCourseScreen(navController: NavHostController, moduleId: String) {
    val viewModel: DetailCourseViewModel = viewModel()
    val moduleDetail by viewModel.moduleDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchModuleDetail(moduleId)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(Color(0xFF27B07A), Color(0xFF86CFAC))))
                .padding(paddingValues)
        ) {
            Header(navController)
            Spacer(modifier = Modifier.height(16.dp))

            moduleDetail?.let {
                IsiModule(it)
            }
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
fun Header(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Brush.verticalGradient(listOf(Color(0xFF27B07A), Color(0xFF86CFAC)))),
        contentAlignment = Alignment.TopStart
    ) {
        Image(
            painter = painterResource(id = R.drawable.course_image_rmvbg),
            contentDescription = "Header Image",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxSize()
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier
                .padding(16.dp)
                .size(36.dp)
                .background(Color.White, CircleShape)
                .padding(8.dp)
                .clickable { navController.popBackStack() }
        )
    }
}

@Composable
fun IsiModule(module: DetailCourse) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(16.dp)
    ) {
        item {
            Text(
                text = module.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = module.description,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Lessons",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
            module.sections.sortedBy { it.order }.forEach { section ->
                SectionItem(section)
            }
            Spacer(modifier = Modifier.height(12.dp))
            QuizCard()

        }
    }
}

@Composable
fun QuizCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_quiz),
                contentDescription = "Quiz Icon",
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Quiz Sessions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lesson),
                        contentDescription = "Question Count",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "50 Question", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_time),
                        contentDescription = "Duration",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "1h 20min", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun SectionItem(section: Section) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = section.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = section.content,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}


@Preview
@Composable
private fun DetailPrev() {
    VerdaAppTheme {
        val navController = rememberNavController()
        DetailCourseScreen(navController, "")
    }
}
