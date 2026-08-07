// core/device/AppPreferences.kt
package bsb.dev.bsb_bangking_jp.core.device

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AppPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isInitSuccess(): Boolean = prefs.getBoolean(KEY_INIT_SUCCESS, false)
    fun saveInitSuccess(value: Boolean) = prefs.edit { putBoolean(KEY_INIT_SUCCESS, value) }

    fun getConfirmMpinStatus(): Boolean = prefs.getBoolean(KEY_CONFIRM_MPIN, false)
    fun saveConfirmMpinStatus(value: Boolean) = prefs.edit { putBoolean(KEY_CONFIRM_MPIN, value) }

    fun getRegistStatus(): Boolean = prefs.getBoolean(KEY_REGIST, false)
    fun saveRegistStatus(value: Boolean) = prefs.edit { putBoolean(KEY_REGIST, value) }

    fun isLoginAllowed(): Boolean = prefs.getBoolean(KEY_LOGIN_ALLOWED, false)
    fun saveLoginAllowed(value: Boolean) = prefs.edit { putBoolean(KEY_LOGIN_ALLOWED, value) }

    // 🔹 "Ingat ID Pengguna" -- cuma userid, bukan passcode, jadi aman di plain prefs
    fun isRememberMeEnabled(): Boolean = prefs.getBoolean(KEY_REMEMBER_ME, false)
    fun getRememberedUserId(): String? = prefs.getString(KEY_REMEMBERED_USERID, null)

    fun saveRememberedLogin(userId: String) {
        prefs.edit {
            putBoolean(KEY_REMEMBER_ME, true)
            putString(KEY_REMEMBERED_USERID, userId)
        }
    }

    fun clearRememberedLogin() {
        prefs.edit {
            putBoolean(KEY_REMEMBER_ME, false)
            remove(KEY_REMEMBERED_USERID)
        }
    }

    companion object {
        private const val PREFS_NAME = "app_prefs"
        private const val KEY_INIT_SUCCESS = "init_success"
        private const val KEY_CONFIRM_MPIN = "confirm_mpin_status"
        private const val KEY_REGIST = "regist_status"
        private const val KEY_LOGIN_ALLOWED = "login_allowed"
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_REMEMBERED_USERID = "remembered_userid"
    }
}