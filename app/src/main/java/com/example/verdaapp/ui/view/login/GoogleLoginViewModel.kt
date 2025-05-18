package com.example.verdaapp.ui.view.login

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verdaapp.api.ApiConfig
import com.example.verdaapp.api.SyncProfileRequest
import com.example.verdaapp.datastore.UserPreference
import com.example.verdaapp.datastore.UserPreferenceKeys
import com.example.verdaapp.datastore.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GoogleLoginViewModel : ViewModel() {

    private val _syncSuccess = MutableStateFlow(false)
    val syncSuccess: StateFlow<Boolean> = _syncSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun syncGoogleUser(context: Context, userId: String, email: String, nama: String, token: String) {
        viewModelScope.launch {
            try {
                val request = SyncProfileRequest(userId, email, nama)
                val response = ApiConfig.getApiService().syncProfile(request)
                Log.d("GoogleLoginViewModel", "Profile sync success: ${response.message}")
                saveUserData(context, nama, token, userId)
                _syncSuccess.value = true
            } catch (e: Exception) {
                Log.e("GoogleLoginViewModel", "Profile sync failed", e)
                _errorMessage.value = e.message
                _syncSuccess.value = false
            }
        }
    }

    fun saveUserData(context: Context, name: String, token: String, userId: String) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[UserPreferenceKeys.USER_NAME] = name
                preferences[UserPreferenceKeys.USER_TOKEN] = token
                preferences[UserPreferenceKeys.USER_ID_KEY] = userId
            }
        }
    }

    fun resetState() {
        _syncSuccess.value = false
        _errorMessage.value = null
    }
}