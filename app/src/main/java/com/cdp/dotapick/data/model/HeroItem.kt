package com.cdp.dotapick.data.model

data class HeroItem(
    val heroName: String,
    val itemId: Int,
    val itemName: String,
    val itemImage: String,
    val priority: Int,
    val reason: String,
    val itemType: String
)