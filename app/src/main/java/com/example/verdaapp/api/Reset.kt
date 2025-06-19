package com.example.verdaapp.api

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    val email: String
)

data class ResetPasswordData(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("new_password")
    val newPassword: String
)