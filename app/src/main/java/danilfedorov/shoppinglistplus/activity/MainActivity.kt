package danilfedorov.shoppinglistplus.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import danilfedorov.common.core_res.colors
import danilfedorov.common.deeplink.DeepLink
import danilfedorov.shoppinglistplus.ShoppingListPlusApplication
import danilfedorov.shoppinglistplus.ad.YandexStickyBanner
import danilfedorov.shoppinglistplus.nav.Screen
import danilfedorov.shoppinglistplus.nav.ShoppingListAppNavigation
import danilfedorov.shoppinglistplus.theme.ShoppingListTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val appComponent by lazy { (application as ShoppingListPlusApplication).appComponent }

    private var initialDeepLink by mutableStateOf<DeepLink?>(null)

    private var navController: NavHostController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        processIncomingIntent(intent)

        lifecycleScope.launch {
            val initialDarkTheme = appComponent.appearanceRepository.themeFlow.first()
            val initialColorIndex = appComponent.appearanceRepository.colorIndexFlow.first()

            setContent {
                val controller = rememberNavController()
                LaunchedEffect(Unit) { navController = controller }

                val isSystemInDarkTheme = isSystemInDarkTheme()
                val darkTheme by appComponent.appearanceRepository.themeFlow.collectAsState(initial = initialDarkTheme)
                val colorIndex by appComponent.appearanceRepository.colorIndexFlow.collectAsState(
                    initial = initialColorIndex
                )

                LaunchedEffect(darkTheme) {
                    if (darkTheme == null) {
                        appComponent.appearanceRepository.setTheme(isSystemInDarkTheme)
                    }
                }

                ShoppingListTheme(
                    isDarkTheme = darkTheme ?: isSystemInDarkTheme,
                    primaryColor = colors[colorIndex]
                ) {
                    Column(
                        modifier = Modifier
                            .background(colorScheme.background)
                            .navigationBarsPadding()
                    ) {
                        ShoppingListAppNavigation(
                            navController = controller,
                            initialDeepLink = initialDeepLink,
                            nullDeepLink = { initialDeepLink = null },
                            modifier = Modifier.weight(1f)
                        )
                            YandexStickyBanner(
                                isDarkTheme = darkTheme ?: isSystemInDarkTheme
                            )

                    }
                }
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let {
            setIntent(it)
            processIncomingIntent(it)
        }
    }

    private fun processIncomingIntent(intent: Intent) {
        val uri = intent.data ?: return

        initialDeepLink = null

        val newDeepLink = when (uri.host) {
            "shoppinglistapp.ru", "shoppinglistplus.ru" -> {
                when (uri.pathSegments.firstOrNull()) {
                    "live_edit" -> uri.lastPathSegment?.takeIf { it.isNotBlank() }
                        ?.let { DeepLink.LiveEdit(it) }
                    else -> null
                }
            }

            else -> null
        } ?: return

        initialDeepLink = newDeepLink

        navController?.let { nc ->
            if (nc.currentDestination != Screen.ShoppingList) {
                nc.navigate(Screen.ShoppingList) {
                    popUpTo(nc.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        }
    }
}