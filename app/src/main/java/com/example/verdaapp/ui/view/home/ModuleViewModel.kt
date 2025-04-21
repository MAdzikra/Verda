package com.example.verdaapp.ui.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verdaapp.api.ApiConfig
import com.example.verdaapp.api.Module
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ModuleViewModel : ViewModel() {
    private val _modules = MutableStateFlow<List<Module>>(emptyList())
    val modules: StateFlow<List<Module>> = _modules
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


//    init {
//        fetchModules()
//    }

    fun fetchModules() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().getModules()
                _modules.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}