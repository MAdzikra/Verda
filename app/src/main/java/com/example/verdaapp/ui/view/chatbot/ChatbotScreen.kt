package com.example.verdaapp.ui.view.chatbot

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.verdaapp.R
import com.example.verdaapp.api.ChatRequest
import com.example.verdaapp.api.ChatbotApiConfig
import com.example.verdaapp.datastore.UserPreferenceKeys
import com.example.verdaapp.datastore.dataStore
import com.example.verdaapp.ui.theme.VerdaAppTheme
import kotlinx.coroutines.launch

@Composable
fun ChatbotScreen(navController: NavHostController) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(Color(0xFF27B07A), Color(0xFF86CFAC))))
                .padding(paddingValues)
        ) {
            Header(navController)
            Spacer(modifier = Modifier.height(16.dp))
            IsiChat()
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
fun IsiChat() {
    val scope = rememberCoroutineScope()
    var message by rememberSaveable { mutableStateOf("") }
    var chatMessages by rememberSaveable {
        mutableStateOf(
            listOf<Pair<String, Boolean>>() // Pair(message, isUser)
        )
    }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(chatMessages.size) {
        listState.animateScrollToItem(chatMessages.size)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Daftar pesan
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp)
        ) {
//            item(chatMessages.forEach { (text, isUser) ->
//                ChatBubble(text = text, isUser = isUser)
//            }
//            if (isLoading) {
//                ChatBubble(text = "Mengetik...", isUser = false)
//            }
            items(chatMessages) { (text, isUser) ->
                ChatBubble(text = text, isUser = isUser)
            }

            if (isLoading) {
                item {
                    ChatBubble(text = "Mengetik...", isUser = false)
                }
            }
        }

        // Kolom input + tombol kirim
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text("Ketik pesan...") },
                shape = RoundedCornerShape(20.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_send),
                contentDescription = "Kirim",
                tint = Color(0xFF27B07A),
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        if (message.isNotBlank()) {
                            val userMessage = message
                            chatMessages = chatMessages + (userMessage to true)
                            message = ""
                            isLoading = true

                            scope.launch {
                                try {
                                    val response = ChatbotApiConfig
                                        .getApiService()
                                        .sendMessage(ChatRequest(userMessage))
                                    chatMessages = chatMessages + (response.response to false)
                                } catch (e: Exception) {
                                    chatMessages =
                                        chatMessages + ("Terjadi kesalahan koneksi." to false)
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    }
            )
        }
    }
}

@Composable
fun ChatBubble(text: String, isUser: Boolean) {
    val backgroundColor = if (isUser) Color(0xFFDCF8C6) else Color(0xFFEFEFEF)
    val horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUser) {
            Icon(
                painter = painterResource(id = R.drawable.ic_robot),
                contentDescription = "Bot",
                tint = Color(0xFF27B07A),
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 4.dp)
            )
        }

        Text(
            text = text,
            modifier = Modifier
                .background(backgroundColor, RoundedCornerShape(12.dp))
                .padding(12.dp),
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}


@Preview
@Composable
private fun QuizPrev() {
    VerdaAppTheme {
        val navController = rememberNavController()
        ChatbotScreen(navController)
    }
}
