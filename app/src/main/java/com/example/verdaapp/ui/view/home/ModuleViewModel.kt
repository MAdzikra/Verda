package com.example.verdaapp.ui.view.home

import android.util.Log
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
    private val _sectionCounts = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val sectionCounts: StateFlow<Map<Int, Int>> = _sectionCounts


//    init {
//        fetchModules()
//    }

    fun fetchModules(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().getModules()
                _modules.value = response

                val sectionCountMap = mutableMapOf<Int, Int>()
                response.forEach { module ->
                    val detail = ApiConfig.getApiService().getModuleDetail(module.module_id.toString(), userId)
                    sectionCountMap[module.module_id] = detail.sections.size
                    Log.d("ModuleViewModel", "Detail for ${module.module_id}: ${detail.sections.size} sections")
                }
                _sectionCounts.value = sectionCountMap
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}