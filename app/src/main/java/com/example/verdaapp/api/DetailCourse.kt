package com.example.verdaapp.api

import com.google.gson.annotations.SerializedName

data class DetailCourse(
    @SerializedName("module_id")
    val moduleId: String,

    @SerializedName("judul")
    val title: String,

    @SerializedName("deskripsi")
    val description: String,

    @SerializedName("urutan")
    val order: Int,

    @SerializedName("sections")
    val sections: List<Section>,

    @SerializedName("references")
    val references: List<Reference>
)

data class Section(
    @SerializedName("section_id")
    val sectionId: String,

    @SerializedName("judul")
    val title: String,

    @SerializedName("konten")
    val content: String,

    @SerializedName("urutan")
    val order: Int
)

data class Reference(
    @SerializedName("label")
    val label: String,

    @SerializedName("url")
    val url: String
)
