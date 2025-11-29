package com.cdp.dotapick.domain.usecase

import com.cdp.dotapick.data.model.HeroItem
import com.cdp.dotapick.data.repository.HeroRepository

class GetItemsAgainstHeroUseCase(
    private val repository: HeroRepository
) {
    suspend operator fun invoke(heroId: Int): List<HeroItem> {
        return repository.getItemsAgainstHero(heroId)
    }
}