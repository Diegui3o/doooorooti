package com.cdp.dotapick.domain.model

import com.cdp.dotapick.data.model.Hero

// Sistema de puntuación simple -10 a +10
object SimpleScoringSystem {

    // Puntuaciones predefinidas entre héroes (relaciones bidireccionales)
    private val heroRelationships = mapOf(
        // Anti-Mage relationships
        1 to mapOf(
            2 to 1.71,
            3 to 3.76,
            4 to 3.7,
            5 to -4.18,
            6 to -4.5,
            7 to -2.0,
            8 to -3.0,
            9 to -0.7,
            10 to -1.63,
            11 to -1.7,
            12 to 0.2,
            13 to 2.2,
            14 to 0.9,
            15 to 0.1,
            16 to -1.2,
            17 to 3.7,
            18 to 0.0,
            19 to 1.4,
            20 to 0.9,
            21 to 3.9,
            22 to -0.2,
            23 to 0.7,
            24 to -0.9,
            25 to 1.3,
            26 to -0.1,
            27 to 3.0,
            28 to -0.5,
            29 to 0.4,
            30 to -1.7,
            31 to 1.5,
            32 to 4.1,
            33 to -1.0,
            34 to 2.0,
            35 to -1.5,
            36 to -0.7,
            37 to 1.6,
            38 to 0.2,
            39 to 1.2,
            40 to -1.4,
            41 to 0.7,
            42 to 2.2,
            43 to -0.9,
            44 to -0.9,
            45 to 2.4,
            46 to -0.9,
            47 to 1.6,
            48 to -1.7,
            49 to -1.1,
            50 to 1.0,
            51 to 0.4,
            52 to 1.5,
            53 to 0.7,
            54 to 1.9,
            55 to -0.6,
            56 to 2.2,
            57 to 0.6,
            58 to 1.1,
            59 to 1.1,
            60 to 1.6,
            61 to 1.3,
            62 to -1.0,
            63 to 1.8,
            64 to -0.7,
            65 to 0.9,
            66 to 2.2,
            67 to 1.2,
            68 to 1.6,
            69 to -0.7,
            70 to 0.3,
            72 to -1.4,
            73 to 1.3,
            74 to 2.0,
            75 to 3.2,
            76 to 1.9,
            77 to 0.3,
            78 to -0.4,
            79 to -0.6,
            80 to 1.5,
            81 to -1.4,
            82 to -0.3,
            83 to 2.2,
            84 to -0.9,
            85 to 1.4,
            86 to -0.5,
            87 to -0.3,
            88 to 2.2,
            89 to -1.4,
            90 to 3.0,
            91 to 0.4,
            92 to -1.0,
            93 to -4.2,
            94 to -1.0,
            95 to -0.6,
            96 to 1.8,
            97 to 0.7,
            98 to 0.9,
            99 to -3.0,
            100 to -3.0,
            101 to -0.4,
            102 to -0.7,
            103 to 1.8,
            104 to 0.8,
            105 to -0.7,
            106 to 0.9,
            107 to 1.0,
            108 to 1.2,
            109 to 2.4,
            110 to 1.4,
            111 to -0.5,
            112 to -2.2,
            113 to 1.6,
            114 to 1.7,
            115 to 3.6,
            116 to -0.5,
            117 to -1.0,
            118 to -0.7,
            119 to 2.5,
            120 to 0.8,
            121 to -1.4,
            122 to -0.3,
            123 to 1.0,
            124 to 0.1,
            125 to 0.2,
            126 to -0.4,
        ),

        // Axe relationships
        5 to mapOf(
            1 to 6.0,  // Axe BUENO contra Anti-Mage
            2 to 5.0,  // Axe BUENO contra Juggernaut
            8 to -3.0, // Axe MALO contra Crystal Maiden
            3 to -4.0  // Axe MALO contra Invoker
        ),

        // Invoker relationships
        3 to mapOf(
            1 to -4.0, // Invoker MALO contra Anti-Mage
            5 to 3.0,  // Invoker BUENO contra Axe
            7 to 2.0,  // Invoker BUENO contra Rubick
            8 to 4.0   // Invoker BUENO contra Crystal Maiden
        ),

        // Juggernaut relationships
        2 to mapOf(
            5 to -3.0, // Juggernaut MALO contra Axe
            8 to 4.0,  // Juggernaut BUENO contra Crystal Maiden
            3 to 2.0,  // Juggernaut BUENO contra Invoker
            7 to 1.0   // Juggernaut BUENO contra Rubick
        ),

        // Rubick relationships
        7 to mapOf(
            1 to -4.0, // Rubick MALO contra Anti-Mage
            3 to 2.0,  // Rubick BUENO contra Invoker
            8 to 3.0,  // Rubick BUENO contra Crystal Maiden
            5 to -2.0  // Rubick MALO contra Axe
        ),

        // Crystal Maiden relationships
        8 to mapOf(
            1 to 5.0,  // Crystal Maiden BUENA contra Anti-Mage
            2 to -4.0, // Crystal Maiden MALA contra Juggernaut
            5 to 3.0,  // Crystal Maiden BUENA contra Axe
            3 to -2.0  // Crystal Maiden MALA contra Invoker
        )
    )

    fun getScore(selectedHeroId: Int, candidateHeroId: Int): Double {
        // Obtener la relación directa A vs B
        val directScore = heroRelationships[selectedHeroId]?.get(candidateHeroId) ?: 0.0

        // Si no hay relación directa, verificar la relación inversa B vs A
        if (directScore == 0.0) {
            val inverseScore = heroRelationships[candidateHeroId]?.get(selectedHeroId) ?: 0.0
            // Si B es bueno contra A, entonces A es malo contra B (y viceversa)
            return -inverseScore
        }

        return directScore
    }

    // Reglas generales simples - SUMA todos los puntajes
    fun calculateSimpleScore(selectedTeam: List<Hero>, candidateHero: Hero): Double {
        if (selectedTeam.isEmpty()) return 0.0

        var totalScore = 0.0

        selectedTeam.forEach { selectedHero ->
            // Puntaje específico entre héroes (se SUMAN todos)
            val matchupScore = getScore(selectedHero.id, candidateHero.id)
            totalScore += matchupScore

            // Reglas básicas de composición (también se SUMAN)
            if (selectedHero.position != candidateHero.position) {
                totalScore += 1.0 // Bonus por diversidad de posiciones
            }

            if (selectedHero.primaryAttr != candidateHero.primaryAttr) {
                totalScore += 0.5 // Bonus por diversidad de atributos
            }
        }

        // Limitar entre -10 y +10
        return totalScore.coerceIn(-10.0, 10.0)
    }

    // Función para debugging - ver los puntajes individuales
    fun getScoreBreakdown(selectedTeam: List<Hero>, candidateHero: Hero): Map<String, Double> {
        val breakdown = mutableMapOf<String, Double>()

        selectedTeam.forEach { selectedHero ->
            val matchupScore = getScore(selectedHero.id, candidateHero.id)
            breakdown["${selectedHero.localizedName} vs ${candidateHero.localizedName}"] = matchupScore

            if (selectedHero.position != candidateHero.position) {
                breakdown["Bonus posición diferente"] = 1.0
            }

            if (selectedHero.primaryAttr != candidateHero.primaryAttr) {
                breakdown["Bonus atributo diferente"] = 0.5
            }
        }

        return breakdown
    }
}