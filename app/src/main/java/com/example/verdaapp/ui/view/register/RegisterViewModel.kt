package com.example.verdaapp.ui.view.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verdaapp.api.ApiConfig
import com.example.verdaapp.api.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel : ViewModel() {
    private val _registerState = MutableStateFlow<String?>(null)
    val registerState: StateFlow<String?> = _registerState
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _registerError = MutableStateFlow<String?>(null)
    val registerError: StateFlow<String?> = _registerError.asStateFlow()

    fun register(name: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().register(
                    RegisterRequest(username = name, email = email, password = password, role = role)
                )
                _registerState.value = "Registrasi Berhasil! Silakan cek email Anda."
                _registerError.value = null
            } catch (e: IOException) {
                _registerState.value = "Network error: ${e.localizedMessage}"
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                if (errorBody != null && errorBody.contains("duplicate")) {
                    _registerError.value = "This email is already registered"
                } else if (errorBody != null && errorBody.contains("Password minimal")) {
                    _registerError.value = "Password must be at least 8 characters long"
                } else {
                    _registerError.value = "Unable to register. Please try again."
                }
//                _registerState.value = "Kesalahan: ${e.localizedMessage}. Periksa kembali data yang dimasukkan."
                _registerState.value = null
            } catch (e: Exception) {
                _registerError.value = "Terjadi kesalahan: ${e.localizedMessage}"
                _registerState.value = null
            }  finally {
            _isLoading.value = false
            }
        }
    }

    fun resetRegisterState() {
        _registerState.value = null
    }

    fun resetRegisterError() {
        _registerError.value = null
    }
}