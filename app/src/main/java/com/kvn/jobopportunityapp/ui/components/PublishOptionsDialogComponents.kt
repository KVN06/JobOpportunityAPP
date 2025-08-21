package com.kvn.jobopportunityapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kvn.jobopportunityapp.data.PublishOption
import com.kvn.jobopportunityapp.data.UserType
import com.kvn.jobopportunityapp.data.getPublishPermissions
import com.kvn.jobopportunityapp.data.getAvailableOptions
import com.kvn.jobopportunityapp.ui.theme.*

@Composable
fun PublishOptionsDialog(
    userType: UserType,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            PublishOptionsContent(
                userType = userType,
                onDismiss = onDismiss,
                onOptionSelected = onOptionSelected
            )
        }
    }
}

@Composable
private fun PublishOptionsContent(
    userType: UserType,
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    val permissions = userType.getPublishPermissions()
    val options = permissions.getAvailableOptions()
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Qué deseas publicar?",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
                
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = getUserTypeDescription(userType),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Options
            options.forEach { option ->
                PublishOptionItem(
                    option = option,
                    onClick = {
                        onOptionSelected(option.route)
                        onDismiss()
                    }
                )
                
                if (option != options.last()) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            
            if (options.isEmpty()) {
                // Mensaje cuando no hay opciones disponibles
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = TextSecondary,
                        modifier = Modifier.size(48.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "No tienes permisos para publicar",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextSecondary
                    )
                    
                    Text(
                        text = "Contacta al administrador",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun PublishOptionItem(
    option: PublishOption,
    onClick: () -> Unit
) {
    val icon = getIconForOption(option.route)
    val color = getColorForOption(option.route)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = option.title,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = option.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
                
                Text(
                    text = option.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Ir",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

private fun getUserTypeDescription(userType: UserType): String {
    return when (userType) {
        UserType.CESANTE -> "Como persona en búsqueda de empleo, puedes publicar:"
        UserType.EMPRESA -> "Como empresa registrada, puedes publicar:"
    }
}

private fun getIconForOption(route: String): ImageVector {
    return when (route) {
        "publish_job" -> Icons.Default.Work
        "publish_classified" -> Icons.Default.GridView
        "publish_training" -> Icons.Default.School
        else -> Icons.Default.Add
    }
}

private fun getColorForOption(route: String): Color {
    return when (route) {
        "publish_job" -> Primary // Azul oscuro profesional
        "publish_classified" -> Secondary // Gris oscuro
        "publish_training" -> TextPrimary // Negro carbón
        else -> Primary
    }
}
