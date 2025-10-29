package danilfedorov.feature_archive.internal.util

import android.content.Context
import io.avdev.archive.R

fun getDeletedTimeText(context: Context, deletedAt: Long?): String {
    if (deletedAt == null) return ""

    val now = System.currentTimeMillis()
    val diff = now - deletedAt
    val daysAgo = (diff / (1000 * 60 * 60 * 24)).toInt()

    return when (daysAgo) {
        0 -> context.getString(R.string.deleted_today)
        1 -> context.getString(R.string.deleted_yesterday)
        else -> context.resources.getQuantityString(R.plurals.deleted_days_ago, daysAgo, daysAgo)
    }
}