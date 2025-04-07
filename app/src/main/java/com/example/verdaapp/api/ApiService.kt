package com.example.verdaapp.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("functions/v1/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("functions/v1/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
}