package com.example.verdaapp.api

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName("message")
    val message: String
)

data class ChatResponse(
    @SerializedName("response")
    val response: String
)
