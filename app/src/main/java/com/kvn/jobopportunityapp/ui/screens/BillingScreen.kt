package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kvn.jobopportunityapp.ui.components.TopHeader

@Composable
fun BillingScreen(onBack: () -> Unit, sharedProgress: Float = 1f) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopHeader(title = "Job Opportunity", onBack = onBack, sharedProgress = sharedProgress)

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp)) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Gestiona tus planes, facturación y recibos (placeholder)")
                }
            }
        }
    }
}
