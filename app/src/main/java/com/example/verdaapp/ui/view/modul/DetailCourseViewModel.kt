package com.example.verdaapp.ui.view.modul

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verdaapp.api.ApiConfig
import com.example.verdaapp.api.DetailCourse
import com.example.verdaapp.api.SaveProgressRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailCourseViewModel : ViewModel() {
    private val _moduleDetail = MutableStateFlow<DetailCourse?>(null)
    val moduleDetail: StateFlow<DetailCourse?> get() = _moduleDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun fetchModuleDetail(moduleId: String, userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getModuleDetail(moduleId, userId)
                _moduleDetail.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                _moduleDetail.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveProgress(userId: String, moduleId: String, sectionId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().saveProgress(SaveProgressRequest(userId, moduleId, sectionId))
                if (response.success) {
                    Log.d("SaveProgress", "Progress saved")
                }
            } catch (e: Exception) {
                Log.e("SaveProgress", "Error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}