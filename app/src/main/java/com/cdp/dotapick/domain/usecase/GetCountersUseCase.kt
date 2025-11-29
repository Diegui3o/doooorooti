package com.cdp.dotapick.domain.usecase

import com.cdp.dotapick.data.model.Counter
import com.cdp.dotapick.data.repository.HeroRepository

class GetCountersUseCase(
    private val repository: HeroRepository
) {
    suspend operator fun invoke(heroId: Int): List<Counter> {
        return repository.getCountersForHero(heroId)
    }
}