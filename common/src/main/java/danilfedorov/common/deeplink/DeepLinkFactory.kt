package danilfedorov.common.deeplink


object DeepLinkFactory {

    private const val BASE_URL = "https://shoppinglistapp.ru"

    private const val PATH_LIVE_EDIT = "live_edit"

    fun createLink(type: DeepLink): String {
        val path = when (type) {
            is DeepLink.LiveEdit -> "$PATH_LIVE_EDIT/${type.suffix}"
        }
        return "$BASE_URL/$path"
    }

    fun getUriPattern(type: DeepLink): String {
        val path = when (type) {
            is DeepLink.LiveEdit -> "$PATH_LIVE_EDIT/{remoteId}"
        }
        return "$BASE_URL/$path"
    }

    fun parseLink(link: String): DeepLink? {
        val clean = link.removePrefix("$BASE_URL/")
        val parts = clean.split("/")

        if (parts.size < 2) return null

        val path = parts[0]
        val suffix = parts[1]

        return when (path) {
            PATH_LIVE_EDIT -> DeepLink.LiveEdit(suffix)
            else -> null
        }
    }
}