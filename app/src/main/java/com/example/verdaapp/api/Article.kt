package com.example.verdaapp.api

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("module_id")
    val moduleId: String?
)