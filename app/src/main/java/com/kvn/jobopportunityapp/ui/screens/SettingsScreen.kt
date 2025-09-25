package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kvn.jobopportunityapp.data.AppPreferences
import com.kvn.jobopportunityapp.ui.theme.*


enum class DataUsageOption(val key: String, val displayName: String) {
    WIFI("wifi", "Solo WiFi"),
    MOBILE("mobile", "WiFi y Datos Móviles")
}

enum class LanguageOption(val key: String, val displayName: String) {
    ES("es", "Español"),
    EN("en", "English")
}

@Composable
fun SettingsScreen(appPreferences: AppPreferences, onBackClick: () -> Unit = {}, onDeleteAccount: () -> Unit = {}, onLogout: () -> Unit = {}) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header with gradient to match other screens
        Box(modifier = Modifier.fillMaxWidth().height(110.dp).background(brush = Brush.linearGradient(listOf(com.kvn.jobopportunityapp.ui.theme.GradientStart, com.kvn.jobopportunityapp.ui.theme.GradientEnd))).statusBarsPadding(), contentAlignment = Alignment.CenterStart) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
            IconButton(onClick = onBackClick, modifier = Modifier.size(44.dp)) { Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onPrimary) }
                    Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Ajustes", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onPrimary)
                }
            }

            var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }

            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                item { AppearanceSection(appPreferences) }
                item { NotificationsSection(appPreferences) }
                item { DataSection(appPreferences) }
                item { LanguageSection(appPreferences) }
                item { AccountSection(onDelete = { showDeleteConfirm = true }, onLogout = onLogout) }
                item { Spacer(modifier = Modifier.height(28.dp)) }
            }

            if (showDeleteConfirm) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirm = false },
                    title = { Text("Eliminar cuenta") },
                    text = { Text("¿Estás seguro que deseas eliminar tu cuenta? Esta acción es irreversible.") },
                    confirmButton = { TextButton(onClick = { showDeleteConfirm = false; onDeleteAccount() }) { Text("Eliminar", color = Error) } },
                    dismissButton = { TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancelar") } }
                )
            }
        }
    }
}

@Composable
private fun AppearanceSection(appPreferences: AppPreferences) {
    SettingsSection(title = "Apariencia") {
        SettingsSwitchItem(
            title = "Tema Oscuro",
            subtitle = "Usar modo oscuro para la interfaz",
            icon = Icons.Default.DarkMode,
            checked = appPreferences.isDarkTheme,
            onCheckedChange = { appPreferences.updateDarkTheme(it) }
        )
        HorizontalDivider(color = BorderColor)
        SettingsSwitchItem(
            title = "Modo Demo",
            subtitle = "Poblar la app con más datos de ejemplo",
            icon = Icons.Default.Info,
            checked = appPreferences.demoMode,
            onCheckedChange = { appPreferences.updateDemoMode(it) }
        )
    }
}

@Composable
private fun NotificationsSection(appPreferences: AppPreferences) {
    SettingsSection(title = "Notificaciones") {
        SettingsSwitchItem(
            title = "Notificaciones",
            subtitle = "Habilitar todas las notificaciones",
            icon = Icons.Default.Notifications,
            checked = appPreferences.notificationsEnabled,
            onCheckedChange = { appPreferences.updateNotifications(it) }
        )
    HorizontalDivider(color = BorderColor)
    }
}

@Composable
private fun DataSection(appPreferences: AppPreferences) {
    var showDataDialog by rememberSaveable { mutableStateOf(false) }
    SettingsSection(title = "Datos y Sincronización") {
        SettingsSwitchItem(
            title = "Sincronización Automática",
            subtitle = "Sincronizar datos automáticamente",
            icon = Icons.Default.Sync,
            checked = appPreferences.autoSync,
            onCheckedChange = { appPreferences.updateAutoSync(it) }
        )
    SettingsDivider()
        SettingsClickableItem(
            title = "Uso de Datos",
            subtitle = DataUsageOption.values().find { it.key == appPreferences.dataUsage }?.displayName ?: "Solo WiFi",
            icon = Icons.Default.DataUsage,
            onClick = { showDataDialog = true }
        )
    }
    
    if (showDataDialog) {
        DataUsageDialog(
            currentSelection = appPreferences.dataUsage,
            onSelectionChanged = { appPreferences.updateDataUsage(it) },
            onDismiss = { showDataDialog = false }
        )
    }
}

