package com.example.verdaapp.ui.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verdaapp.api.ApiConfig
import com.example.verdaapp.api.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<String?>(null)
    val loginState: StateFlow<String?> = _loginState.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().login(LoginRequest(email, password))
                _loginState.value = response.message ?: "Login sukses"
            } catch (e: IOException) {
                _loginState.value = "Network error: ${e.localizedMessage}"
            } catch (e: HttpException) {
                _loginState.value = "Unexpected error: ${e.localizedMessage}"
            } catch (e: Exception) {
                _loginState.value = "Login error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = null
    }
}