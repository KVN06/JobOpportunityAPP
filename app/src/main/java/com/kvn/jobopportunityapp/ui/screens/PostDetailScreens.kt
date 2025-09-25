package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kvn.jobopportunityapp.domain.model.JobPost
import com.kvn.jobopportunityapp.domain.model.ClassifiedPost
import com.kvn.jobopportunityapp.domain.model.TrainingPost
import com.kvn.jobopportunityapp.ui.theme.*

@Composable
fun JobDetailScreen(post: JobPost, onBack: () -> Unit) {
    DetailScaffold(title = post.title, onBack = onBack) {
        Text("Salario: ${post.salary}", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        Text("Ubicación: ${post.location}", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
        Spacer(Modifier.height(12.dp))
        Text(text = post.description, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
    }
}

@Composable
fun ClassifiedDetailScreen(post: ClassifiedPost, onBack: () -> Unit) {
    DetailScaffold(title = post.title, onBack = onBack) {
        Text("Precio: ${post.price}", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        Text("Ubicación: ${post.location}", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
        Spacer(Modifier.height(12.dp))
        Text(text = post.description, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
    }
}

@Composable
fun TrainingDetailScreen(post: TrainingPost, onBack: () -> Unit) {
    DetailScaffold(title = post.title, onBack = onBack) {
        Text("Proveedor: ${post.provider}", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        Text("Fechas: ${post.startDate} → ${post.endDate}", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
        Spacer(Modifier.height(12.dp))
        Text(text = post.description, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
    }
}

@Composable
private fun DetailScaffold(title: String, onBack: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
            }
            Spacer(Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
        }
        Spacer(Modifier.height(16.dp))
        Column { content() }
    }
}
