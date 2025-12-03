package danilfedorov.feature_fruit_album.internal.mvi

data class ScreenState(
    val fruitAlbum: Map<Int, Pair<Boolean, Boolean>> = emptyMap(),
    val unlockedIndex: Int? = null,

)
