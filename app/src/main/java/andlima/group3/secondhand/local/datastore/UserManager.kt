package andlima.group3.secondhand.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
        val FIRST_TIME = booleanPreferencesKey("first_time")
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

    val firstTimeFlow: Flow<Boolean> = context.dataStore.data.map {
        it[FIRST_TIME] ?: true
    }

    /**
     * To save new access token
     */
    suspend fun saveAccessToken(accessToken: String) {
        itContext.dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
        }
    }

    /**
     * To save new user data after login
     */
    suspend fun saveUserData(username: String, email: String, password: String) {
        itContext.dataStore.edit {
            it[USERNAME] = username
            it[EMAIL] = email
            it[PASSWORD] = password
        }
    }

    /**
     * To clear old access token
     */
    suspend fun clearOldAccessTokenPreferences() {
        itContext.dataStore.edit {
            it[ACCESS_TOKEN] = ""
        }
    }

    /**
     * To clear user data when logout
     */
    suspend fun clearDataPreferences() {
        itContext.dataStore.edit {
            it[USERNAME] = ""
            it[EMAIL] = ""
            it[PASSWORD] = ""
            it[ACCESS_TOKEN] = ""
//            it.clear()
        }
    }

    /**
     * To set app is not on first time run
     */
    suspend fun setNotFirstTimeRun() {
        itContext.dataStore.edit {
            it[FIRST_TIME] = false
        }
    }
}