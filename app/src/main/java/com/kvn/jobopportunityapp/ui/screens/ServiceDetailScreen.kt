package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kvn.jobopportunityapp.ui.theme.*

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    serviceCategory: String,
    provider: ServiceProvider,
    onBack: () -> Unit
) {
    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible.value = true }
    AnimatedVisibility(
        visible = visible.value,
        enter = fadeIn(animationSpec = tween(350, easing = androidx.compose.animation.core.FastOutSlowInEasing)) +
                slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(350, easing = androidx.compose.animation.core.FastOutSlowInEasing)
                ),
        exit = fadeOut(animationSpec = tween(200)) +
                slideOutVertically(
                    targetOffsetY = { it / 2 },
                    animationSpec = tween(200)
                )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
            // Header con gradiente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Primary, PrimaryVariant)
                        )
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = provider.name,
                    tint = Primary,
                    modifier = Modifier.size(32.dp)
                )
            }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = provider.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
                Text(
                    text = provider.specialization,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = " ${provider.rating} • ${provider.reviews} reseñas",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = provider.price,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Primary
                )
                Button(
                    onClick = { /* Contactar */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Contactar", color = Color.White)
                }
            }
        }
    }

data class ServiceProvider(
    val name: String,
    val specialization: String,
    val rating: Float,
    val reviews: Int,
    val price: String
)

private fun getServiceProviders(category: String): List<ServiceProvider> {
    return when (category) {
        "Desarrollo Web" -> listOf(
            ServiceProvider("Juan Carlos M.", "Frontend & Backend", 4.8f, 25, "$500.000"),
            ServiceProvider("María Elena R.", "React & Node.js", 4.9f, 18, "$650.000"),
            ServiceProvider("Carlos Andrés V.", "Full Stack Developer", 4.7f, 32, "$450.000"),
            ServiceProvider("Laura Sofía P.", "WordPress & Shopify", 4.8f, 21, "$400.000")
        )
        "Diseño Gráfico" -> listOf(
            ServiceProvider("Ana Isabel C.", "Branding & Logos", 4.9f, 45, "$200.000"),
            ServiceProvider("Roberto Díaz", "Diseño Digital", 4.7f, 28, "$250.000"),
            ServiceProvider("Carmen Torres", "Ilustración", 4.8f, 35, "$180.000"),
            ServiceProvider("Diego Muñoz", "Packaging Design", 4.6f, 22, "$220.000")
        )
        "Marketing Digital" -> listOf(
            ServiceProvider("Alejandro R.", "SEO & SEM", 4.8f, 41, "$350.000"),
            ServiceProvider("Valentina S.", "Social Media", 4.9f, 38, "$300.000"),
            ServiceProvider("Andrés López", "Google Ads", 4.7f, 29, "$400.000"),
            ServiceProvider("Camila Rojas", "Content Marketing", 4.8f, 33, "$280.000")
        )
        else -> listOf(
            ServiceProvider("Profesional 1", "Especialista", 4.5f, 15, "$150.000"),
            ServiceProvider("Profesional 2", "Experto", 4.7f, 20, "$200.000"),
            ServiceProvider("Profesional 3", "Consultor", 4.6f, 18, "$180.000")
        )
    }
}
