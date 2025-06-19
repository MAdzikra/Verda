package com.example.verdaapp.supabase

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient

object SupabaseClient {
    val instance = createSupabaseClient(
        supabaseUrl = "https://gncwiljqegllybkibmeq.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImduY3dpbGpxZWdsbHlia2libWVxIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0Mjk3MjQ5MiwiZXhwIjoyMDU4NTQ4NDkyfQ.TGXHlykinTGKd_aueNY3-SS3w7xfFvgV-KhfbSDz9Dk",
    ) {
        install(Auth)
        install(ComposeAuth) {
            googleNativeLogin(serverClientId = "396510804696-r48b9614it8affjpqfjfv7jjkokf29l9.apps.googleusercontent.com")
        }
    }
}