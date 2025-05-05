package com.example.verdaapp.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferenceKeys {
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_TOKEN = stringPreferencesKey("user_token")
}