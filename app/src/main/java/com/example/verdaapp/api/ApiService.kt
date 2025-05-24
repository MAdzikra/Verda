package com.example.verdaapp.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("functions/v1/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("functions/v1/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("functions/v1/get-modules")
    suspend fun getModules(): List<Module>

    @GET("functions/v1/modul/{module_id}")
    suspend fun getModuleDetail(
        @Path("module_id") moduleId: String,
        @Query("user_id") userId: String
    ): DetailCourse

    @GET("functions/v1/articles")
    suspend fun getArticles(): List<Article>

    @POST("functions/v1/sync-profile")
    suspend fun syncProfile(@Body request: SyncProfileRequest): SyncProfileResponse

    @POST("functions/v1/save-progress")
    suspend fun saveProgress(@Body request: SaveProgressRequest): SaveProgressResponse

    @GET("functions/v1/kuis/{module_id}")
    suspend fun getQuizByModuleId(
        @Path("module_id") moduleId: String
    ): List<Quiz>

    @POST("functions/v1/submit-answer")
    suspend fun submitAnswer(@Body body: AnswerRequest): SubmitAnswerResponse

    @GET("functions/v1/quiz-status")
    suspend fun getQuizStatus(
        @Query("user_id") userId: String,
        @Query("module_id") moduleId: String
    ): QuizStatus

    @POST("functions/v1/req-pass-reset")
    suspend fun requestPasswordReset(
        @Body request: ResetPasswordRequest
    ): Response<Unit>

    @POST("functions/v1/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordData
    ): Response<Unit>
}