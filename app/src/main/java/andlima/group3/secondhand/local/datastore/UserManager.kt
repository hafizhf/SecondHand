package andlima.group3.secondhand.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context: Context) {
    private val itContext = context

    companion object {
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "userpref")

        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")
    }

    val accessTokenFlow : Flow<String> = context.dataStore.data.map {
        it[ACCESS_TOKEN] ?: ""
    }

    val usernameFlow : Flow<String> = context.dataStore.data.map {
        it[USERNAME] ?: ""
    }

    val emailFlow : Flow<String> = context.dataStore.data.map {
        it[EMAIL] ?: ""
    }

    val passwordFlow : Flow<String> = context.dataStore.data.map {
        it[PASSWORD] ?: ""
    }

    suspend fun saveAccessToken(accessToken: String) {
        itContext.dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun saveUserData(username: String, email: String, password: String) {
        itContext.dataStore.edit {
            it[USERNAME] = username
            it[EMAIL] = email
            it[PASSWORD] = password
        }
    }

    suspend fun clearOldAccessTokenPreferences() {
        itContext.dataStore.edit {
            it[ACCESS_TOKEN] = ""
        }
    }

    suspend fun clearDataPreferences() {
        itContext.dataStore.edit {
            it.clear()
        }
    }
}