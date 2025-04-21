package com.example.verdaapp.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("functions/v1/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("functions/v1/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("functions/v1/get-modules")
    suspend fun getModules(): List<Module>

    @GET("functions/v1/get-module/{module_id}")
    suspend fun getModuleDetail(@Path("module_id") moduleId: String): DetailCourse
}