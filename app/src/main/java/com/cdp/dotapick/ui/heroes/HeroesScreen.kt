package com.cdp.dotapick.ui.heroes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    val selectedHero by viewModel.selectedHero.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Fondo con gradiente Dota 2
    Box(
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
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header de la app
            HeaderSection()

            // Buscador
            SearchSection(
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                onClearSearch = { viewModel.clearSearch() }
            )

            // Héroe seleccionado (si hay uno)
            selectedHero?.let { hero ->
                SelectedHeroSection(
                    hero = hero,
                    onClearSelection = { viewModel.clearSelection() }
                )
            }

            // Lista de héroes
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

                    if (heroes.isEmpty() && searchQuery.isNotBlank()) {
                        // Mostrar mensaje cuando no hay resultados
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "No se encontraron héroes",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Intenta con otro nombre o rol",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
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
                                    isSelected = selectedHero?.id == hero.id,
                                    onHeroClick = {
                                        viewModel.selectHero(hero)
                                        onHeroSelected(hero.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        DotaRed,
                        DotaBlue,
                        DotaGreen
                    )
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "DOTA PICK",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
    }
}

@Composable
fun SearchSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de búsqueda
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Campo de búsqueda
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Buscar héroes por nombre, rol o atributo...",
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

            // Botón para limpiar búsqueda
            if (searchQuery.isNotBlank()) {
                IconButton(
                    onClick = onClearSearch,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Limpiar búsqueda",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun HeroItem(
    hero: com.cdp.dotapick.data.model.Hero,
    isSelected: Boolean,
    onHeroClick: () -> Unit
) {
    // Color según la POSICIÓN del héroe
    val positionColor = when (hero.position) {
        1 -> DotaRed.copy(alpha = 0.8f)      // Carry - Rojo
        2 -> DotaBlue.copy(alpha = 0.8f)     // Mid - Azul
        3 -> DotaGreen.copy(alpha = 0.8f)    // Offlane - Verde
        4 -> Color(0xFF9C27B0).copy(alpha = 0.8f) // Soft Support - Púrpura
        5 -> Color.White.copy(alpha = 0.8f)  // Hard Support - Blanco
        else -> Color.Gray.copy(alpha = 0.6f)
    }

    // Color de fondo según posición (más suave)
    val backgroundColor = when (hero.position) {
        1 -> DotaRed.copy(alpha = 0.15f)     // Carry - Rojo claro
        2 -> DotaBlue.copy(alpha = 0.15f)    // Mid - Azul claro
        3 -> DotaGreen.copy(alpha = 0.15f)   // Offlane - Verde claro
        4 -> Color(0xFF9C27B0).copy(alpha = 0.15f) // Soft Support - Púrpura claro
        5 -> Color.White.copy(alpha = 0.1f)  // Hard Support - Blanco muy claro
        else -> Color.Gray.copy(alpha = 0.05f)
    }

    // Color del borde según selección y posición
    val borderColor = when {
        isSelected -> DotaGold
        else -> positionColor.copy(alpha = 0.6f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) DotaGold.copy(alpha = 0.15f) else backgroundColor
        ),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder(false),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp),
        onClick = onHeroClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del héroe con marco del color de la posición
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(positionColor.copy(alpha = 0.3f))
            ) {
                AsyncImage(
                    model = hero.icon,
                    contentDescription = "Icono de ${hero.localizedName}",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información del héroe
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = hero.localizedName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Nombre de la posición con el color correspondiente
                Text(
                    text = hero.getPositionName(),
                    style = MaterialTheme.typography.bodySmall,
                    color = positionColor,
                    fontWeight = FontWeight.Bold
                )

                // Atributo principal
                Text(
                    text = "Atributo: ${hero.getAttributeName()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    maxLines = 1
                )

                // Roles
                Text(
                    text = hero.roles.joinToString(" • "),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            // Indicador de selección
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(DotaGold, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SelectedHeroSection(
    hero: com.cdp.dotapick.data.model.Hero,
    onClearSelection: () -> Unit
) {
    // ... (el resto del código de SelectedHeroSection se mantiene igual)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Imagen del héroe seleccionado
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        AsyncImage(
                            model = hero.icon,
                            contentDescription = "Icono de ${hero.localizedName}",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Contra:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = hero.localizedName,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Botón para cambiar selección
                IconButton(
                    onClick = onClearSelection,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cambiar héroe",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Selecciona otro héroe para ver counters",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}