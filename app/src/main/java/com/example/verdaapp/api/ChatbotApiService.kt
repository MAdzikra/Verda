package com.example.verdaapp.api

import retrofit2.http.Body
import retrofit2.http.POST

interface ChatbotApiService {
    @POST("chat")
    suspend fun sendMessage(@Body request: ChatRequest): ChatResponse
}