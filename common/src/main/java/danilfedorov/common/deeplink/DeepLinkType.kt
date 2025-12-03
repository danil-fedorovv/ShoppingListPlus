package danilfedorov.common.deeplink

sealed class DeepLink(open val suffix: String) {
    data class LiveEdit(override val suffix: String) : DeepLink(suffix)
    data class Share(override val suffix: String) : DeepLink(suffix)
}