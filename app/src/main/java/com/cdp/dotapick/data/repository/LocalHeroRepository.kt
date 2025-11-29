package com.cdp.dotapick.data.repository

import com.cdp.dotapick.data.model.Counter
import com.cdp.dotapick.data.model.Hero
import com.cdp.dotapick.data.model.HeroItem

class LocalHeroRepository : HeroRepository {

    override suspend fun getAllHeroes(): List<Hero> {
        return listOf(
            // POSICIÓN 1 - CARRY
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
                id = 2,
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
                id = 2,
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
                id = 2,
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
                id = 3,
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
                id = 3,
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
                id = 4,
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
                id = 4,
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
                id = 4,
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
                id = 4,
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
                id = 5,
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
                id = 5,
                name = "npc_dota_hero_clockwerk",
                localizedName = "Clockwerk",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Incapacitador", "Resistente", "Iniciador"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/clockwerk.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/clockwerk.png",
                position = 3
            ),
            Hero(
                id = 5,
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
                id = 5,
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
                id = 5,
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
                id = 6,
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
                id = 6,
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
                id = 6,
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
                id = 6,
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
                id = 6,
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
                id = 6,
                name = "npc_dota_hero_doom",
                localizedName = "Doom",
                primaryAttr = "str",
                attackType = "Melee",
                roles = listOf("Carry", "Nuker", "Resistente"),
                img = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/doom.png",
                icon = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota_react/heroes/icons/doom.png",
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

    // ... el resto de los métodos se mantienen igual
    override suspend fun getHeroById(id: Int): Hero? {
        return getAllHeroes().find { it.id == id }
    }

    override suspend fun getCountersForHero(heroId: Int): List<Counter> {
        return emptyList()
    }

    override suspend fun getItemsAgainstHero(heroId: Int): List<HeroItem> {
        return emptyList()
    }
}