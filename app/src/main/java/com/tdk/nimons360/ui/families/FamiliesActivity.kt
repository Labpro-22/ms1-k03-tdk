package com.tdk.nimons360.ui.families

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tdk.nimons360.ui.components.BottomNavBar
import com.tdk.nimons360.ui.components.BottomNavDestination
import com.tdk.nimons360.ui.home.HomeActivity
import com.tdk.nimons360.ui.map.MapActivity

class FamiliesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            startActivity(Intent(this@FamiliesActivity, CreateFamilyActivity::class.java))
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Create Family")
                    }
                },
                bottomBar = {
                    BottomNavBar(
                        currentDestination = BottomNavDestination.FAMILIES,
                        onHomeClick = {
                            startActivity(Intent(this@FamiliesActivity, HomeActivity::class.java))
                            finish()
                        },
                        onMapClick = {
                            startActivity(Intent(this@FamiliesActivity, MapActivity::class.java))
                            finish()
                        },
                        onFamiliesClick = {}
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Dummy Families Page", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}