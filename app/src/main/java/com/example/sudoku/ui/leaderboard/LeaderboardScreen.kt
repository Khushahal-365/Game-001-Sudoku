package com.example.sudoku.ui.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sudoku.data.model.LeaderboardEntry
import com.example.sudoku.ui.components.AppIcons
import com.example.sudoku.ui.game.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    viewModel: GameViewModel,
    onNavigateBack: () -> Unit = {},
    showBackButton: Boolean = false,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All Time") }
    val filters = listOf(
        Triple("All Time", AppIcons.Trophy, Color(0xFFD97706)),
        Triple("This Week", AppIcons.Flame, Color(0xFFEF4444)),
        Triple("Daily Cup", AppIcons.Star, Color(0xFF2563EB))
    )

    val playerName by viewModel.playerName.collectAsStateWithLifecycle()
    val playerAvatarIndex by viewModel.playerAvatarIndex.collectAsStateWithLifecycle()
    val coins by viewModel.coins.collectAsStateWithLifecycle()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var editedName by remember(playerName) { mutableStateOf(playerName) }

    val entries = remember(selectedFilter, playerName, playerAvatarIndex, coins) {
        viewModel.getLeaderboardEntries(filter = selectedFilter)
    }

    val top3 = remember(entries) { entries.take(3) }
    val rest = remember(entries) { entries.drop(3) }
    val userEntry = remember(entries) { entries.find { it.isUser } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF3C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = AppIcons.Leaderboard,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "Leaderboard",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFEF3C7),
                        modifier = Modifier
                            .clickable { showEditProfileDialog = true }
                            .padding(end = 12.dp)
                            .testTag("leaderboard_edit_profile_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = AppIcons.Pencil,
                                contentDescription = "Edit Profile",
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFF92400E)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Edit",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Filter Pills
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filters.forEach { (filterName, filterIcon, iconColor) ->
                        val isSelected = filterName == selectedFilter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = filterName },
                            leadingIcon = {
                                Icon(
                                    imageVector = filterIcon,
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else iconColor,
                                    modifier = Modifier.size(15.dp)
                                )
                            },
                            label = { Text(filterName, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Top 3 Podium
            if (top3.size >= 3) {
                item {
                    PodiumSection(top3 = top3)
                }
            }

            // "Your Rank" sticky banner if user is not in top 3
            if (userEntry != null && userEntry.rank > 3) {
                item {
                    UserRankBanner(entry = userEntry, onClickEdit = { showEditProfileDialog = true })
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Global Solvers",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${entries.size} Players",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Rest of rankings (#4 and onwards)
            items(rest) { entry ->
                LeaderboardRow(entry = entry)
            }
        }
    }

    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Player Profile", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Display Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                    )

                    Text("Choose Avatar Emoji:", style = MaterialTheme.typography.labelMedium)
                    val emojis = listOf("🦁", "🦊", "🐼", "🦅", "🐺", "🐯", "🦉", "🦄", "🐉", "🐬", "🦋", "🚀")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        emojis.take(6).forEachIndexed { idx, emo ->
                            val isChosen = playerAvatarIndex == idx
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (isChosen) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                                    .border(
                                        width = if (isChosen) 2.dp else 0.dp,
                                        color = if (isChosen) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { viewModel.setPlayerAvatarIndex(idx) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(emo, fontSize = 20.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setPlayerName(editedName)
                        showEditProfileDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun PodiumSection(top3: List<LeaderboardEntry>) {
    val second = top3.getOrNull(1)
    val first = top3.getOrNull(0)
    val third = top3.getOrNull(2)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            // 2nd Place (Silver)
            second?.let {
                PodiumColumn(
                    entry = it,
                    podiumHeight = 90.dp,
                    badgeColor = Color(0xFF94A3B8),
                    podiumColor = Color(0xFFE2E8F0),
                    crownIcon = false
                )
            }

            // 1st Place (Gold)
            first?.let {
                PodiumColumn(
                    entry = it,
                    podiumHeight = 120.dp,
                    badgeColor = Color(0xFFF59E0B),
                    podiumColor = Color(0xFFFEF3C7),
                    crownIcon = true
                )
            }

            // 3rd Place (Bronze)
            third?.let {
                PodiumColumn(
                    entry = it,
                    podiumHeight = 70.dp,
                    badgeColor = Color(0xFFD97706),
                    podiumColor = Color(0xFFFFEDD5),
                    crownIcon = false
                )
            }
        }
    }
}

@Composable
private fun PodiumColumn(
    entry: LeaderboardEntry,
    podiumHeight: androidx.compose.ui.unit.Dp,
    badgeColor: Color,
    podiumColor: Color,
    crownIcon: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(96.dp)
    ) {
        if (crownIcon) {
            Icon(
                imageVector = AppIcons.Crown,
                contentDescription = "Champion Crown",
                tint = Color(0xFFF59E0B),
                modifier = Modifier.size(24.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Avatar
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(entry.avatarBgColorHex))
                .border(2.dp, badgeColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(entry.avatarEmoji, fontSize = 26.sp)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (entry.isUser) "You" else entry.name,
            fontWeight = if (entry.isUser) FontWeight.ExtraBold else FontWeight.Bold,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        Text(
            text = "${entry.score} pts",
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Podium step pillar
        Box(
            modifier = Modifier
                .width(84.dp)
                .height(podiumHeight)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(podiumColor, podiumColor.copy(alpha = 0.6f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#${entry.rank}",
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                color = badgeColor
            )
        }
    }
}

@Composable
private fun UserRankBanner(entry: LeaderboardEntry, onClickEdit: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth().testTag("user_rank_banner")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "#${entry.rank}",
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(entry.avatarBgColorHex)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(entry.avatarEmoji, fontSize = 22.sp)
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${entry.name} (You)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = entry.badge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "${entry.score} Points • ${entry.winCount} Wins",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            IconButton(onClick = onClickEdit) {
                Icon(
                    imageVector = AppIcons.Pencil,
                    contentDescription = "Edit Profile",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun LeaderboardRow(entry: LeaderboardEntry) {
    val isUser = entry.isUser
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isUser) 3.dp else 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("leaderboard_row_${entry.rank}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "#${entry.rank}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(32.dp)
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(entry.avatarBgColorHex)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(entry.avatarEmoji, fontSize = 20.sp)
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isUser) "${entry.name} (You)" else entry.name,
                            fontWeight = if (isUser) FontWeight.ExtraBold else FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = if (isUser) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "${entry.winCount} wins • ${entry.badge}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "${entry.score} pts",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
