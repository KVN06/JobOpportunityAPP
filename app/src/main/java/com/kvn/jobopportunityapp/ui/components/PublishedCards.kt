package com.kvn.jobopportunityapp.ui.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.kvn.jobopportunityapp.ui.navigation.LocalOverlayNavigator
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kvn.jobopportunityapp.domain.model.ClassifiedPost
import com.kvn.jobopportunityapp.domain.model.JobPost
import com.kvn.jobopportunityapp.domain.model.TrainingPost
import com.kvn.jobopportunityapp.ui.theme.*
import java.util.concurrent.TimeUnit

private fun timeAgoText(ts: Long): String {
    val diff = System.currentTimeMillis() - ts
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val mins = TimeUnit.MILLISECONDS.toMinutes(diff)
    return when {
        days >= 1 -> "$days days ago"
        hours >= 1 -> "$hours hours ago"
        else -> "$mins mins ago"
    }
}

@Composable
private fun MapLink(url: String?) {
    // This composable uses an ambient route dispatcher through a simple activity-wide state in MainActivity.
    // To keep it decoupled, we navigate by sending a broadcast intent-like callback via LocalContext cast if needed.
    if (url.isNullOrBlank()) return
    val context = LocalContext.current
    val nav = LocalOverlayNavigator.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            if (nav != null) {
                nav("map_preview", url)
            } else {
                // Fallback externo sin try/catch directo (Compose restriction)
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                runCatching { context.startActivity(intent) }
            }
        }
    ) {
        Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Ver en mapa", tint = TextSecondary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text("Ver en mapa", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun JobPublishedCard(post: JobPost, onDetails: () -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            // Top Row: title + type pill + price box
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text(post.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    Spacer(Modifier.height(6.dp))
                    Text("Empresa verificada", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                Spacer(Modifier.width(8.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Surface(color = Primary, shape = RoundedCornerShape(50)) {
                        Text(
                            text = "Contrato",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Surface(color = CardBackground, shape = RoundedCornerShape(12.dp)) {
                        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), horizontalAlignment = Alignment.End) {
                            Text(post.salary.ifBlank { "$ 0.00" }, color = TextPrimary, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(timeAgoText(post.createdAt), color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Meta row: location, map, time
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Primary, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(post.location, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
                MapLink(post.geoUrl)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(timeAgoText(post.createdAt), color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(10.dp))

            // Categories chips
            FlowRowChips(post.categories)

            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = onDetails,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Ver Detalles", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}

// Eliminado: ClassifiedPublishedCard y TrainingPublishedCard (maquetación local)

@Deprecated("Mantener por compatibilidad, se eliminará si deja de usarse")
fun ClassifiedPublishedCardDeprecated(post: ClassifiedPost, onDetails: () -> Unit = {}) {}

@Deprecated("Mantener por compatibilidad, se eliminará si deja de usarse")
fun TrainingPublishedCardDeprecated(post: TrainingPost, onDetails: () -> Unit = {}) {}

@Composable
private fun FlowRowChips(categories: List<String>) {
    // Simple wrap: two rows max via weight – for now do a Column with wrap logic
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        categories.take(3).forEach { c ->
            Surface(color = SurfaceVariant, shape = RoundedCornerShape(20.dp)) {
                Text(c, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = TextPrimary, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
