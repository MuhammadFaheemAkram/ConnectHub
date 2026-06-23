package co.bitfuse.connecthub.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.connectHubDataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(
    name = DataStorePlaceholder.preferencesName,
)
