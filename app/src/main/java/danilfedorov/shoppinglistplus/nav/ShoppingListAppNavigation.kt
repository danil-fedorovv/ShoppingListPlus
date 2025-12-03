package danilfedorov.shoppinglistplus.nav

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import danilfedorov.common.deeplink.DeepLink
import danilfedorov.feature_archive.api.ArchiveScreenEntry
import danilfedorov.feature_fruit_album.api.FruitAlbumScreenEntry
import danilfedorov.feature_premium.api.PremiumScreenEntry
import danilfedorov.feature_productlist.api.ProductListScreenEntry
import danilfedorov.feature_settings.api.SettingsScreenEntry
import danilfedorov.feature_shoppinglist.api.ShoppingListScreenEntry
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicBoolean


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShoppingListAppNavigation(
    navController: NavHostController,
    initialDeepLink: DeepLink? = null,
    nullDeepLink: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val fadeInTransition = fadeIn(animationSpec = tween(300))
    val fadeOutTransition = fadeOut(animationSpec = tween(300))

    NavHost(
        navController = navController,
        startDestination = Screen.Shoppinglist,
        modifier = modifier.background(colorScheme.background)
    ) {
        composable<Screen.Shoppinglist>(
            deepLinks = listOf(
                navDeepLink { uriPattern = "https://shoppinglistplus.ru/live_edit/{remoteId}" },
                navDeepLink { uriPattern = "https://shoppinglistplus.ru/share_long/{encodedData}" },
                navDeepLink { uriPattern = "https://shoppinglistplus.ru/share_short/{id}" }
            )
        ) {
            ShoppingListScreenEntry(
                deepLink = initialDeepLink,
                nullDeepLink = nullDeepLink,
                navArchive = { navController.navigate(Screen.Archive) },
                navSettings = { navController.navigate(Screen.Settings) },
                navSubscription = { navController.navigate(Screen.Subscription) },
                navFruitAlbum = { navController.navigate(Screen.FruitAlbum) },
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
                    navController.safePopBackStack()
                }
            )
        }

        composable<Screen.Settings>(
            enterTransition = { fadeInTransition },
            exitTransition = { fadeOutTransition },
            popEnterTransition = { fadeInTransition },
            popExitTransition = { fadeOutTransition }
        ) {
            SettingsScreenEntry(
                navBack = {
                    navController.safePopBackStack()
                },
                navFruitCollection = {
                    navController.navigate(Screen.FruitAlbum)
                },
                navPremium = {
                    navController.navigate(Screen.Subscription)
                }

            )
        }

        composable<Screen.Archive>(
            enterTransition = { fadeInTransition },
            exitTransition = { fadeOutTransition },
            popEnterTransition = { fadeInTransition },
            popExitTransition = { fadeOutTransition }
        ) {
            ArchiveScreenEntry {
                navController.safePopBackStack()
            }
        }
        composable<Screen.Subscription>(
            enterTransition = { fadeInTransition },
            exitTransition = { fadeOutTransition },
            popEnterTransition = { fadeInTransition },
            popExitTransition = { fadeOutTransition }
        ) {
            PremiumScreenEntry {
                navController.safePopBackStack()
            }
        }

        composable<Screen.FruitAlbum>(
            enterTransition = { fadeInTransition },
            exitTransition = { fadeOutTransition },
            popEnterTransition = { fadeInTransition },
            popExitTransition = { fadeOutTransition }
        ) {
            FruitAlbumScreenEntry {
                navController.safePopBackStack()
            }
        }
    }
}

private val poppingFlags = WeakHashMap<NavHostController, AtomicBoolean>()

fun NavHostController.safePopBackStack() {
    val flag = synchronized(poppingFlags) {
        poppingFlags.getOrPut(this) { AtomicBoolean(false) }
    }

    if (flag.getAndSet(true)) return

    try {
        if (currentBackStackEntry?.destination == Screen.Shoppinglist) return
        if (previousBackStackEntry == null) return

        popBackStack()
    } finally {
        Handler(Looper.getMainLooper()).post { flag.set(false) }
    }
}