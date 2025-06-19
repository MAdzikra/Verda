package com.example.verdaapp.ui.view.kuis

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.verdaapp.R
import com.example.verdaapp.api.AnswerItem
import com.example.verdaapp.api.Quiz
import com.example.verdaapp.datastore.UserPreferenceKeys
import com.example.verdaapp.datastore.dataStore
import com.example.verdaapp.ui.theme.VerdaAppTheme
import kotlinx.coroutines.flow.map

@Composable
fun QuizScreen(navController: NavHostController, moduleId: String) {
    val viewModel: QuizViewModel = viewModel()
    val quizList by viewModel.quiz.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val userIdFlow = context.dataStore.data.map { it[UserPreferenceKeys.USER_ID_KEY] ?: "" }
    val userId by userIdFlow.collectAsState(initial = "")

    LaunchedEffect(moduleId) {
        viewModel.fetchQuiz(moduleId)
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
            when {
                isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }

                quizList.isNotEmpty() -> {
                    IsiQuiz(navController, quiz = quizList[0], viewModel = viewModel, userId = userId)
                }

                else -> {
                    Text("Tidak ada kuis ditemukan", color = Color.White, modifier = Modifier.padding(16.dp))
                }
            }
        }

//        if (isLoading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color(0x80000000)),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator(color = Color.White)
//            }
//        }
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
fun IsiQuiz(
    navController: NavHostController,
    quiz: Quiz,
    viewModel: QuizViewModel,
    userId: String
) {
    val questions = quiz.questions
    var currentQuestionIndex by rememberSaveable { mutableStateOf(0) }
    var selectedAnswers by rememberSaveable { mutableStateOf(MutableList(questions.size) { -1 }) }
    val currentQuestion = questions[currentQuestionIndex]
    var isFinished by rememberSaveable { mutableStateOf(false) }
    var score by rememberSaveable { mutableStateOf(0) }

    var showOverview by rememberSaveable { mutableStateOf(true) }
    val quizStatus by viewModel.quizStatus.collectAsState()

    LaunchedEffect(userId) {
        viewModel.fetchQuizStatus(userId, quiz.quizId.toString())
    }

    if (showOverview) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(quiz.judul, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF27B07A))
            Spacer(modifier = Modifier.height(16.dp))

            if (quizStatus != null) {
                Text("Status: ${quizStatus!!.status}", fontSize = 16.sp)
                Text("Skor terakhir: ${quizStatus!!.skor}/${quizStatus!!.totalSoal} (${quizStatus!!.skorPersen})", fontSize = 16.sp)
            } else {
                CircularProgressIndicator(color = Color(0xFF27B07A))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { showOverview = false },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27B07A))
            ) {
                Text("Mulai Kuis", color = Color.White)
            }
        }
    } else if (isFinished) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Skor Kamu", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF27B07A))
                Spacer(Modifier.height(8.dp))
                Text("$score dari ${questions.size} benar", fontSize = 20.sp, color = Color.Black)
                Spacer(Modifier.height(24.dp))

                Row {
                    Text(
                        "Coba Lagi",
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF86CFAC))
                            .clickable {
                                selectedAnswers.clear()
                                selectedAnswers.addAll(List(questions.size) { -1 })
                                currentQuestionIndex = 0
                                isFinished = false
                            }
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        color = Color.White,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        "Kembali",
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray)
                            .clickable {
                                navController.popBackStack()
                            }
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Pertanyaan ${currentQuestionIndex + 1}/${questions.size}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF27B07A)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = currentQuestion.pertanyaan,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                currentQuestion.toOptions().forEachIndexed { index, option ->
                    val isSelected = selectedAnswers[currentQuestionIndex] == index
                    val bgColor = if (isSelected) Color(0xFF27B07A) else Color.LightGray

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(bgColor)
                            .clickable {
//                                selectedAnswers[currentQuestionIndex] = index
                                selectedAnswers = selectedAnswers.toMutableList().also {
                                    it[currentQuestionIndex] = index
                                }
                            }
                            .padding(12.dp)
                    ) {
                        Text(text = option, color = Color.White, fontSize = 14.sp)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentQuestionIndex > 0) {
                    Text(
                        text = "Previous",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF27B07A))
                            .clickable { currentQuestionIndex-- }
                            .padding(12.dp),
                        color = Color.White
                    )
                }

                if (currentQuestionIndex < questions.size - 1) {
                    Text(
                        text = "Next",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF27B07A))
                            .padding(12.dp)
                            .clickable {
                                currentQuestionIndex++
                            }
                    )
                } else {
                    Text(
                        text = "Finish",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF27B07A))
                            .clickable {
                                val correctCount = questions.indices.count { index ->
                                    val selectedIndex = selectedAnswers[index]
                                    if (selectedIndex == -1) return@count false
                                    val correctIndex = when (questions[index].jawabanBenar.trim().lowercase()) {
                                        "a" -> 0
                                        "b" -> 1
                                        "c" -> 2
                                        "d" -> 3
                                        "e" -> 4
                                        else -> -1
                                    }
                                    Log.d("QuizDebug", "Q${index+1} - Selected: $selectedIndex | Correct: $correctIndex")
                                    selectedIndex == correctIndex
                                }
                                val answerList = questions.mapIndexedNotNull { index, question ->
                                    val selected = selectedAnswers[index]
                                    if (selected != -1) {
                                        val selectedLabel = when (selected) {
                                            0 -> "A"
                                            1 -> "B"
                                            2 -> "C"
                                            3 -> "D"
                                            else -> ""
                                        }
                                        AnswerItem(questionId = question.questionId, jawabanUser = selectedLabel)
                                    } else null
                                }

                                viewModel.submitAnswer(userId, answerList) { success, total ->
                                    if (success) {
                                        score = correctCount
                                        isFinished = true
                                    }
                                }
                            }
                            .padding(12.dp),
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun QuizPrev() {
    VerdaAppTheme {

    }
}