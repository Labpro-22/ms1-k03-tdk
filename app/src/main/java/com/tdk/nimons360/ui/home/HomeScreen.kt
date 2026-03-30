package com.tdk.nimons360.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tdk.nimons360.ui.components.AppTopBar
import com.tdk.nimons360.ui.components.BottomNavBar
import com.tdk.nimons360.ui.components.BottomNavDestination
import com.tdk.nimons360.ui.components.DiscoverFamilyRow
import com.tdk.nimons360.ui.components.FamilyUiModel
import com.tdk.nimons360.ui.components.MyFamilyCard
import com.tdk.nimons360.ui.theme.NimonsColors
import androidx.compose.foundation.background

@Composable
fun HomeScreen(
    fullName: String,
    isLoading: Boolean,
    myFamilies: List<FamilyUiModel>,
    discoverFamilies: List<FamilyUiModel>,
    onProfileClick: () -> Unit,
    onCreateFamilyClick: () -> Unit,
    onHomeClick: () -> Unit,
    onMapClick: () -> Unit,
    onFamiliesClick: () -> Unit,
    onFamilyClick: (Int) -> Unit,
    onJoinClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Nimons360",
                fullName = fullName,
                onAvatarClick = onProfileClick
            )
        },
        bottomBar = {
            BottomNavBar(
                currentDestination = BottomNavDestination.HOME,
                onHomeClick = onHomeClick,
                onMapClick = onMapClick,
                onFamiliesClick = onFamiliesClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateFamilyClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Family"
                )
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NimonsColors.ScreenBackground)
                    .padding(innerPadding)
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(24.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)

            ) {
                item {
                    Text(
                        text = "MY FAMILIES",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(myFamilies) {family ->
                            MyFamilyCard(family = family, onClick = { onFamilyClick(family.id) })
                        }
                    }
                }

                item {
                    Text(
                        text = "DISCOVER FAMILIES",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                items(discoverFamilies) { family ->
                    DiscoverFamilyRow(
                        family = family,
                        onClick = { onFamilyClick(family.id) },
                        onJoinClick = { onJoinClick(family.id) }
                    )
                }
            }
        }

    }
}