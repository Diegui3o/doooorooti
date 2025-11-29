package com.cdp.dotapick.ui.heroes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdp.dotapick.data.model.Hero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
                allHeroes = createMockHeroes().sortedBy { it.localizedName }
                filterHeroes(_searchQuery.value)
            } catch (e: Exception) {
                _uiState.value = HeroesUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterHeroes(query)
    }

    private fun filterHeroes(query: String) {
        val selectedIds = _selectedTeam.value.map { it.id }
        val filteredHeroes = if (query.isBlank()) {
            allHeroes.filter { it.id !in selectedIds }
        } else {
            allHeroes.filter { hero ->
                hero.id !in selectedIds && (
                        hero.localizedName.contains(query, ignoreCase = true) ||
                                hero.roles.any { it.contains(query, ignoreCase = true) } ||
                                hero.getAttributeName().contains(query, ignoreCase = true) ||
                                hero.getPositionName().contains(query, ignoreCase = true)
                        )
            }
        }

        _uiState.update { currentState ->
            when (currentState) {
                is HeroesUiState.Success -> HeroesUiState.Success(filteredHeroes)
                is HeroesUiState.Loading -> HeroesUiState.Success(filteredHeroes)
                is HeroesUiState.Error -> currentState
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        filterHeroes("")
    }

    fun selectHero(hero: Hero) {
        if (_selectedTeam.value.size < 5) {
            _selectedTeam.value = _selectedTeam.value + hero
            filterHeroes(_searchQuery.value) // Actualizar lista para remover el héroe seleccionado
        }
    }

    fun removeHeroFromTeam(hero: Hero) {
        _selectedTeam.value = _selectedTeam.value.filter { it.id != hero.id }
        filterHeroes(_searchQuery.value) // Actualizar lista para agregar el héroe de vuelta
    }

    fun clearTeam() {
        _selectedTeam.value = emptyList()
        filterHeroes(_searchQuery.value)
    }

    private fun createMockHeroes(): List<Hero> {
        // Tu lista actual de héroes aquí...
        return listOf(
                    Hero(
                        id = 1,
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
                        id = 2,
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
                        id = 3,
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
                        id = 4,
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
                        id = 5,
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
                        id = 6,
                        name = "npc_dota_hero_clinkz",
                        localizedName = "Clinkz",
                        primaryAttr = "agi",
                        attackType = "Melee",
                        roles = listOf("Carry", "Evasivo", "Presionador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/clinkz.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/clinkz.png",
                        position = 1
                    ),

                    // POSICIÓN 2 - MID
                    Hero(
                        id = 7,
                        name = "npc_dota_hero_invoker",
                        localizedName = "Invoker",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Carry", "Nuker", "Disabler"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/invoker.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/invoker.png",
                        position = 2
                    ),
                    Hero(
                        id = 8,
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
                        id = 9,
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
                        id = 10,
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
                        id = 11,
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
                        id = 12,
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
                        id = 13,
                        name = "npc_dota_hero_death_prophet",
                        localizedName = "Death Prophet",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Incapacitador", "Presionador", "Nuker"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/death_prophet.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/death_prophet.png",
                        position = 2
                    ),

                    // POSICIÓN 3 - OFFLANE
                    Hero(
                        id = 14,
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
                        id = 15,
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
                        id = 16,
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
                        id = 17,
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
                        id = 18,
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
                        id = 19,
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
                        id = 20,
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
                        id = 21,
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
                        id = 22,
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
                        id = 23,
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
                        id = 24,
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
                        id = 6,
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
                        id = 6,
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
                        id = 7,
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
                        id = 7,
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
                        id = 7,
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
                        id = 7,
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
                        id = 7,
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
                        id = 7,
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
                        id = 7,
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
                        id = 7,
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
                        id = 7,
                        name = "npc_dota_hero_chen",
                        localizedName = "Chen",
                        primaryAttr = "int",
                        attackType = "Ranged",
                        roles = listOf("Apoyo", "Presionador"),
                        img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/chen.png",
                        icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/chen.png",
                        position = 4
                    ),
                    // POSICIÓN 5 - HARD SUPPORT
                    Hero(
                        id = 8,
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
                        id = 8,
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
                        id = 8,
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
                        id = 8,
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
                        id = 8,
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
    }
}
sealed class HeroesUiState {
    object Loading : HeroesUiState()
    data class Success(val heroes: List<Hero>) : HeroesUiState()
    data class Error(val message: String) : HeroesUiState()
}