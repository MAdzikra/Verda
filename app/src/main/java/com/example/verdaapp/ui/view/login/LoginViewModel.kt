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
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()


    fun login(context: Context, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().login(LoginRequest(email, password))
                saveUserData(context, response.user.nama, response.token, response.user.id)
                _loginState.value = response.message ?: "Login sukses"
                _loginError.value = null
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                if (errorBody != null && errorBody.contains("Email atau password salah")) {
                    _loginError.value = "Incorrect email or password"
                } else {
                    _loginError.value = "Unable to login. Please try again."
                }
                _loginState.value = null
            } catch (e: Exception) {
                _loginError.value = "Terjadi kesalahan: ${e.message}"
                _loginState.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun saveUserData(context: Context, name: String, token: String, userId: String) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[UserPreferenceKeys.USER_NAME] = name
                preferences[UserPreferenceKeys.USER_TOKEN] = token
                preferences[UserPreferenceKeys.USER_ID_KEY] = userId
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

    fun resetLoginError(){
        _loginError.value = null
    }
}