package danilfedorov.common.core_res

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

val DarkColorScheme = darkColorScheme(
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    outline = outline,
    secondary = TextWhite,
    error = red,
    primary = lime
)


@Composable
fun ShoppingListThemePreview(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
