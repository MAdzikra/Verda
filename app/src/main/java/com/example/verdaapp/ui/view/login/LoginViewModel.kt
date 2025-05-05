package com.example.verdaapp.ui.view.login

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verdaapp.api.ApiConfig
import com.example.verdaapp.api.LoginRequest
import com.example.verdaapp.datastore.UserPreferenceKeys
import com.example.verdaapp.datastore.dataStore
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

    fun login(context: Context, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().login(LoginRequest(email, password))
                saveUserData(context, response.user.nama, response.token)
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

    private fun saveUserData(context: Context, name: String, token: String) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[UserPreferenceKeys.USER_NAME] = name
                preferences[UserPreferenceKeys.USER_TOKEN] = token
            }
        }
    }

    fun clearUserData(context: Context) {
        viewModelScope.launch {
            context.dataStore.edit { it.clear() }
        }
    }

    fun resetLoginState() {
        _loginState.value = null
    }
}