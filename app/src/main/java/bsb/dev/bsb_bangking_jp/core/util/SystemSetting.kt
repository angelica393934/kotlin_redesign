package bsb.dev.bsb_bangking_jp.core.util

import android.content.Context
import android.content.res.Configuration

fun isSystemDarkTheme(
    context: Context
): Boolean {

    return when (
        context.resources.configuration.uiMode
                and Configuration.UI_MODE_NIGHT_MASK
    ) {

        Configuration.UI_MODE_NIGHT_YES -> true

        else -> false

    }

}

fun getSystemLanguage(
    context: Context
): String {

    return context.resources.configuration.locales[0]
        .language

}