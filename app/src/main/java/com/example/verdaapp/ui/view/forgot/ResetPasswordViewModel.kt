package com.example.verdaapp.ui.view.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verdaapp.api.ApiConfig
import com.example.verdaapp.api.ResetPasswordData
import com.example.verdaapp.api.ResetPasswordRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResetPasswordViewModel: ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun requestPasswordReset(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().requestPasswordReset(ResetPasswordRequest(email))
                if (response.isSuccessful) {
                    _message.value = "Link reset password telah dikirim."
                } else {
                    _message.value = "Gagal mengirim permintaan reset password."
                }
            } catch (e: Exception) {
                _message.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetPassword(accessToken: String, newPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().resetPassword(ResetPasswordData(accessToken, newPassword))
                if (response.isSuccessful) {
                    _message.value = "Password berhasil diubah."
                } else {
                    _message.value = "Gagal mengubah password."
                }
            } catch (e: Exception) {
                _message.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
