package com.cdp.dotapick.ui.heroes

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cdp.dotapick.data.model.Hero
import com.cdp.dotapick.ui.theme.DotaBlue
import com.cdp.dotapick.ui.theme.DotaGold
import com.cdp.dotapick.ui.theme.DotaGreen
import com.cdp.dotapick.ui.theme.DotaRed
import kotlinx.coroutines.delay

@Composable
fun HeroesScreen(
    viewModel: HeroesViewModel = viewModel(),
    onHeroSelected: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedTeam by viewModel.selectedTeam.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0A0A),
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E)
                    )
                )
            )
            .padding(bottom = 80.dp)
    ) {
        // Header (5%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.05f)
        ) {
            HeaderSection()
        }

        // Buscador (10%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            SearchSection(
                searchQuery = searchQuery,
                selectedTeam = selectedTeam,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                onClearSearch = { viewModel.clearSearch() },
                onClearTeam = { viewModel.clearTeam() }
            )
        }

        // Lista de héroes (75%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f)
        ) {
            HeroesListSection(
                uiState = uiState,
                selectedTeam = selectedTeam,
                onHeroClick = { viewModel.selectHero(it.hero) }
            )
        }

        // Equipo seleccionado (10%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            SelectedTeamSection(
                selectedTeam = selectedTeam,
                onRemoveHero = { viewModel.removeHeroFromTeam(it) }
            )
        }
    }
}
@Composable
fun HeroRow(
    item: HeroesViewModel.HeroWithScore
) {
    val hero = item.hero

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Imagen del héroe
        AsyncImage(
            model = hero.img,
            contentDescription = hero.localizedName,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            // 👇 ID al ladito de la imagen
            Text(
                text = "ID: ${hero.id}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = hero.localizedName,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "DOTA PICK",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun SearchSection(
    searchQuery: String,
    selectedTeam: List<Hero>, // ⬅️ Agregamos este parámetro
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onClearTeam: () -> Unit   // ⬅️ Agregamos este parámetro
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de búsqueda
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Campo de búsqueda
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Buscar héroes...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            // Botones a la derecha
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón para limpiar búsqueda
                if (searchQuery.isNotBlank()) {
                    IconButton(
                        onClick = onClearSearch,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Limpiar búsqueda",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Botón para limpiar equipo
                if (selectedTeam.isNotEmpty()) {
                    TextButton(
                        onClick = onClearTeam,
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(
                            text = "Limpiar",
                            style = MaterialTheme.typography.labelSmall,
                            color = DotaRed
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TeamInfoSection(
    selectedTeam: List<Hero>,
    onClearTeam: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Equipo: ${selectedTeam.size}/5",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            if (selectedTeam.isNotEmpty()) {
                TextButton(
                    onClick = onClearTeam,
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        text = "Limpiar",
                        style = MaterialTheme.typography.labelSmall,
                        color = DotaRed
                    )
                }
            }
        }
    }
}

@Composable
fun HeroesListSection(
    uiState: HeroesViewModel.HeroesUiState, // ⬅️ Tipo actualizado
    selectedTeam: List<Hero>,
    onHeroClick: (HeroesViewModel.HeroWithScore) -> Unit // ⬅️ Tipo actualizado
) {
    when (uiState) {
        is HeroesViewModel.HeroesUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DotaRed)
            }
        }
        is HeroesViewModel.HeroesUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = (uiState as HeroesViewModel.HeroesUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        is HeroesViewModel.HeroesUiState.Success -> {
            val heroes = (uiState as HeroesViewModel.HeroesUiState.Success).heroes

            if (heroes.isEmpty() && selectedTeam.size == 5) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "✅ Equipo completo",
                            style = MaterialTheme.typography.bodyLarge,
                            color = DotaGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Has seleccionado 5 héroes",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else if (heroes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron héroes",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(heroes) { heroWithScore ->
                        HeroItem(
                            heroWithScore = heroWithScore, // ⬅️ Nuevo parámetro
                            onHeroClick = { onHeroClick(heroWithScore) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedTeamSection(
    selectedTeam: List<Hero>,
    onRemoveHero: (Hero) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        if (selectedTeam.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Selecciona hasta 5 héroes",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                // Instrucción
                Text(
                    text = "Toca un héroe para removerlo",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Lista de héroes seleccionados
                LazyRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(selectedTeam) { hero ->
                        SelectedTeamHeroItem(
                            hero = hero,
                            onRemoveClick = { onRemoveHero(hero) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedTeamHeroItem(
    hero: Hero,
    onRemoveClick: () -> Unit
) {
    val positionColor = when (hero.position) {
        1 -> DotaRed
        2 -> DotaBlue
        3 -> DotaGreen
        4 -> Color(0xFF9C27B0)
        5 -> Color.White
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .size(60.dp)
            .clickable { onRemoveClick() } // ⬅️ TOCAR para remover
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(positionColor.copy(alpha = 0.3f))
                .align(Alignment.Center)
        ) {
            AsyncImage(
                model = hero.icon,
                contentDescription = "Icono de ${hero.localizedName}",
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun HeroItem(
    heroWithScore: HeroesViewModel.HeroWithScore,
    onHeroClick: () -> Unit
) {
    val hero = heroWithScore.hero
    val score = heroWithScore.score

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 150)
    )

    val roleName = when (hero.position) {
        1 -> "Carry"
        2 -> "Mid"
        3 -> "Offlane"
        4 -> "Support"
        5 -> "Hard Support"
        else -> "Unknown"
    }

    val roleColor = when (hero.position) {
        1 -> DotaRed
        2 -> DotaBlue
        3 -> DotaGreen
        4 -> Color(0xFFBA68C8)
        5 -> Color(0xFFFFF176)
        else -> Color.Gray
    }

    // 🎨 Neon glow (más intenso)
    val neonColor = roleColor.copy(alpha = 0.9f)

    val scoreColor = when {
        score > 0 -> DotaGreen
        score < 0 -> DotaRed
        else -> DotaGold
    }

    val scoreText = when {
        score > 0 -> "+${"%.2f".format(score)}"
        score < 0 -> "%.2f".format(score)
        else -> "0.00"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .scale(scale),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onHeroClick,
        interactionSource = interactionSource
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🌟 HERO AVATAR — circular + neon halo
            Box(
                modifier = Modifier
                    .size(90.dp),
                contentAlignment = Alignment.Center
            ) {
                // Halo externo
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .shadow(
                            elevation = 22.dp,
                            shape = CircleShape,
                            ambientColor = neonColor,
                            spotColor = neonColor
                        )
                        .background(neonColor.copy(alpha = 0.15f), CircleShape)
                )

                // Imagen circular
                AsyncImage(
                    model = hero.img,
                    contentDescription = hero.localizedName,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = neonColor,
                            shape = CircleShape
                        )
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Nombre
            Text(
                text = hero.localizedName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )

            // Rol + Score
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = roleName,
                    style = MaterialTheme.typography.labelMedium,
                    color = neonColor,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = scoreText,
                    style = MaterialTheme.typography.labelMedium,
                    color = scoreColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
