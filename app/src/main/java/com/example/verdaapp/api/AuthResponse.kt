package com.example.verdaapp.api

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
	@SerializedName("password")
	val password: String? = null,

	@SerializedName("role")
	val role: String? = null,

	@SerializedName("nama")
	val username: String? = null,

	@SerializedName("email")
	val email: String? = null
)

data class LoginRequest(
	@SerializedName("email")
	val email: String? = null,

	@SerializedName("password")
	val password: String? = null
)

data class AuthResponse (
	val message: String
)

