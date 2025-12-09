package com.cdp.dotapick.ui.heroes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdp.dotapick.data.model.Hero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.cdp.dotapick.domain.model.SimpleScoringSystem

class HeroesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HeroesUiState>(HeroesUiState.Loading)
    val uiState: StateFlow<HeroesUiState> = _uiState

    private val _selectedTeam = MutableStateFlow<List<Hero>>(emptyList())
    val selectedTeam: StateFlow<List<Hero>> = _selectedTeam

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private var allHeroes: List<Hero> = emptyList()

    init {
        loadHeroes()
    }

    fun loadHeroes() {
        viewModelScope.launch {
            _uiState.value = HeroesUiState.Loading
            try {
                // createMockHeroes ya devuelve ordenado, si quieres puedes quitar el sortedBy de aquí
                allHeroes = createMockHeroes()
                updateHeroesList()
            } catch (e: Exception) {
                _uiState.value = HeroesUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        updateHeroesList()
    }

    fun selectHero(hero: Hero) {
        if (_selectedTeam.value.size < 5) {
            _selectedTeam.value = _selectedTeam.value + hero
            updateHeroesList()
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun removeHeroFromTeam(hero: Hero) {
        _selectedTeam.value = _selectedTeam.value.filter { it.id != hero.id }
        updateHeroesList()
    }

    fun clearTeam() {
        _selectedTeam.value = emptyList()
        updateHeroesList()
    }

    private fun updateHeroesList() {
        val selectedIds = _selectedTeam.value.map { it.id }
        val availableHeroes = if (_searchQuery.value.isBlank()) {
            allHeroes.filter { it.id !in selectedIds }
        } else {
            allHeroes.filter { hero ->
                hero.id !in selectedIds && (
                        hero.localizedName.contains(_searchQuery.value, ignoreCase = true) ||
                                hero.roles.any { it.contains(_searchQuery.value, ignoreCase = true) } ||
                                hero.getAttributeName().contains(_searchQuery.value, ignoreCase = true) ||
                                hero.getPositionName().contains(_searchQuery.value, ignoreCase = true)
                        )
            }
        }

        val heroesWithScores = if (_selectedTeam.value.isNotEmpty()) {
            availableHeroes.map { hero ->
                val score = SimpleScoringSystem.calculateSimpleScore(_selectedTeam.value, hero)
                HeroWithScore(hero, score)
            }.sortedWith(
                compareByDescending<HeroWithScore> { it.score }
                    .thenBy { it.hero.localizedName }
            )
        } else {
            availableHeroes
                .sortedBy { it.localizedName }
                .map { hero -> HeroWithScore(hero, 0.0) }
        }

        _uiState.update { currentState ->
            when (currentState) {
                is HeroesUiState.Success -> HeroesUiState.Success(heroesWithScores)
                is HeroesUiState.Loading -> HeroesUiState.Success(heroesWithScores)
                is HeroesUiState.Error -> currentState
            }
        }
    }

    // Data class para llevar hero + score
    data class HeroWithScore(val hero: Hero, val score: Double)

    sealed class HeroesUiState {
        object Loading : HeroesUiState()
        data class Success(val heroes: List<HeroWithScore>) : HeroesUiState()
        data class Error(val message: String) : HeroesUiState()
    }

    // 👇 AQUÍ va la función correctamente cerrada
    private fun createMockHeroes(): List<Hero> {
        val heroesBase = listOf(
            Hero(
                        name = "npc_dota_hero_antimage",
                        localizedName = "Anti-Mage",
                        primaryAttr = "agi",
                        attackType = "Melee",
                        roles = listOf("Carry", "Escape", "Nuker"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/antimage.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/antimage.png",
                        position = 1
                    ),

                    Hero(
                        name = "npc_dota_hero_juggernaut",
                        localizedName = "Juggernaut",
                        primaryAttr = "agi",
                        attackType = "Melee",
                        roles = listOf("Carry", "Pusher"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/juggernaut.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/juggernaut.png",
                        position = 1
                    ),
            Hero(
                name = "npc_dota_hero_marci",
                localizedName = "Marci",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Pusher"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/marci.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/marci.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_luna",
                localizedName = "Luna",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Pusher"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/luna.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/luna.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_medusa",
                localizedName = "Medusa",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Pusher"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/medusa.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/medusa.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_kez",
                localizedName = "Kez",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Pusher"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/kez.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/kez.png",
                position = 1
            ),
                    Hero(
                        name = "npc_dota_hero_bloodseeker",
                        localizedName = "Bloodseeker",
                        primaryAttr = "agi",
                        attackType = "Melee",
                        roles = listOf("Carry", "Iniciador", "Incapacitador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/bloodseeker.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/bloodseeker.png",
                        position = 1
                    ),
            Hero(
                name = "npc_dota_hero_slark",
                localizedName = "Slark",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Iniciador", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/slark.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/slark.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_monkey_king",
                localizedName = "Monkey King",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Iniciador", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/monkey_king.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/monkey_king.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_sniper",
                localizedName = "Sniper",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Iniciador", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/sniper.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/sniper.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_troll_warlord",
                localizedName = "Troll Warlord",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Iniciador", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/troll_warlord.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/troll_warlord.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_spectre",
                localizedName = "Spectre",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Iniciador", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/spectre.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/spectre.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_sven",
                localizedName = "Sven",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Iniciador", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/sven.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/sven.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_ursa",
                localizedName = "Ursa",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Iniciador", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/ursa.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/ursa.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_skeleton_king",
                localizedName = "Wraith King",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Iniciador", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/skeleton_king.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/skeleton_king.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_morphling",
                localizedName = "Morphling",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Iniciador", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/morphling.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/morphling.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_muerta",
                localizedName = "Muerta",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Iniciador", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/muerta.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/muerta.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_naga_siren",
                localizedName = "Naga Siren",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Iniciador", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/naga_siren.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/naga_siren.png",
                position = 1
            ),
                    Hero(
                        name = "npc_dota_hero_drow_ranger",
                        localizedName = "Drow Ranger",
                        primaryAttr = "agi",
                        attackType = "Melee",
                        roles = listOf("Carry", "Incapacitador", "Presionador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/drow_ranger.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/drow_ranger.png",
                        position = 1
                    ),
            Hero(
                name = "npc_dota_hero_weaver",
                localizedName = "Weaver",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Incapacitador", "Presionador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/weaver.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/weaver.png",
                position = 1
            ),
                    Hero(
                        name = "npc_dota_hero_chaos_knight",
                        localizedName = "Chaos Knight",
                        primaryAttr = "agi",
                        attackType = "Melee",
                        roles = listOf("Carry", "Iniciador", "Presionador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/chaos_knight.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/chaos_knight.png",
                        position = 1
                    ),
                    Hero(
                        name = "npc_dota_hero_clinkz",
                        localizedName = "Clinkz",
                        primaryAttr = "agi",
                        attackType = "Melee",
                        roles = listOf("Carry", "Evasivo", "Presionador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/clinkz.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/clinkz.png",
                        position = 1
                    ),
                    Hero(
                        name = "npc_dota_hero_faceless_void",
                        localizedName = "Faceless Void",
                        primaryAttr = "agi",
                        attackType = "Melee",
                        roles = listOf("Carry", "Incapacitador", "Iniciador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/faceless_void.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/faceless_void.png",
                        position = 1
                    ),
                    Hero(
                        name = "npc_dota_hero_gyrocopter",
                        localizedName = "Gyrocopter",
                        primaryAttr = "agi",
                        attackType = "Melee",
                        roles = listOf("Carry", "Nuker", "Incapacitador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/gyrocopter.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/gyrocopter.png",
                        position = 1
                    ),
            Hero(
                name = "npc_dota_hero_pangolier",
                localizedName = "Pangolier",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/pangolier.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/pangolier.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_phantom_assassin",
                localizedName = "Phantom Assassin",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/phantom_assassin.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/phantom_assassin.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_viper",
                localizedName = "Viper",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/viper.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/viper.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_riki",
                localizedName = "Riki",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/riki.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/riki.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_terrorblade",
                localizedName = "Terrorblade",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/terrorblade.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/terrorblade.png",
                position = 1
            ),
            Hero(
                name = "npc_dota_hero_phantom_lancer",
                localizedName = "Phantom Lancer",
                primaryAttr = "agi",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Incapacitador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/phantom_lancer.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/phantom_lancer.png",
                position = 1
            ),
                    // POSICIÓN 2 - MID
                    Hero(
                        name = "npc_dota_hero_ember_spirit",
                        localizedName = "Ember Spirit",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Carry", "Nuker", "Evasivo"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/ember_spirit.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/ember_spirit.png",
                        position = 2
                    ),
            Hero(
                name = "npc_dota_hero_storm_spirit",
                localizedName = "Storm Spirit",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Carry", "Nuker", "Evasivo"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/storm_spirit.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/storm_spirit.png",
                position = 2
            ),
            Hero(
                name = "npc_dota_hero_visage",
                localizedName = "Visage",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Carry", "Nuker", "Evasivo"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/visage.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/visage.png",
                position = 2
            ),
            Hero(
                name = "npc_dota_hero_windrunner",
                localizedName = "Windranger",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Carry", "Nuker", "Evasivo"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/windrunner.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/windrunner.png",
                position = 2
            ),
            Hero(
                name = "npc_dota_hero_zuus",
                localizedName = "Zeus",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Carry", "Nuker", "Evasivo"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/zuus.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/zuus.png",
                position = 2
            ),
            Hero(
                name = "npc_dota_hero_void_spirit",
                localizedName = "Void Spirit",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Carry", "Nuker", "Evasivo"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/void_spirit.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/void_spirit.png",
                position = 2
            ),
            Hero(
                name = "npc_dota_hero_lina",
                localizedName = "Lina",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Carry", "Nuker", "Evasivo"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/lina.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/lina.png",
                position = 2
            ),
                    Hero(
                        name = "npc_dota_hero_arc_warden",
                        localizedName = "Arc Warden",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Carry", "Nuker", "Evasivo"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/arc_warden.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/arc_warden.png",
                        position = 2
                    ),
                    Hero(
                        name = "npc_dota_hero_puck",
                        localizedName = "Puck",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Initiator", "Disabler", "Escape"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/puck.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/puck.png",
                        position = 2
                    ),
            Hero(
                name = "npc_dota_hero_nevermore",
                localizedName = "Shadow Fiend",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Initiator", "Disabler", "Escape"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/nevermore.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/nevermore.png",
                position = 2
            ),
                    Hero(
                        name = "npc_dota_hero_alchemist",
                        localizedName = "Alchemist",
                        primaryAttr = "int",
                        attackType = "Melee",
                        roles = listOf("Carry", "Nuker", "Iniciador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/alchemist.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/alchemist.png",
                        position = 2
                    ),
            Hero(
                name = "npc_dota_hero_meepo",
                localizedName = "Meepo",
                primaryAttr = "int",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Iniciador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/meepo.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/meepo.png",
                position = 2
            ),
            Hero(
                name = "npc_dota_hero_obsidian_destroyer",
                localizedName = "Outworld Destroyer",
                primaryAttr = "int",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Iniciador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/obsidian_destroyer.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/obsidian_destroyer.png",
                position = 2
            ),
                    Hero(
                        name = "npc_dota_hero_broodmother",
                        localizedName = "Broodmother",
                        primaryAttr = "int",
                        attackType = "Melee",
                        roles = listOf("Carry", "Nuker", "Presionador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/broodmother.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/broodmother.png",
                        position = 2
                    ),
            Hero(
                name = "npc_dota_hero_lone_druid",
                localizedName = "Lone Druid",
                primaryAttr = "int",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Presionador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/lone_druid.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/lone_druid.png",
                position = 2
            ),
                    Hero(
                        name = "npc_dota_hero_death_prophet",
                        localizedName = "Death Prophet",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Incapacitador", "Presionador", "Nuker"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/death_prophet.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/death_prophet.png",
                        position = 2
                    ),
            Hero(
                name = "npc_dota_hero_invoker",
                localizedName = "Invoker",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Incapacitador", "Presionador", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/invoker.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/invoker.png",
                position = 2
            ),
            Hero(
                name = "npc_dota_hero_tinker",
                localizedName = "Tinker",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Incapacitador", "Presionador", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/tinker.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/tinker.png",
                position = 2
            ),
            Hero(
                name = "npc_dota_hero_templar_assassin",
                localizedName = "Templar Assassin",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Incapacitador", "Presionador", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/templar_assassin.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/templar_assassin.png",
                position = 2
            ),
            Hero(
                name = "npc_dota_hero_pugna",
                localizedName = "Pugna",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Incapacitador", "Presionador", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/pugna.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/pugna.png",
                position = 2
            ),
            Hero(
                name = "npc_dota_hero_queenofpain",
                localizedName = "Queen of Pain",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Incapacitador", "Presionador", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/queenofpain.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/queenofpain.png",
                position = 2
            ),

                    // POSICIÓN 3 - OFFLANE
                    Hero(
                        name = "npc_dota_hero_abaddon",
                        localizedName = "Abaddon",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Carry", "Apoyo", "Resistente"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/abaddon.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/abaddon.png",
                        position = 3
                    ),
            Hero(
                name = "npc_dota_hero_necrolyte",
                localizedName = "Necrophos",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Apoyo", "Resistente"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/necrolyte.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/necrolyte.png",
                position = 3
            ),
            Hero(
                name = "npc_dota_hero_shredder",
                localizedName = "Timbersaw",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Apoyo", "Resistente"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/shredder.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/shredder.png",
                position = 3
            ),
            Hero(
                name = "npc_dota_hero_razor",
                localizedName = "Razor",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Apoyo", "Resistente"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/razor.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/razor.png",
                position = 3
            ),
            Hero(
                name = "npc_dota_hero_night_stalker",
                localizedName = "Night Stalker",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Apoyo", "Resistente"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/night_stalker.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/night_stalker.png",
                position = 3
            ),
                    Hero(
                        name = "npc_dota_hero_clockwerk",
                        localizedName = "Clockwerk",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Incapacitador", "Resistente", "Iniciador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/rattletrap.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/rattletrap.png",
                        position = 3
                    ),
                    Hero(
                        name = "npc_dota_hero_dawnbreaker",
                        localizedName = "Dawnbreaker",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Carry", "Resistente"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/dawnbreaker.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/dawnbreaker.png",
                        position = 3
                    ),
                    Hero(
                        name = "npc_dota_hero_centaur_warrunner",
                        localizedName = "Centaur Warrunner",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Carry", "Apoyo", "Resistente"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/centaur.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/centaur.png",
                        position = 3
                    ),
            Hero(
                name = "npc_dota_hero_huskar",
                localizedName = "Huskar",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Resistente", "Iniciador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/huskar.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/huskar.png",
                position = 3
            ),
                    Hero(
                        name = "npc_dota_hero_axe",
                        localizedName = "Axe",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Initiator", "Durable", "Disabler"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/axe.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/axe.png",
                        position = 3
                    ),
                    Hero(
                        name = "npc_dota_hero_tidehunter",
                        localizedName = "Tidehunter",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Initiator", "Durable", "Disabler"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/tidehunter.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/tidehunter.png",
                        position = 3
                    ),
            Hero(
                name = "npc_dota_hero_kunkka",
                localizedName = "Kunkka",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Initiator", "Durable", "Disabler"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/kunkka.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/kunkka.png",
                position = 3
            ),
            Hero(
                name = "npc_dota_hero_lycan",
                localizedName = "Lycan",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Initiator", "Durable", "Disabler"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/lycan.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/lycan.png",
                position = 3
            ),
            Hero(
                name = "npc_dota_hero_magnataur",
                localizedName = "Magnus",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Initiator", "Durable", "Disabler"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/magnataur.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/magnataur.png",
                position = 3
            ),
            Hero(
                name = "npc_dota_hero_legion_commander",
                localizedName = "Legion Commander",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Initiator", "Durable", "Disabler"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/legion_commander.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/legion_commander.png",
                position = 3
            ),
            Hero(
                name = "npc_dota_hero_primal_beast",
                localizedName = "Primal Beast",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Initiator", "Durable", "Disabler"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/primal_beast.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/primal_beast.png",
                position = 3
            ),
                    Hero(
                        name = "npc_dota_hero_batrider",
                        localizedName = "Batrider",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Incapacitador", "Evasivo", "Iniciador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/batrider.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/batrider.png",
                        position = 3
                    ),
                    Hero(
                        name = "npc_dota_hero_beastmaster",
                        localizedName = "Beastmaster",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Incapacitador", "Evasivo", "Iniciador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/beastmaster.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/beastmaster.png",
                        position = 3
                    ),
                    Hero(
                        name = "npc_dota_hero_brewmaster",
                        localizedName = "Brewmaster",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Carry", "Nuker", "Resistente"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/brewmaster.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/brewmaster.png",
                        position = 3
                    ),
            Hero(
                name = "npc_dota_hero_life_stealer",
                localizedName = "Lifestealer",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Resistente"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/life_stealer.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/life_stealer.png",
                position = 3
            ),
                    Hero(
                        name = "npc_dota_hero_bristleback",
                        localizedName = "Bristleback",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Carry", "Nuker", "Resistente"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/bristleback.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/bristleback.png",
                        position = 3
                    ),
            Hero(
                name = "npc_dota_hero_mars",
                localizedName = "Mars",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Resistente"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/mars.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/mars.png",
                position = 3
            ),
                    Hero(
                        name = "npc_dota_hero_doom",
                        localizedName = "Doom",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Carry", "Nuker", "Resistente"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/doom_bringer.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/doom_bringer.png",
                        position = 3
                    ),
                    Hero(
                        name = "npc_dota_hero_dragon_knight",
                        localizedName = "Dragon Knight",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Carry", "Nuker", "Resistente"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/dragon_knight.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/dragon_knight.png",
                        position = 3
                    ),
            Hero(
                name = "npc_dota_hero_slardar",
                localizedName = "Slardar",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Resistente"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/slardar.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/slardar.png",
                position = 3
            ),
            Hero(
                name = "npc_dota_hero_abyssal_underlord",
                localizedName = "Underlord",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Resistente"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/abyssal_underlord.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/abyssal_underlord.png",
                position = 3
            ),
            Hero(
                name = "npc_dota_hero_undying",
                localizedName = "Undying",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Resistente"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/undying.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/undying.png",
                position = 3
            ),
            Hero(
                name = "npc_dota_hero_sand_king",
                localizedName = "Sand King",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Resistente"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/sand_king.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/sand_king.png",
                position = 3
            ),
            Hero(
                name = "npc_dota_hero_tiny",
                localizedName = "Tiny",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Resistente"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/tiny.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/tiny.png",
                position = 3
            ),
                    Hero(
                        name = "npc_dota_hero_earthshaker",
                        localizedName = "Earthshaker",
                        primaryAttr = "str",
                        attackType = "Melee",
                        roles = listOf("Apoyo", "Incapacitador", "Iniciador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/earthshaker.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/earthshaker.png",
                        position = 3
                    ),
                    // POSICIÓN 4 - SOFT SUPPORT
                    Hero(
                        name = "npc_dota_hero_rubick",
                        localizedName = "Rubick",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Support", "Disabler", "Nuker"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/rubick.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/rubick.png",
                        position = 4
                    ),
            Hero(
                name = "npc_dota_hero_snapfire",
                localizedName = "Snapfire",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Support", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/snapfire.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/snapfire.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_leshrac",
                localizedName = "Leshrac",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Support", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/leshrac.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/leshrac.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_pudge",
                localizedName = "Pudge",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Support", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/pudge.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/pudge.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_tusk",
                localizedName = "Tusk",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Support", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/tusk.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/tusk.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_shadow_demon",
                localizedName = "Shadow Demon",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Support", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/shadow_demon.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/shadow_demon.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_nyx_assassin",
                localizedName = "Nyx Assassin",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Support", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/nyx_assassin.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/nyx_assassin.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_ogre_magi",
                localizedName = "Ogre Magi",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Support", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/ogre_magi.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/ogre_magi.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_vengefulspirit",
                localizedName = "Vengeful Spirit",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Support", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/vengefulspirit.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/vengefulspirit.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_venomancer",
                localizedName = "Venomancer",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Support", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/venomancer.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/venomancer.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_phoenix",
                localizedName = "Phoenix",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Support", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/phoenix.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/phoenix.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_omniknight",
                localizedName = "Omniknight",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Support", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/omniknight.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/omniknight.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_techies",
                localizedName = "Techies",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Support", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/techies.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/techies.png",
                position = 4
            ),
                    Hero(
                        name = "npc_dota_hero_hoodwink",
                        localizedName = "Hoodwink",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Apoyo", "Nuker", "Incapacitador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/hoodwink.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/hoodwink.png",
                        position = 4
                    ),
                    Hero(
                        name = "npc_dota_hero_earth_spirit",
                        localizedName = "Earth Spirit",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Nuker", "Incapacitador", "Iniciador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/earth_spirit.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/earth_spirit.png",
                        position = 4
                    ),
                    Hero(
                        name = "npc_dota_hero_enchantress",
                        localizedName = "Enchantress",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Presionador", "Apoyo", "Resistente"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/enchantress.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/enchantress.png",
                        position = 4
                    ),
                    Hero(
                        name = "npc_dota_hero_enigma",
                        localizedName = "Enigma",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Presionador", "Iniciador", "Incapacitador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/enigma.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/enigma.png",
                        position = 4
                    ),
                    Hero(
                        name = "npc_dota_hero_elder_titan",
                        localizedName = "Elder Titan",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Incapacitador", "Resistente", "Iniciador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/elder_titan.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/elder_titan.png",
                        position = 4
                    ),
                    Hero(
                        name = "npc_dota_hero_dark_seer",
                        localizedName = "Dark Seer",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Incapacitador", "Evasivo", "Iniciador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/dark_seer.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/dark_seer.png",
                        position = 4
                    ),
                    Hero(
                        name = "npc_dota_hero_bane",
                        localizedName = "Bane",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Apoyo", "Nuker", "Resistente"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/bane.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/bane.png",
                        position = 4
                    ),
                    Hero(
                        name = "npc_dota_hero_bounty_hunter",
                        localizedName = "Bounty Hunter",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Nuker", "Evasivo"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/bounty_hunter.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/bounty_hunter.png",
                        position = 4
                    ),
            Hero(
                name = "npc_dota_hero_furion",
                localizedName = "Nature's Prophet",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Nuker", "Evasivo"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/furion.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/furion.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_spirit_breaker",
                localizedName = "Spirit Breaker",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Nuker", "Evasivo"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/spirit_breaker.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/spirit_breaker.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_warlock",
                localizedName = "Warlock",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Nuker", "Evasivo"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/warlock.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/warlock.png",
                position = 4
            ),
                    Hero(
                        name = "npc_dota_hero_chen",
                        localizedName = "Chen",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Apoyo", "Presionador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/chen.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/chen.png",
                        position = 4
                    ),
            Hero(
                name = "npc_dota_hero_mirana",
                localizedName = "Mirana",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Presionador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/mirana.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/mirana.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_ringmaster",
                localizedName = "Ringmaster",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Presionador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/ringmaster.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/ringmaster.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_keeper_of_the_light",
                localizedName = "Keeper of the Light",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Presionador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/keeper_of_the_light.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/keeper_of_the_light.png",
                position = 4
            ),
            Hero(
                name = "npc_dota_hero_silencer",
                localizedName = "Silencer",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Presionador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/silencer.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/silencer.png",
                position = 4
            ),
                    // POSICIÓN 5 - HARD SUPPORT
                    Hero(
                        name = "npc_dota_hero_crystal_maiden",
                        localizedName = "Crystal Maiden",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Support", "Disabler", "Nuker"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/crystal_maiden.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/crystal_maiden.png",
                        position = 5
                    ),
                    Hero(
                        name = "npc_dota_hero_grimstroke",
                        localizedName = "Grimstroke",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Apoyo", "Evasivo", "Nuker"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/grimstroke.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/grimstroke.png",
                        position = 5
                    ),
            Hero(
                name = "npc_dota_hero_shadow_shaman",
                localizedName = "Shadow Shaman",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Evasivo", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/shadow_shaman.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/shadow_shaman.png",
                position = 5
            ),
            Hero(
                name = "npc_dota_hero_treant",
                localizedName = "Treant Protector",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Evasivo", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/treant.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/treant.png",
                position = 5
            ),
            Hero(
                name = "npc_dota_hero_oracle",
                localizedName = "Oracle",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Evasivo", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/oracle.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/oracle.png",
                position = 5
            ),
            Hero(
                name = "npc_dota_hero_skywrath_mage",
                localizedName = "Skywrath Mage",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Evasivo", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/skywrath_mage.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/skywrath_mage.png",
                position = 5
            ),
                    Hero(
                        name = "npc_dota_hero_disruptor",
                        localizedName = "Disruptor",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Apoyo", "Disabler", "Nuker"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/disruptor.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/disruptor.png",
                        position = 5
                    ),
            Hero(
                name = "npc_dota_hero_lion",
                localizedName = "Lion",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/lion.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/lion.png",
                position = 5
            ),
            Hero(
                name = "npc_dota_hero_wisp",
                localizedName = "Io",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/wisp.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/wisp.png",
                position = 5
            ),
            Hero(
                name = "npc_dota_hero_lich",
                localizedName = "Lich",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/lich.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/lich.png",
                position = 5
            ),
            Hero(
                name = "npc_dota_hero_jakiro",
                localizedName = "Jakiro",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Disabler", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/jakiro.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/jakiro.png",
                position = 5
            ),
                    Hero(
                        name = "npc_dota_hero_dazzle",
                        localizedName = "Dazzle",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Apoyo", "Incapacitador", "Nuker"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/dazzle.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/dazzle.png",
                        position = 5
                    ),
            Hero(
                name = "npc_dota_hero_witch_doctor",
                localizedName = "Witch Doctor",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Incapacitador", "Nuker"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/witch_doctor.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/witch_doctor.png",
                position = 5
            ),
                    Hero(
                        name = "npc_dota_hero_dark_willow",
                        localizedName = "Dark Willow",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Apoyo", "Incapacitador", "Evasivo"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/dark_willow.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/dark_willow.png",
                        position = 5
                    ),
            Hero(
                name = "npc_dota_hero_winter_wyvern",
                localizedName = "Winter Wyvern",
                primaryAttr = "int",
                attackType = "Ranged",
                roles = listOf("Apoyo", "Incapacitador", "Evasivo"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/winter_wyvern.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/winter_wyvern.png",
                position = 5
            ),
                    Hero(
                        name = "npc_dota_hero_ancient_apparition",
                        localizedName = "Ancient Apparition",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Apoyo", "Nuker", "Incapacitador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/ancient_apparition.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/ancient_apparition.png",
                        position = 5
                    )
        )

        // 👇 generas ids empezando en 1 y ordenado por nombre
        return heroesBase
            .sortedBy { it.localizedName }
            .mapIndexed { index, hero ->
                hero.copy(id = index + 1)
            }
    }
}
sealed class HeroesUiState {
    object Loading : HeroesUiState()
    data class Success(val heroes: List<HeroesViewModel.HeroWithScore>) : HeroesUiState()
    data class Error(val message: String) : HeroesUiState()
}