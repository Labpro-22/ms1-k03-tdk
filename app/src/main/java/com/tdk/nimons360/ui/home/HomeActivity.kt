package com.tdk.nimons360.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.tdk.nimons360.data.local.SessionManager
import com.tdk.nimons360.data.remote.RetrofitClient
import com.tdk.nimons360.ui.auth.LoginActivity
import com.tdk.nimons360.ui.components.FamilyUiModel
import kotlinx.coroutines.launch
import android.content.Intent

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
            var fullName by remember { mutableStateOf("Labpro ITB") }
            var isLoading by remember { mutableStateOf(true) }

            val myFamilies = remember { dummyMyFamilies() }
            val discoverFamilies = remember { dummyDiscoverFamilies() }

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
                                fullName = response.body()!!.data.fullName
                            }
                        }
                    } catch (_: Exception) {
                    } finally {
                        isLoading = false
                    }
                }
            }

            HomeScreen(
                fullName = fullName,
                isLoading = isLoading,
                myFamilies = myFamilies,
                discoverFamilies = discoverFamilies,
                onProfileClick = {
                    Toast.makeText(this, "TODO: Open Profile", Toast.LENGTH_SHORT).show()
                },
                onCreateFamilyClick = {
                    Toast.makeText(this, "TODO: Open Create Family", Toast.LENGTH_SHORT).show()
                },
                onHomeClick = {
                    // sudah di Home
                },
                onMapClick = {
                    Toast.makeText(this, "TODO: Open Map", Toast.LENGTH_SHORT).show()
                },
                onFamiliesClick = {
                    Toast.makeText(this, "TODO: Open Families", Toast.LENGTH_SHORT).show()
                },
                onFamilyClick = { familyId ->
                    Toast.makeText(this, "Klik family id=$familyId", Toast.LENGTH_SHORT).show()
                },
                onJoinClick = { familyId ->
                    Toast.makeText(this, "Join family id=$familyId", Toast.LENGTH_SHORT).show()
                }
            )
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

    private fun dummyMyFamilies(): List<FamilyUiModel> {
        return listOf(
            FamilyUiModel(
                id = 1,
                name = "Maulana Family",
                memberCount = 4,
                memberInitials = listOf("L", "R", "A", "B"),
                iconEmoji = "🏠"
            ),
            FamilyUiModel(
                id = 2,
                name = "Study Group",
                memberCount = 7,
                memberInitials = listOf("B", "C", "D", "E"),
                iconEmoji = "🏫"
            ),
            FamilyUiModel(
                id = 3,
                name = "Camping Trip",
                memberCount = 3,
                memberInitials = listOf("E", "F", "G"),
                iconEmoji = "🏕️"
            )
        )
    }

    private fun dummyDiscoverFamilies(): List<FamilyUiModel> {
        return listOf(
            FamilyUiModel(
                id = 10,
                name = "Neighborhood Watch",
                memberCount = 5,
                memberInitials = listOf("S", "J", "L"),
                iconEmoji = "🌟"
            ),
            FamilyUiModel(
                id = 11,
                name = "ITB 2022 Batch",
                memberCount = 8,
                memberInitials = listOf("A", "B", "C"),
                iconEmoji = "🎓"
            ),
            FamilyUiModel(
                id = 12,
                name = "Weekend Warriors",
                memberCount = 6,
                memberInitials = listOf("M", "N", "K"),
                iconEmoji = "⚽"
            )
        )
    }
}