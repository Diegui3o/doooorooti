package com.cdp.dotapick.di

import com.cdp.dotapick.data.repository.HeroRepository
import com.cdp.dotapick.data.repository.LocalHeroRepository
import com.cdp.dotapick.ui.heroes.HeroesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositorios
    single<HeroRepository> { LocalHeroRepository() }

    // ViewModels - Sin parámetros
    viewModel { HeroesViewModel() }
}