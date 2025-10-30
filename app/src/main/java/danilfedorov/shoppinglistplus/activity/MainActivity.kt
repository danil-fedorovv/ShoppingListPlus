package danilfedorov.shoppinglistplus.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import danilfedorov.common.core_res.colors
import danilfedorov.shoppinglistplus.ShoppingListPlusApplication
import danilfedorov.shoppinglistplus.ad.YandexStickyBanner
import danilfedorov.shoppinglistplus.nav.ShoppingListAppNavigation
import danilfedorov.shoppinglistplus.theme.ShoppingListTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val appComponent by lazy {
        (applicationContext as ShoppingListPlusApplication).appComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appearanceRepository = appComponent.appearanceRepository

        enableEdgeToEdge()
        lifecycleScope.launch {
            val initialDarkTheme = appearanceRepository.themeFlow.first()
            val initialColorIndex = appearanceRepository.colorIndexFlow.first()
            setContent {
                val isSystemInDarkTheme = isSystemInDarkTheme()
                val darkTheme by appearanceRepository.themeFlow.collectAsState(initial = initialDarkTheme)
                LaunchedEffect(darkTheme) {
                    if (darkTheme == null) {
                        appearanceRepository.setTheme(isSystemInDarkTheme)
                    }
                }
                val colorIndex by appearanceRepository.colorIndexFlow.collectAsState(initial = initialColorIndex)

                ShoppingListTheme(
                    isDarkTheme = darkTheme ?: isSystemInDarkTheme,
                    primaryColor = colors[colorIndex]
                ) {
                    Column(Modifier.background(colorScheme.background)) {
                        ShoppingListAppNavigation(modifier = Modifier.weight(1f))
                        YandexStickyBanner(
                            isDarkTheme = darkTheme ?: isSystemInDarkTheme
                        )
                    }
                }
            }
        }
    }
}
