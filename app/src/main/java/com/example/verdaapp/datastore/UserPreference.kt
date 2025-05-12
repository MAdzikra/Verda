package com.example.verdaapp.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreference(private val context: Context) {
    suspend fun saveUserData(name: String, token: String) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferenceKeys.USER_NAME] = name
            preferences[UserPreferenceKeys.USER_TOKEN] = token
        }
    }
}
