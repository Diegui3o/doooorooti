package com.cdp.dotapick.ui.heroes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
            .padding(bottom = 80.dp) // ⬅️ PADDING PARA BOTONES DE NAVEGACIÓN
    ) {
        // Rectángulo 1: Header MINIMALISTA (5%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.05f)
        ) {
            HeaderSection()
        }

        // Rectángulo 2: Buscador CON BOTÓN LIMPIAR (10%)
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

        // Rectángulo 3: Lista de héroes disponibles (75%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f)
        ) {
            HeroesListSection(
                uiState = uiState,
                selectedTeam = selectedTeam,
                onHeroClick = { viewModel.selectHero(it) }
            )
        }

        // Rectángulo 4: Equipo seleccionado (10%)
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
    uiState: HeroesUiState,
    selectedTeam: List<Hero>,
    onHeroClick: (Hero) -> Unit
) {
    when (uiState) {
        is HeroesUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = DotaRed)
            }
        }
        is HeroesUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = (uiState as HeroesUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
        is HeroesUiState.Success -> {
            val heroes = (uiState as HeroesUiState.Success).heroes

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
                    items(heroes) { hero ->
                        HeroItem(
                            hero = hero,
                            onHeroClick = { onHeroClick(hero) }
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
    hero: Hero,
    onHeroClick: () -> Unit
) {
    val positionColor = when (hero.position) {
        1 -> DotaRed.copy(alpha = 0.8f)
        2 -> DotaBlue.copy(alpha = 0.8f)
        3 -> DotaGreen.copy(alpha = 0.8f)
        4 -> Color(0xFF9C27B0).copy(alpha = 0.8f)
        5 -> Color.White.copy(alpha = 0.8f)
        else -> Color.Gray.copy(alpha = 0.6f)
    }

    val backgroundColor = when (hero.position) {
        1 -> DotaRed.copy(alpha = 0.15f)
        2 -> DotaBlue.copy(alpha = 0.15f)
        3 -> DotaGreen.copy(alpha = 0.15f)
        4 -> Color(0xFF9C27B0).copy(alpha = 0.15f)
        5 -> Color.White.copy(alpha = 0.1f)
        else -> Color.Gray.copy(alpha = 0.05f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onHeroClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(positionColor.copy(alpha = 0.3f))
            ) {
                AsyncImage(
                    model = hero.icon,
                    contentDescription = "Icono de ${hero.localizedName}",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = hero.localizedName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = hero.getPositionName(),
                    style = MaterialTheme.typography.labelSmall,
                    color = positionColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}