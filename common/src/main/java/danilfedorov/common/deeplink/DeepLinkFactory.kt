package danilfedorov.common.deeplink


object DeepLinkFactory {

    private const val BASE_URL = "https://shoppinglistplus.ru"

    private const val PATH_LIVE_EDIT = "live_edit"
    private const val PATH_LONG_SHARE = "share"
    private const val PATH_SHORT_SHARE = "share_short"

    fun createLink(type: DeepLink): String {
        val path = when (type) {
            is DeepLink.LiveEdit -> "$PATH_LIVE_EDIT/${type.suffix}"
            is DeepLink.Share -> "$PATH_LONG_SHARE/${type.suffix}"
        }
        return "$BASE_URL/$path"
    }

    fun getUriPattern(type: DeepLink): String {
        val path = when (type) {
            is DeepLink.LiveEdit -> "$PATH_LIVE_EDIT/{remoteId}"
            is DeepLink.Share -> "$PATH_LONG_SHARE/{encodedData}"
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
            PATH_LONG_SHARE -> DeepLink.Share(suffix)
            else -> null
        }
    }
}