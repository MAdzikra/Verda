package com.example.verdaapp.ui.view.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verdaapp.api.ApiConfig
import com.example.verdaapp.api.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel : ViewModel() {

    private val _registerState = MutableStateFlow<String?>(null)
    val registerState: StateFlow<String?> = _registerState

    fun register(name: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().register(
                    RegisterRequest(username = name, email = email, password = password, role = role)
                )
                _registerState.value = response.message ?: "Register successful"
            } catch (e: IOException) {
                _registerState.value = "Network error: ${e.localizedMessage}"
            } catch (e: HttpException) {
                _registerState.value = "Unexpected error: ${e.localizedMessage}"
            } catch (e: Exception) {
                _registerState.value = "Error: ${e.localizedMessage}"
            }
        }
    }

    fun resetRegisterState() {
        _registerState.value = null
    }
}