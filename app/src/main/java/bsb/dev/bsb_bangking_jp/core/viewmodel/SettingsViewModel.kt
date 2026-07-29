package bsb.dev.bsb_bangking_jp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import bsb.dev.bsb_bangking_jp.core.data.datastore.AppPreferenceRepository
import bsb.dev.bsb_bangking_jp.core.data.model.AppSettings
import bsb.dev.bsb_bangking_jp.core.util.getSystemLanguage
import bsb.dev.bsb_bangking_jp.core.util.isSystemDarkTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        AppPreferenceRepository(application)

    private val _settings = MutableStateFlow(
        AppSettings(
            darkTheme = isSystemDarkTheme(application),
            language = getSystemLanguage(application)
        )
    )

    val settings: StateFlow<AppSettings> =
        _settings.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {

        viewModelScope.launch {

            combine(
                repository.darkTheme,
                repository.language
            ) { darkTheme, language ->

                AppSettings(
                    darkTheme = darkTheme
                        ?: isSystemDarkTheme(getApplication()),

                    language = language
                        ?: getSystemLanguage(getApplication())
                )

            }.collect {

                _settings.value = it

            }

        }

    }

    fun saveTheme(
        isDark: Boolean
    ) {

        viewModelScope.launch {

            repository.saveTheme(isDark)

        }

    }

    fun saveLanguage(
        language: String
    ) {
        viewModelScope.launch {
            repository.saveLanguage(language)
        }

    }

}