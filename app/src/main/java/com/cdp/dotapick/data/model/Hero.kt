package com.cdp.dotapick.data.model

data class Hero(
    val id: Int = 0,
    val name: String,
    val localizedName: String,
    val primaryAttr: String,
    val attackType: String,
    val roles: List<String>,
    val img: String,
    val icon: String,
    val position: Int
){
    fun getPositionName(): String {
        return when (position) {
            1 -> "Carry"
            2 -> "Mid"
            3 -> "Offlane"
            4 -> "Soft Support"
            5 -> "Hard Support"
            else -> "Unknown"
        }
    }

    fun getAttributeName(): String {
        return when (primaryAttr) {
            "str" -> "Fuerza"
            "agi" -> "Agilidad"
            "int" -> "Inteligencia"
            else -> "Desconocido"
        }
    }
}
