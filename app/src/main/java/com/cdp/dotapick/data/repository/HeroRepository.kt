package com.cdp.dotapick.data.repository

import com.cdp.dotapick.data.model.Counter
import com.cdp.dotapick.data.model.Hero
import com.cdp.dotapick.data.model.HeroItem

interface HeroRepository {
    suspend fun getAllHeroes(): List<Hero>
    suspend fun getHeroById(id: Int): Hero?
    suspend fun getCountersForHero(heroId: Int): List<Counter>
    suspend fun getItemsAgainstHero(heroId: Int): List<HeroItem>
}