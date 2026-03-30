package com.tdk.nimons360.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

enum class BottomNavDestination {
    HOME, MAP, FAMILIES
}

@Composable
fun BottomNavBar(
    currentDestination: BottomNavDestination,
    onHomeClick: () -> Unit,
    onMapClick: () -> Unit,
    onFamiliesClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentDestination == BottomNavDestination.HOME,
            onClick = onHomeClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = {Text("Home")}
        )

        NavigationBarItem(
            selected = currentDestination == BottomNavDestination.MAP,
            onClick = onMapClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Map"
                )
            },
            label = { Text("Map") }
        )

        NavigationBarItem(
            selected = currentDestination == BottomNavDestination.FAMILIES,
            onClick = onFamiliesClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Families"
                )
            },
            label = { Text("Families") }
        )
    }
}