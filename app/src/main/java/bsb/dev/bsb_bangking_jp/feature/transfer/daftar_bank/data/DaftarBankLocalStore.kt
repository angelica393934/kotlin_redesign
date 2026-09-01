// feature/transfer/data/DaftarBankLocalStore.kt
package bsb.dev.bsb_bangking_jp.feature.transfer.daftar_bank.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import bsb.dev.bsb_bangking_jp.data.datastore.dataStore
import bsb.dev.bsb_bangking_jp.feature.transfer.daftar_bank.domain.BankItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

/**
 * Cache lokal daftar bank -- persist ke DataStore (bertahan lintas restart app).
 * Padanan `DaftarBankCache` (Hive) di Flutter, TTL 24 jam sama seperti versi Dart.
 */
class DaftarBankLocalStore(private val context: Context) {

    private val gson = Gson()
    private val listType = object : TypeToken<List<BankItem>>() {}.type

    companion object {
        private val KEY_DATA = stringPreferencesKey("daftar_bank_data")
        private val KEY_LAST_FETCH = longPreferencesKey("daftar_bank_last_fetch")
        private val TTL_MILLIS = 24 * 60 * 60 * 1000L
    }

    suspend fun get(): List<BankItem>? {
        val raw = context.dataStore.data.first()[KEY_DATA] ?: return null
        return try {
            gson.fromJson<List<BankItem>>(raw, listType)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getLastFetchTime(): Long? =
        context.dataStore.data.first()[KEY_LAST_FETCH]

    suspend fun save(banks: List<BankItem>) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DATA] = gson.toJson(banks)
            prefs[KEY_LAST_FETCH] = System.currentTimeMillis()
        }
    }

    fun isExpired(lastFetchTime: Long?): Boolean {
        if (lastFetchTime == null) return true
        return System.currentTimeMillis() - lastFetchTime > TTL_MILLIS
    }
}