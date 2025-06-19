package com.example.verdaapp.api

import com.google.gson.annotations.SerializedName

data class SyncProfileRequest(
    @SerializedName("user_id")
    val userId: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("nama")
    val nama: String? = null
)

data class SyncProfileResponse(
    val message: String
)