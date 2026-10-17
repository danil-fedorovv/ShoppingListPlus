package danilfedorov.feature_settings.internal.presenter.mvi

import androidx.compose.runtime.Immutable

@Immutable
data class ScreenState(
    val darkTheme: Boolean = false,
    val colorIndex: Int = 0,
    val isChangeColorSheetVisible: Boolean = false
)
