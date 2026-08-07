package bsb.dev.bsb_bangking_jp.core.device

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorageService(context: Context) {

    private val appContext = context.applicationContext

    private val prefs by lazy {
        val masterKey = MasterKey.Builder(appContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            appContext,
            "secure_device_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    fun getDeviceId(): String? = prefs.getString(KEY_DEVICE_ID, null)
    fun saveDeviceId(id: String) = prefs.edit { putString(KEY_DEVICE_ID, id) }

    fun getPrivateKey(): String? = prefs.getString(KEY_PRIVATE_KEY, null)
    fun savePrivateKey(base64Key: String) = prefs.edit { putString(KEY_PRIVATE_KEY, base64Key) }

    fun getInitAccessToken(): String? = prefs.getString(KEY_INIT_ACCESS_TOKEN, null)
    fun saveInitAccessToken(token: String) = prefs.edit { putString(KEY_INIT_ACCESS_TOKEN, token) }

    fun getInitRefreshToken(): String? = prefs.getString(KEY_INIT_REFRESH_TOKEN, null)
    fun saveInitRefreshToken(token: String) = prefs.edit { putString(KEY_INIT_REFRESH_TOKEN, token) }

    fun clearInitTokens() {
        prefs.edit {
            remove(KEY_INIT_ACCESS_TOKEN)
            remove(KEY_INIT_REFRESH_TOKEN)
        }
    }

    companion object {
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_PRIVATE_KEY = "ed25519_private_key"
        private const val KEY_INIT_ACCESS_TOKEN = "init_access_token"
        private const val KEY_INIT_REFRESH_TOKEN = "init_refresh_token"
    }
}