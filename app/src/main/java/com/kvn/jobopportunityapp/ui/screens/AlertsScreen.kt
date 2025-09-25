package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kvn.jobopportunityapp.ui.components.TopHeader
import com.kvn.jobopportunityapp.ui.viewmodel.AlertsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AlertsScreen(onBack: () -> Unit, sharedProgress: Float = 1f) {
    val vm: AlertsViewModel = viewModel()
    val alerts by vm.items.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        TopHeader(title = "Job Opportunity", onBack = onBack, sharedProgress = sharedProgress)

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp)) {
            Spacer(modifier = Modifier.height(12.dp))
            alerts.forEach { a ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = a.query, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(text = "Frecuencia: ${a.frequency}", color = Color.Gray)
                        Spacer(Modifier.height(6.dp))
                        AssistChip(onClick = { }, label = { Text(if (a.active) "Activa" else "Inactiva") })
                    }
                }
            }
        }
    }
}
