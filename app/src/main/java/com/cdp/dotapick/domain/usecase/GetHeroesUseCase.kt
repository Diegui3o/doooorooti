package com.cdp.dotapick.domain.usecase

import com.cdp.dotapick.data.model.Hero
import com.cdp.dotapick.data.repository.HeroRepository

class GetHeroesUseCase(
    private val repository: HeroRepository
) {
    suspend operator fun invoke(): List<Hero> {
        return repository.getAllHeroes()
    }
}