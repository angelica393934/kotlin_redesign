package bsb.dev.bsb_bangking_jp.core.session

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import bsb.dev.bsb_bangking_jp.core.network.dto.LoginResponse
import bsb.dev.bsb_bangking_jp.core.network.dto.MeResponse

/**
 * Data profil user yang sudah login, diambil dari SessionManager.
 * Dipakai Dashboard/halaman lain supaya tidak perlu hit API ulang
 * hanya untuk menampilkan nama/email/foto.
 */
data class UserProfile(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
)

/**
 * Penyimpanan sederhana untuk accessToken & data user setelah login berhasil.
 * Wajib dipanggil `SessionManager.init(context)` sekali di Application.onCreate()
 * sebelum dipakai.
 *
 * TODO: untuk produksi, pertimbangkan migrasi ke DataStore atau
 * EncryptedSharedPreferences supaya token tidak tersimpan plain text.
 */
object SessionManager {

    private const val PREF_NAME = "bsb_session"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"
    private const val KEY_ID = "id"
    private const val KEY_USERNAME = "username"
    private const val KEY_EMAIL = "email"
    private const val KEY_FIRST_NAME = "first_name"
    private const val KEY_LAST_NAME = "last_name"
    private const val KEY_GENDER = "gender"
    private const val KEY_IMAGE = "image"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.applicationContext
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    /** Simpan seluruh response login (token + profil) sekaligus. */
    fun saveSession(response: LoginResponse) {
        prefs.edit {
            putString(KEY_ACCESS_TOKEN, response.accessToken)
            putString(KEY_REFRESH_TOKEN, response.refreshToken)
            putInt(KEY_ID, response.id)
            putString(KEY_USERNAME, response.username)
            putString(KEY_EMAIL, response.email)
            putString(KEY_FIRST_NAME, response.firstName)
            putString(KEY_LAST_NAME, response.lastName)
            putString(KEY_GENDER, response.gender)
            putString(KEY_IMAGE, response.image)
        }
    }

    /**
     * Perbarui data profil SAJA dari hasil GET /auth/me (dipakai
     * pull-to-refresh), token TIDAK disentuh sama sekali.
     */
    fun updateProfile(me: MeResponse) {
        prefs.edit {
            putInt(KEY_ID, me.id)
            putString(KEY_USERNAME, me.username)
            putString(KEY_EMAIL, me.email)
            putString(KEY_FIRST_NAME, me.firstName)
            putString(KEY_LAST_NAME, me.lastName)
            putString(KEY_GENDER, me.gender)
            putString(KEY_IMAGE, me.image)
        }
    }

    /**
     * Perbarui accessToken & refreshToken SAJA dari hasil POST /auth/refresh,
     * data profil TIDAK disentuh sama sekali.
     */
    fun updateTokens(accessToken: String, refreshToken: String) {
        prefs.edit {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
        }
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    fun getUserProfile(): UserProfile? {
        val username = prefs.getString(KEY_USERNAME, null) ?: return null
        return UserProfile(
            id = prefs.getInt(KEY_ID, 0),
            username = username,
            email = prefs.getString(KEY_EMAIL, "") ?: "",
            firstName = prefs.getString(KEY_FIRST_NAME, "") ?: "",
            lastName = prefs.getString(KEY_LAST_NAME, "") ?: "",
            gender = prefs.getString(KEY_GENDER, "") ?: "",
            image = prefs.getString(KEY_IMAGE, "") ?: "",
        )
    }

    fun isLoggedIn(): Boolean = !getAccessToken().isNullOrEmpty()

    fun clearSession() {
        prefs.edit { clear() }
    }
}
