package com.cdp.dotapick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cdp.dotapick.ui.DotaPickApp
import com.cdp.dotapick.ui.theme.DotaPickTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            DotaPickTheme {
                DotaPickApp()
            }
        }
    }
}