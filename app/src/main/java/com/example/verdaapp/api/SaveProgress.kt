package com.example.verdaapp.api

import com.google.gson.annotations.SerializedName

data class SaveProgressRequest(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("module_id")
    val moduleId: String,
    @SerializedName("section_id")
    val sectionId: String
)

data class SaveProgressResponse(
    @SerializedName("success")
    val success: Boolean
)
