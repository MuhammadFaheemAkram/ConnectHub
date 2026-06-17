package com.example.connecthub.core.common.util

import kotlin.math.max

object TimeAgoFormatter {
    fun format(nowMillis: Long, createdAtMillis: Long): String {
        val minutes = max(1, (nowMillis - createdAtMillis) / 60_000)
        return when {
            minutes < 60 -> "${minutes}m ago"
            minutes < 1_440 -> "${minutes / 60}h ago"
            else -> "${minutes / 1_440}d ago"
        }
    }
}
