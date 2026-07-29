package bsb.dev.bsb_bangking_jp.core.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import bsb.dev.bsb_bangking_jp.data.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferenceRepository(
    context: Context
) {

    private val dataStore = context.dataStore

    companion object {

        val DARK_THEME =
            booleanPreferencesKey("dark_theme")

        val LANGUAGE =
            stringPreferencesKey("language")

    }

    val darkTheme: Flow<Boolean?> =
        dataStore.data.map {

            it[DARK_THEME]

        }

    val language: Flow<String?> =
        dataStore.data.map {

            it[LANGUAGE]

        }

    suspend fun saveTheme(
        isDark: Boolean
    ) {

        dataStore.edit {

            it[DARK_THEME] = isDark

        }

    }

    suspend fun saveLanguage(
        language: String
    ) {

        dataStore.edit {

            it[LANGUAGE] = language

        }

    }

}