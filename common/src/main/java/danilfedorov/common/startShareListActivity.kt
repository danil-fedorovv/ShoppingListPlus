package danilfedorov.common

import android.content.Context
import android.content.Intent

fun shareListUtil(context: Context, list: List<String>) {
    val itemList = generateShoppingListText(list)

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, itemList)
        type = "text/plain"
    }

    val shareIntent =
        Intent.createChooser(sendIntent, context.getString(R.string.share_shopping_list))
    context.startActivity(shareIntent)
}

private fun generateShoppingListText(list: List<String>): String {
    return buildString {
        list.forEachIndexed { index, item ->
            append("${index + 1}. $item\n")
        }
    }
}