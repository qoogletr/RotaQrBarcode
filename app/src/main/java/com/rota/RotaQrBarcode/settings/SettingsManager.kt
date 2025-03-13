import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object SettingsManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private object PreferencesKeys {
        val SERVER_IP = stringPreferencesKey("server_ip")
        val SERVER_PORT = intPreferencesKey("server_port")
        val AUTO_SCAN = booleanPreferencesKey("auto_scan")
        val SCANNER_LIBRARY = stringPreferencesKey("scanner_library")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val BEEP_ENABLED = booleanPreferencesKey("beep_enabled")
    }

    suspend fun setServerIp(ip: String) {
        appContext.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SERVER_IP] = ip
        }
    }

    suspend fun getServerIp(): String {
        return appContext.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.SERVER_IP] ?: "192.168.1.1"
        }.first()
    }

    suspend fun setServerPort(port: Int) {
        appContext.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SERVER_PORT] = port
        }
    }

    suspend fun getServerPort(): Int {
        return appContext.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.SERVER_PORT] ?: 8080
        }.first()
    }

    suspend fun setAutoScanEnabled(enabled: Boolean) {
        appContext.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTO_SCAN] = enabled
        }
    }

    suspend fun getAutoScanEnabled(): Boolean {
        return appContext.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.AUTO_SCAN] ?: true
        }.first()
    }

    suspend fun setScannerLibrary(library: ScannerLibrary) {
        appContext.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SCANNER_LIBRARY] = library.name
        }
    }

    suspend fun getScannerLibrary(): ScannerLibrary {
        return appContext.dataStore.data.map { preferences ->
            ScannerLibrary.valueOf(preferences[PreferencesKeys.SCANNER_LIBRARY] ?: ScannerLibrary.MLKIT.name)
        }.first()
    }
}

enum class ScannerLibrary {
    MLKIT,
    ZXING
}