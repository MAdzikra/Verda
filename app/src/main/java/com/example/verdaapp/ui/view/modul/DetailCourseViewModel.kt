package com.example.verdaapp.ui.view.modul

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verdaapp.api.ApiConfig
import com.example.verdaapp.api.ApiService
import com.example.verdaapp.api.DetailCourse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailCourseViewModel : ViewModel() {
    private val _moduleDetail = MutableStateFlow<DetailCourse?>(null)
    val moduleDetail: StateFlow<DetailCourse?> get() = _moduleDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun fetchModuleDetail(moduleId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getModuleDetail(moduleId)
                _moduleDetail.value = response
            } catch (e: Exception) {
                e.printStackTrace()
                _moduleDetail.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}