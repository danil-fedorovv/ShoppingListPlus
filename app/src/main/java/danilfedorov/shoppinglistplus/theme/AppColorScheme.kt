package danilfedorov.shoppinglistplus.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import danilfedorov.common.core_res.TextWhite
import danilfedorov.common.core_res.background
import danilfedorov.common.core_res.backgroundWhite
import danilfedorov.common.core_res.lime
import danilfedorov.common.core_res.onBackground
import danilfedorov.common.core_res.onBackgroundWhite
import danilfedorov.common.core_res.onSurface
import danilfedorov.common.core_res.onSurfaceWhite
import danilfedorov.common.core_res.outline
import danilfedorov.common.core_res.outlineWhite
import danilfedorov.common.core_res.red
import danilfedorov.common.core_res.secondaryWhite
import danilfedorov.common.core_res.surface
import danilfedorov.common.core_res.surfaceWhite

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
val LightColorScheme = lightColorScheme(
    background = backgroundWhite,
    onBackground = onBackgroundWhite,
    surface = surfaceWhite,
    onSurface = onSurfaceWhite,
    outline = outlineWhite,
    secondary = secondaryWhite,
    error = red,
    primary = lime
)
