package com.tdk.nimons360.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class FamilyUiModel(
    val id: Int,
    val name: String,
    val memberCount: Int,
    val memberInitials: List<String>,
    val iconEmoji: String = "🏠"
)

private val avatarColors = listOf(
    Color(0xFF1976D2),
    Color(0xFFD32F2F),
    Color(0xFF388E3C),
    Color(0xFFF57C00),
    Color(0xFF7B1FA2),
    Color(0xFF0097A7)
)

@Composable
private fun SmallAvatar(text: String, bgColor: Color) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .background(bgColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AvatarGroup(
    initials: List<String>,
    maxVisible: Int = 3
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        initials.take(maxVisible).forEachIndexed { index, initials ->
            SmallAvatar(
                text = initials,
                bgColor = avatarColors[index % avatarColors.size]
            )
        }

        val extra = initials.size - maxVisible
        if (extra > 0) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$extra",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun MyFamilyCard(
    family: FamilyUiModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFFFF3E0), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = family.iconEmoji)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = family.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${family.memberCount} members",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            AvatarGroup(initials = family.memberInitials, maxVisible = 3)
        }
    }
}

@Composable
fun DiscoverFamilyRow(
    family: FamilyUiModel,
    onClick: () -> Unit,
    onJoinClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFFFF9C4), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = family.iconEmoji)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = family.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        AvatarGroup(initials = family.memberInitials, maxVisible = 3)

        Spacer(modifier = Modifier.width(8.dp))

        TextButton(onClick = onJoinClick) {
            Text("Join")
        }

    }
}