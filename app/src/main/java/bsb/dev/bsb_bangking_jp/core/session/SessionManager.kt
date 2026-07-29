package bsb.dev.bsb_bangking_jp.core.session

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Penyimpanan sederhana untuk accessToken & data user setelah login berhasil.
 * Wajib dipanggil `SessionManager.init(context)` sekali di Application/MainActivity
 * sebelum dipakai (misalnya di Application.onCreate()).
 *
 * TODO: untuk produksi, pertimbangkan migrasi ke DataStore atau
 * EncryptedSharedPreferences supaya token tidak tersimpan plain text.
 */
object SessionManager {

    private const val PREF_NAME = "bsb_session"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_USERNAME = "username"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.applicationContext
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    fun saveSession(accessToken: String, username: String) {
        prefs.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_USERNAME, username)
        }
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)

    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    fun isLoggedIn(): Boolean = !getAccessToken().isNullOrEmpty()

    fun clearSession() {
        prefs.edit { clear() }
    }
}
