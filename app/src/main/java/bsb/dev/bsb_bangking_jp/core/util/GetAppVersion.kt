package bsb.dev.bsb_bangking_jp.core.util

import android.content.Context

fun getAppVersion(context: Context): String? {
    return context.packageManager
        .getPackageInfo(context.packageName, 0)
        .versionName
}