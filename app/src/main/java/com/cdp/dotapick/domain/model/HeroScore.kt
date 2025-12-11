package com.cdp.dotapick.domain.model

import com.cdp.dotapick.data.model.Hero

object SimpleScoringSystem {

    // Usamos el mapa que viene de HeroRelationshipsData.kt
    private val heroRelationshipsByName = HERO_RELATIONSHIPS

    fun getScore(selectedHero: Hero, candidateHero: Hero): Double {
        val directScore = heroRelationshipsByName[selectedHero.name]
            ?.get(candidateHero.name)

        if (directScore != null) return directScore

        val inverseScore = heroRelationshipsByName[candidateHero.name]
            ?.get(selectedHero.name)

        if (inverseScore != null) return -inverseScore

        return 0.0
    }

    // 👉 SOLO suma lo que tú definiste en heroRelationshipsByName
    fun calculateSimpleScore(selectedTeam: List<Hero>, candidateHero: Hero): Double {
        if (selectedTeam.isEmpty()) return 0.0

        var totalScore = 0.0

        selectedTeam.forEach { selectedHero ->
            val matchupScore = getScore(selectedHero, candidateHero)
            totalScore += matchupScore
        }
        return totalScore.coerceIn(-10.0, 10.0)
    }

    // Si usas breakdown, también sin bonus extra:
    fun getScoreBreakdown(selectedTeam: List<Hero>, candidateHero: Hero): Map<String, Double> {
        val breakdown = mutableMapOf<String, Double>()

        selectedTeam.forEach { selectedHero ->
            val matchupScore = getScore(selectedHero, candidateHero)
            breakdown["${selectedHero.localizedName} vs ${candidateHero.localizedName}"] = matchupScore
        }

        return breakdown
    }
}
