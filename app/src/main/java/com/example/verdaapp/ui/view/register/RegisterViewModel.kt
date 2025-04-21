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
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun register(name: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().register(
                    RegisterRequest(username = name, email = email, password = password, role = role)
                )
                _registerState.value = "Registrasi Berhasil! Silakan cek email Anda."
            } catch (e: IOException) {
                _registerState.value = "Network error: ${e.localizedMessage}"
            } catch (e: HttpException) {
                _registerState.value = "Kesalahan: ${e.localizedMessage}. Periksa kembali data yang dimasukkan."
            } catch (e: Exception) {
                _registerState.value = "Terjadi kesalahan: ${e.localizedMessage}"
            }  finally {
            _isLoading.value = false
            }
        }
    }

    fun resetRegisterState() {
        _registerState.value = null
    }
}