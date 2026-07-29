package bsb.dev.bsb_bangking_jp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import bsb.dev.bsb_bangking_jp.core.navigation.AppNavigation
import bsb.dev.bsb_bangking_jp.core.theme.BSBBangkingJPTheme
import bsb.dev.bsb_bangking_jp.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val settings by settingsViewModel.settings.collectAsState()

            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            SideEffect {
                insetsController.isAppearanceLightStatusBars = !settings.darkTheme
                window.navigationBarColor = if (settings.darkTheme) Color.BLACK else Color.WHITE
                insetsController.isAppearanceLightNavigationBars = !settings.darkTheme
            }

            BSBBangkingJPTheme(
                darkTheme = settings.darkTheme
            ) {
                Surface(
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.systemBars.only(WindowInsetsSides.Bottom)
                    ),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        darkTheme = settings.darkTheme,
                        onThemeChange = { isDark ->
                            settingsViewModel.saveTheme(isDark)
                        }
                    )
                }
            }
        }
    }
}