package com.tdk.nimons360.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.tdk.nimons360.data.local.SessionManager
import com.tdk.nimons360.data.model.DiscoverFamily
import com.tdk.nimons360.data.model.MyFamily
import com.tdk.nimons360.data.remote.RetrofitClient
import com.tdk.nimons360.ui.auth.LoginActivity
import com.tdk.nimons360.ui.components.FamilyUiModel
import com.tdk.nimons360.ui.families.CreateFamilyActivity
import com.tdk.nimons360.ui.families.FamilyDetailActivity
import com.tdk.nimons360.ui.families.FamiliesActivity
import com.tdk.nimons360.ui.map.MapActivity
import com.tdk.nimons360.ui.profile.ProfileActivity
import com.tdk.nimons360.utils.FamilyIcons
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
            var fullName by remember { mutableStateOf("User") }
            var isLoading by remember { mutableStateOf(true) }
            var myFamilies by remember { mutableStateOf(emptyList<FamilyUiModel>()) }
            var discoverFamilies by remember { mutableStateOf(emptyList<FamilyUiModel>()) }

            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    try {
                        val api = RetrofitClient.create(this@HomeActivity)

                        val meResponse = api.getMe()
                        if (handleSessionExpired(meResponse.code())) return@launch
                        if (meResponse.isSuccessful && meResponse.body() != null) {
                            fullName = meResponse.body()!!.data.fullName
                        }

                        val myFamiliesResponse = api.getMyFamilies()
                        if (handleSessionExpired(myFamiliesResponse.code())) return@launch
                        if (myFamiliesResponse.isSuccessful && myFamiliesResponse.body() != null) {
                            myFamilies = mapMyFamiliesToUi(myFamiliesResponse.body()!!.data)
                        }

                        val discoverFamiliesResponse = api.getDiscoverFamilies()
                        if (handleSessionExpired(discoverFamiliesResponse.code())) return@launch
                        if (discoverFamiliesResponse.isSuccessful && discoverFamiliesResponse.body() != null) {
                            discoverFamilies = mapDiscoverFamiliesToUi(discoverFamiliesResponse.body()!!.data)
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@HomeActivity,
                            "Gagal memuat data Home",
                            Toast.LENGTH_SHORT
                        ).show()
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
                    startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
                },
                onCreateFamilyClick = {
                    startActivity(Intent(this@HomeActivity, CreateFamilyActivity::class.java))
                },
                onHomeClick = {
                    // sudah di Home
                },
                onMapClick = {
                    startActivity(Intent(this@HomeActivity, MapActivity::class.java))
                    finish()
                },
                onFamiliesClick = {
                    startActivity(Intent(this@HomeActivity, FamiliesActivity::class.java))
                    finish()
                },
                onFamilyClick = { familyId ->
                    val family = myFamilies.find { it.id == familyId }
                        ?: discoverFamilies.find { it.id == familyId }

                    val isMember = myFamilies.any { it.id == familyId }

                    startActivity(
                        FamilyDetailActivity.newIntent(
                            context = this@HomeActivity,
                            familyId = familyId,
                            familyName = family?.name ?: "Family Detail",
                            isMember = isMember
                        )
                    )
                },
                onJoinClick = { familyId ->
                    val family = discoverFamilies.find { it.id == familyId }

                    startActivity(
                        FamilyDetailActivity.newIntent(
                            context = this@HomeActivity,
                            familyId = familyId,
                            familyName = family?.name ?: "Join Family",
                            isMember = false
                        )
                    )
                }
            )
        }
    }

    private fun handleSessionExpired(code: Int): Boolean {
        if (code == 409) {
            sessionManager.clearSession()
            goToLogin()
            return true
        }
        return false
    }

    private fun goToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        )
        finish()
    }



    private fun mapMyFamiliesToUi(items: List<MyFamily>): List<FamilyUiModel> {
        return items.map { family ->
            FamilyUiModel(
                id = family.id,
                name = family.name,
                memberCount = family.members.size,
                memberInitials = family.members.map { extractInitial(it.fullName) },
                iconUrl = family.iconUrl
            )
        }
    }

    private fun mapDiscoverFamiliesToUi(items: List<DiscoverFamily>): List<FamilyUiModel> {
        return items.map { family ->
            FamilyUiModel(
                id = family.id,
                name = family.name,
                memberCount = family.members.size,
                memberInitials = family.members.map { extractInitial(it.fullName) },
                iconUrl = family.iconUrl
            )
        }
    }

    private fun extractInitial(fullName: String): String {
        val first = fullName.trim().firstOrNull { it.isLetterOrDigit() } ?: '?'
        return first.uppercaseChar().toString()
    }

}