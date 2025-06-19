package com.example.verdaapp.api

import com.google.gson.annotations.SerializedName

data class Quiz(
    @SerializedName("quiz_id")
    val quizId: Int,

    @SerializedName("judul")
    val judul: String,

    @SerializedName("deskripsi")
    val deskripsi: String,

    @SerializedName("question")
    val questions: List<Question>
)

data class Question(
    @SerializedName("question_id")
    val questionId: Int,

    @SerializedName("pertanyaan")
    val pertanyaan: String,

    @SerializedName("opsi_a")
    val opsiA: String,

    @SerializedName("opsi_b")
    val opsiB: String,

    @SerializedName("opsi_c")
    val opsiC: String,

    @SerializedName("opsi_d")
    val opsiD: String,

    @SerializedName("opsi_e")
    val opsiE: String,

    @SerializedName("jawaban_benar")
    val jawabanBenar: String
) {
    fun toOptions(): List<String> = listOf(opsiA, opsiB, opsiC, opsiD, opsiE)
}

data class AnswerItem(
    @SerializedName("question_id") val questionId: Int,
    @SerializedName("jawaban_user") val jawabanUser: String
)

data class AnswerRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("answers") val answers: List<AnswerItem>
)

data class SubmitAnswerResponse(
    val success: Boolean,
    val total: Int
)

data class QuizStatus(
    @SerializedName("status")
    val status: String,
    @SerializedName("skor")
    val skor: Int,
    @SerializedName("total_soal")
    val totalSoal: Int,
    @SerializedName("skor_persen")
    val skorPersen: String
)
