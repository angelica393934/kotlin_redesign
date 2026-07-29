package bsb.dev.bsb_bangking_jp.core.util

fun String.InitialName(): String {
    val parts = trim()
        .split("\\s+".toRegex())
        .filter { it.isNotBlank() }

    return when {
        parts.isEmpty() -> ""
        parts.size == 1 -> parts.first().take(1).uppercase()
        else -> (parts[0].take(1) + parts[1].take(1)).uppercase()
    }
}