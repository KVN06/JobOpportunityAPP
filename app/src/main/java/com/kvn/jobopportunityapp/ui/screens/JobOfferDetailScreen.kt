package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kvn.jobopportunityapp.ui.components.TopHeader
import com.kvn.jobopportunityapp.ui.theme.Background
import com.kvn.jobopportunityapp.ui.theme.TextPrimary
import com.kvn.jobopportunityapp.ui.theme.TextSecondary

@Composable
fun JobOfferDetailScreen(
    title: String,
    company: String?,
    location: String?,
    salary: String?,
    description: String?,
    onBack: () -> Unit,
    sharedProgress: Float = 1f
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopHeader(title = "Oferta", onBack = onBack, sharedProgress = sharedProgress)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, color = TextPrimary)
            Spacer(Modifier.height(8.dp))
            company?.takeIf { it.isNotBlank() }?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(Modifier.height(4.dp))
            }
            location?.takeIf { it.isNotBlank() }?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(Modifier.height(4.dp))
            }
            salary?.takeIf { it.isNotBlank() }?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(Modifier.height(12.dp))
            }
            Text(
                text = description?.ifBlank { "Sin descripción" } ?: "Sin descripción",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
        }
    }
}
