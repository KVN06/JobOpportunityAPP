package com.kvn.jobopportunityapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// Clase para manejar las preferencias de la aplicación
class AppPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "job_opportunity_prefs", 
        Context.MODE_PRIVATE
    )
    
    // Estados de configuración (sin setters privados para evitar conflictos)
    var isDarkTheme by mutableStateOf(prefs.getBoolean("dark_theme", false))
    var notificationsEnabled by mutableStateOf(prefs.getBoolean("notifications", true))
    var emailNotifications by mutableStateOf(prefs.getBoolean("email_notifications", true))
    var pushNotifications by mutableStateOf(prefs.getBoolean("push_notifications", true))
    var jobAlerts by mutableStateOf(prefs.getBoolean("job_alerts", true))
    var messageNotifications by mutableStateOf(prefs.getBoolean("message_notifications", true))
    var autoSync by mutableStateOf(prefs.getBoolean("auto_sync", true))
    var dataUsage by mutableStateOf(prefs.getString("data_usage", "wifi_only") ?: "wifi_only")
    var language by mutableStateOf(prefs.getString("language", "es") ?: "es")
    // Perfil
    var avatarUri by mutableStateOf(prefs.getString("avatar_uri", "") ?: "")
    // Demo mode
    var demoMode by mutableStateOf(prefs.getBoolean("demo_mode", false))
    
    // Estados de autenticación
    var isLoggedIn by mutableStateOf(prefs.getBoolean("is_logged_in", false))
    var userType by mutableStateOf(
        try {
            UserType.valueOf(prefs.getString("user_type", UserType.CESANTE.name) ?: UserType.CESANTE.name)
        } catch (e: IllegalArgumentException) {
            UserType.CESANTE
        }
    )
    var userEmail by mutableStateOf(prefs.getString("user_email", "") ?: "")
    var userName by mutableStateOf(prefs.getString("user_name", "") ?: "")
    // Token de autenticación
    var authToken: String? = prefs.getString("auth_token", null)
    
    // Métodos para manejar la autenticación
    fun saveUserSession(userType: UserType, email: String, name: String) {
        isLoggedIn = true
        this.userType = userType
        this.userEmail = email
        this.userName = name
        
        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("user_type", userType.name)
            .putString("user_email", email)
            .putString("user_name", name)
            .apply()
    }

    fun saveAuthToken(token: String) {
        authToken = token
        prefs.edit().putString("auth_token", token).apply()
    }
    
    fun clearUserSession() {
        isLoggedIn = false
        this.userType = UserType.CESANTE
        this.userEmail = ""
        this.userName = ""
        authToken = null
        
        prefs.edit()
            .putBoolean("is_logged_in", false)
            .putString("user_type", UserType.CESANTE.name)
            .putString("user_email", "")
            .putString("user_name", "")
            .remove("auth_token")
            .apply()
    }
    
    // Métodos para actualizar configuraciones
    fun updateDarkTheme(enabled: Boolean) {
        isDarkTheme = enabled
        prefs.edit().putBoolean("dark_theme", enabled).apply()
    }
    
    fun updateNotifications(enabled: Boolean) {
        notificationsEnabled = enabled
        prefs.edit().putBoolean("notifications", enabled).apply()
    }
    
    fun updateEmailNotifications(enabled: Boolean) {
        emailNotifications = enabled
        prefs.edit().putBoolean("email_notifications", enabled).apply()
    }
    
    fun updatePushNotifications(enabled: Boolean) {
        pushNotifications = enabled
        prefs.edit().putBoolean("push_notifications", enabled).apply()
    }
    
    fun updateJobAlerts(enabled: Boolean) {
        jobAlerts = enabled
        prefs.edit().putBoolean("job_alerts", enabled).apply()
    }
    
    fun updateMessageNotifications(enabled: Boolean) {
        messageNotifications = enabled
        prefs.edit().putBoolean("message_notifications", enabled).apply()
    }
    
    fun updateAutoSync(enabled: Boolean) {
        autoSync = enabled
        prefs.edit().putBoolean("auto_sync", enabled).apply()
    }
    
    fun updateDataUsage(usage: String) {
        dataUsage = usage
        prefs.edit().putString("data_usage", usage).apply()
    }
    
    fun updateLanguage(lang: String) {
        language = lang
        prefs.edit().putString("language", lang).apply()
    }

    fun updateAvatarUri(uri: String?) {
        avatarUri = uri ?: ""
        prefs.edit().putString("avatar_uri", avatarUri).apply()
    }

    fun updateDemoMode(enabled: Boolean) {
        demoMode = enabled
        prefs.edit().putBoolean("demo_mode", enabled).apply()
    }
}

// Opciones de uso de datos
enum class DataUsageOption(val key: String, val displayName: String) {
    WIFI_ONLY("wifi_only", "Solo WiFi"),
    MOBILE_AND_WIFI("mobile_and_wifi", "Móvil y WiFi"),
    REDUCED_DATA("reduced_data", "Datos reducidos")
}

// Opciones de idioma
enum class LanguageOption(val key: String, val displayName: String) {
    SPANISH("es", "Español"),
    ENGLISH("en", "English"),
    PORTUGUESE("pt", "Português")
}