@Composable
private fun LanguageSection(appPreferences: AppPreferences) {
    var showLanguageDialog by rememberSaveable { mutableStateOf(false) }
    SettingsSection(title = "Idioma y Región") {
        SettingsClickableItem(
            title = "Idioma",
            subtitle = LanguageOption.values().find { it.key == appPreferences.language }?.displayName ?: "Español",
            icon = Icons.Default.Language,
            onClick = { showLanguageDialog = true }
        )
    }
    
    if (showLanguageDialog) {
        LanguageDialog(
            currentSelection = appPreferences.language,
            onSelectionChanged = { appPreferences.updateLanguage(it) },
            onDismiss = { showLanguageDialog = false }
        )
    }
}

@Composable
private fun AccountSection(onDelete: () -> Unit = {}, onLogout: () -> Unit = {}) {
    SettingsSection(title = "Cuenta") {
        SettingsClickableItem(
            title = "Privacidad",
            subtitle = "Configuración de privacidad y datos",
            icon = Icons.Default.Security,
            onClick = { }
        )
    SettingsDivider()
        SettingsClickableItem(
            title = "Ayuda y Soporte",
            subtitle = "Obtener ayuda y contactar soporte",
            icon = Icons.AutoMirrored.Filled.Help,
            onClick = { }
        )
    SettingsDivider()
        SettingsClickableItem(
            title = "Acerca de",
            subtitle = "Información de la aplicación",
            icon = Icons.Default.Info,
            onClick = { }
        )
    SettingsDivider()
        SettingsClickableItem(
            title = "Eliminar cuenta",
            subtitle = "Eliminar permanentemente tu cuenta",
            icon = Icons.Default.Delete,
            onClick = { onDelete() },
            isDestructive = true
        )
    SettingsDivider()
        SettingsClickableItem(
            title = "Cerrar Sesión",
            subtitle = "Salir de tu cuenta",
            icon = Icons.AutoMirrored.Filled.Logout,
            onClick = onLogout,
            isDestructive = true
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 0.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(brush = Brush.linearGradient(listOf(com.kvn.jobopportunityapp.ui.theme.GradientStart, com.kvn.jobopportunityapp.ui.theme.GradientEnd))), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled, colors = SwitchDefaults.colors(checkedThumbColor = Primary))
    }
}

@Composable
private fun SettingsClickableItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    val titleColor = if (isDestructive) Error else TextPrimary
    val subtitleColor = if (isDestructive) Error.copy(alpha = 0.85f) else TextSecondary
    val chevronTint = if (isDestructive) Error else TextSecondary
    val rowBg = if (isDestructive) Color(0xFFFFEBEE) else Color.Transparent
    val iconBoxBg = if (isDestructive) Error.copy(alpha = 0.12f) else null

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = rowBg, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (iconBoxBg != null) {
            Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(color = iconBoxBg), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = title, tint = Error, modifier = Modifier.size(20.dp))
            }
        } else {
            Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(brush = Brush.linearGradient(listOf(com.kvn.jobopportunityapp.ui.theme.GradientStart, com.kvn.jobopportunityapp.ui.theme.GradientEnd))), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }

        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = titleColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = subtitleColor)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = chevronTint)
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(color = BorderColor)
}

@Composable
private fun DataUsageDialog(
    currentSelection: String,
    onSelectionChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Uso de Datos",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )
        },
        text = {
            Column {
                DataUsageOption.values().forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectionChanged(option.key)
                                onDismiss()
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSelection == option.key,
                            onClick = {
                                onSelectionChanged(option.key)
                                onDismiss()
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Primary)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = option.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Primary)
            }
        }
    )
}

@Composable
private fun LanguageDialog(
    currentSelection: String,
    onSelectionChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Idioma",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )
        },
        text = {
            Column {
                LanguageOption.values().forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectionChanged(option.key)
                                onDismiss()
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSelection == option.key,
                            onClick = {
                                onSelectionChanged(option.key)
                                onDismiss()
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Primary)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = option.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Primary)
            }
        }
    )
}
