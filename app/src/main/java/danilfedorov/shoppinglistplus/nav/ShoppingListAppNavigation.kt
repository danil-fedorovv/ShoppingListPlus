package danilfedorov.shoppinglistplus.nav

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import danilfedorov.feature_archive.api.ArchiveScreenEntry
import danilfedorov.feature_productlist.api.ProductListScreenEntry
import danilfedorov.feature_settings.api.SettingsScreenEntry
import danilfedorov.feature_shoppinglist.api.ShoppingListScreenEntry

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShoppingListAppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val fadeInTransition = fadeIn(animationSpec = tween(300))
    val fadeOutTransition = fadeOut(animationSpec = tween(300))

    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier.background(colorScheme.background)
    ) {
        composable<Screen.Home>(
            enterTransition = { fadeInTransition },
            exitTransition = { fadeOutTransition },
            popEnterTransition = { fadeInTransition },
            popExitTransition = { fadeOutTransition }
        ) {
            ShoppingListScreenEntry(
                navArchive = { navController.navigate(Screen.Archive) },
                navSettings = { navController.navigate(Screen.Settings) },
                navProduct = { shoppingListId ->
                    navController.navigate(
                        Screen.ProductList(
                            shoppingListId
                        )
                    )
                }
            )
        }

        composable<Screen.ProductList>(
            enterTransition = { fadeInTransition },
            exitTransition = { fadeOutTransition },
            popEnterTransition = { fadeInTransition },
            popExitTransition = { fadeOutTransition }
        ) {
            ProductListScreenEntry(
                id = it.arguments?.getLong("shoppingListId") ?: 0,
                onNavigateHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Home) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Settings>(
            enterTransition = { fadeInTransition },
            exitTransition = { fadeOutTransition },
            popEnterTransition = { fadeInTransition },
            popExitTransition = { fadeOutTransition }
        ) {
            SettingsScreenEntry {
                navController.navigate(Screen.Home) {
                    popUpTo(Screen.Home) { inclusive = true }
                }
            }
        }

        composable<Screen.Archive>(
            enterTransition = { fadeInTransition },
            exitTransition = { fadeOutTransition },
            popEnterTransition = { fadeInTransition },
            popExitTransition = { fadeOutTransition }
        ) {
            ArchiveScreenEntry {
                navController.navigate(Screen.Home) {
                    popUpTo(Screen.Home) { inclusive = true }
                }
            }
        }
    }
}