package com.tdk.nimons360.data.local

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("nimons360_prefs", Context.MODE_PRIVATE)

    fun saveSession(token: String, expiresAt: String) {
        prefs.edit()
            .putString("auth_token", token)
            .putString("auth_expires_at", expiresAt)
            .apply()
    }

    fun getToken(): String? = prefs.getString("auth_token", null)

    fun getExpiresAt(): String? = prefs.getString("auth_expires_at", null)

    fun isLoggedIn(): Boolean = !getToken().isNullOrBlank()

    fun clearSession() {
        prefs.edit()
            .remove("auth_token")
            .remove("auth_expires_at")
            .apply()
    }
}