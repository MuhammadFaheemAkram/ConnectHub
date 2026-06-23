package co.bitfuse.connecthub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.bitfuse.connecthub.core.designsystem.theme.ConnectHubTheme
import co.bitfuse.connecthub.core.navigation.ConnectHubNavHost
import co.bitfuse.connecthub.feature.settings.AppSettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val appSettingsViewModel: AppSettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by appSettingsViewModel.settings.collectAsStateWithLifecycle()
            ConnectHubTheme(
                darkTheme = settings.darkMode,
                dynamicColor = settings.dynamicColor,
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ConnectHubNavHost()
                }
            }
        }
    }
}
