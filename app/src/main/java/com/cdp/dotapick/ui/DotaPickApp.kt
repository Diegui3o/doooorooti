package com.cdp.dotapick.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cdp.dotapick.ui.heroes.HeroesScreen
import com.cdp.dotapick.ui.theme.DotaPickTheme

@Composable
fun DotaPickApp() {
    DotaPickTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            HeroesScreen()
        }
    }
}