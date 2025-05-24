package com.example.verdaapp.ui.view.kuis

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verdaapp.api.AnswerItem
import com.example.verdaapp.api.AnswerRequest
import com.example.verdaapp.api.ApiConfig
import com.example.verdaapp.api.Quiz
import com.example.verdaapp.api.QuizStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {
    private val _quiz = MutableStateFlow<List<Quiz>>(emptyList())
    val quiz: StateFlow<List<Quiz>> = _quiz

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _submitResult = MutableStateFlow<Int?>(null)
    val submitResult: StateFlow<Int?> = _submitResult

    private val _quizStatus = MutableStateFlow<QuizStatus?>(null)
    val quizStatus: StateFlow<QuizStatus?> = _quizStatus

    fun fetchQuiz(moduleId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = ApiConfig.getApiService().getQuizByModuleId(moduleId)
                _quiz.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitAnswer(userId: String, answers: List<AnswerItem>, onResult: (Boolean, Int) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().submitAnswer(
                    AnswerRequest(userId = userId, answers = answers)
                )
                onResult(response.success, response.total)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false, 0)
            }
        }
    }

    fun fetchQuizStatus(userId: String, moduleId: String) {
        viewModelScope.launch {
            try {
                val result = ApiConfig.getApiService().getQuizStatus(userId, moduleId)
                _quizStatus.value = result
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Error fetch quiz status: ${e.message}")
            }
        }
    }
}