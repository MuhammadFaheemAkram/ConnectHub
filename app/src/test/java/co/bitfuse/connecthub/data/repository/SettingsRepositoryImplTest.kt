package co.bitfuse.connecthub.data.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import co.bitfuse.connecthub.core.datastore.SettingsLocalDataSource
import co.bitfuse.connecthub.core.testing.MainDispatcherRule
import co.bitfuse.connecthub.core.testing.TestDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun `settings updates persist through DataStore`() = runTest {
        val dataStoreFile = temporaryFolder.newFolder("settings").resolve("settings.preferences_pb")
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { dataStoreFile },
        )
        val repository = SettingsRepositoryImpl(
            settingsLocalDataSource = SettingsLocalDataSource(dataStore),
            dispatchers = TestDispatcherProvider(mainDispatcherRule.testDispatcher),
        )

        assertFalse(repository.observeSettings().first().darkMode)

        repository.updateTheme(true)
        repository.updateDynamicColor(true)
        repository.updateNotificationSetting(false)
        repository.updateLanguage("Spanish")

        val settings = repository.observeSettings().first { it.language == "Spanish" }
        assertTrue(settings.darkMode)
        assertTrue(settings.dynamicColor)
        assertFalse(settings.notificationsEnabled)
        assertEquals("Spanish", settings.language)
    }
}
