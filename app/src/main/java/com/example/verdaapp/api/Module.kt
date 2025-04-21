package com.example.verdaapp.api

import com.google.gson.annotations.SerializedName

data class Module(
    @SerializedName("module_id")
    val module_id: Int,

    @SerializedName("judul")
    val judul: String,

    @SerializedName("deskripsi")
    val deskripsi: String,

    @SerializedName("urutan")
    val urutan: Int
)
