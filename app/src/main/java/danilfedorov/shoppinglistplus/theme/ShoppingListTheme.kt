package danilfedorov.shoppinglistplus.theme

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun ShoppingListTheme(
    isDarkTheme: Boolean,
    primaryColor: Color,
    content: @Composable () -> Unit
) {
    val targetColors = if (isDarkTheme) {
        DarkColorScheme.copy(primary = primaryColor)
    } else {
        LightColorScheme.copy(primary = primaryColor)
    }

    val animationDuration = 250

    val primary by animateColorAsState(
        targetValue = targetColors.primary,
        animationSpec = tween(durationMillis = animationDuration)
    )
    val background by animateColorAsState(
        targetValue = targetColors.background,
        animationSpec = tween(durationMillis = animationDuration)
    )
    val surface by animateColorAsState(
        targetValue = targetColors.surface,
        animationSpec = tween(durationMillis = animationDuration)
    )
    val onPrimary by animateColorAsState(
        targetValue = targetColors.onPrimary,
        animationSpec = tween(durationMillis = animationDuration)
    )
    val onBackground by animateColorAsState(
        targetValue = targetColors.onBackground,
        animationSpec = tween(durationMillis = animationDuration)
    )

    val animatedColorScheme = targetColors.copy(
        primary = primary,
        background = background,
        surface = surface,
        onPrimary = onPrimary,
        onBackground = onBackground
    )

    val view = LocalView.current
    val activity = view.context as? Activity
    val useDarkIcons = !isDarkTheme

    activity?.window?.let { window ->
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = animatedColorScheme.background.toArgb()

        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = useDarkIcons
            isAppearanceLightNavigationBars = useDarkIcons
        }
    }

    MaterialTheme(
        colorScheme = animatedColorScheme,
        typography = AppTypography,
        content = content
    )
}