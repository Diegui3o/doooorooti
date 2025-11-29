package com.cdp.dotapick.data.model

data class HeroItem(
    val heroId: Int,
    val itemId: Int,
    val itemName: String,
    val itemImage: String,
    val priority: Int,
    val reason: String,
    val itemType: String
)