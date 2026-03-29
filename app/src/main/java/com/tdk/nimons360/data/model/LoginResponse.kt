package com.tdk.nimons360.data.model

data class LoginResponse(
    val data: LoginData
)

data class LoginData(
    val token: String,
    val expiresAt: String,
    val user: LoggedInUser
)

data class LoggedInUser(
    val id: Int,
    val nim: String,
    val email: String,
    val fullName: String
)
