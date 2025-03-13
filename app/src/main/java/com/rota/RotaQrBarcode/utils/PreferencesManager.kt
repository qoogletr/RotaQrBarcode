object PreferencesManager {
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    var serverAddress: String
        get() = preferences.getString(PREF_SERVER_ADDRESS, "192.168.1.104") ?: "192.168.1.104"
        set(value) = preferences.edit().putString(PREF_SERVER_ADDRESS, value).apply()

    var serverPort: Int
        get() = preferences.getInt(PREF_SERVER_PORT, 6363)
        set(value) = preferences.edit().putInt(PREF_SERVER_PORT, value).apply()

    var serverEndpoint: String
        get() = preferences.getString(PREF_SERVER_ENDPOINT, "") ?: ""
        set(value) = preferences.edit().putString(PREF_SERVER_ENDPOINT, value).apply()

    var selectedFormat: SendFormat
        get() = SendFormat.valueOf(
            preferences.getString(PREF_SEND_FORMAT, SendFormat.PLAIN_TEXT.name)
                ?: SendFormat.PLAIN_TEXT.name
        )
        set(value) = preferences.edit().putString(PREF_SEND_FORMAT, value.name).apply()

    var autoSendEnabled: Boolean
        get() = preferences.getBoolean(PREF_AUTO_SEND, true)
        set(value) = preferences.edit().putBoolean(PREF_AUTO_SEND, value).apply()

    private const val PREF_SERVER_ADDRESS = "server_address"
    private const val PREF_SERVER_PORT = "server_port"
    private const val PREF_SERVER_ENDPOINT = "server_endpoint"
    private const val PREF_SEND_FORMAT = "send_format"
    private const val PREF_AUTO_SEND = "auto_send"
}