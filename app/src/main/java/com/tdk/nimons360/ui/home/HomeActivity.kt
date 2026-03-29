package com.tdk.nimons360.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.tdk.nimons360.data.local.SessionManager
import com.tdk.nimons360.data.remote.RetrofitClient
import com.tdk.nimons360.ui.auth.LoginActivity
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        if (!sessionManager.isLoggedIn()) {
            goToLogin()
            return
        }

        setContent {
            var message by remember { mutableStateOf("Loading profile...") }

            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    try {
                        val response = RetrofitClient.create(this@HomeActivity).getMe()

                        when {
                            response.code() == 409 -> {
                                sessionManager.clearSession()
                                goToLogin()
                            }

                            response.isSuccessful && response.body() != null -> {
                                val user = response.body()!!.data
                                message = "Welcome, ${user.fullName}"
                            }

                            else -> {
                                message = "Gagal memuat data user."
                            }
                        }
                    } catch (e: Exception) {
                        message = "Gagal terhubung ke server."
                    }
                }
            }

            MaterialTheme {
                HomeScreen(message)
            }
        }
    }

    private fun goToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        )
        finish()
    }
}

@Composable
fun HomeScreen(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message)
    }
}