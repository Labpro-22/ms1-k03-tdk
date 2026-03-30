package com.tdk.nimons360.ui.map

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
import androidx.compose.ui.unit.dp
import com.tdk.nimons360.ui.components.BottomNavBar
import com.tdk.nimons360.ui.components.BottomNavDestination
import com.tdk.nimons360.ui.families.CreateFamilyActivity
import com.tdk.nimons360.ui.families.FamiliesActivity
import com.tdk.nimons360.ui.home.HomeActivity

class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            startActivity(Intent(this@MapActivity, CreateFamilyActivity::class.java))
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Create Family")
                    }
                },
                bottomBar = {
                    BottomNavBar(
                        currentDestination = BottomNavDestination.MAP,
                        onHomeClick = {
                            startActivity(Intent(this@MapActivity, HomeActivity::class.java))
                            finish()
                        },
                        onMapClick = {},
                        onFamiliesClick = {
                            startActivity(Intent(this@MapActivity, FamiliesActivity::class.java))
                            finish()
                        }
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Dummy Map Page", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}