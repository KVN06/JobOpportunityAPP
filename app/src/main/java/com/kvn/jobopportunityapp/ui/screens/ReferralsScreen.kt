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

@Composable
fun ReferralsScreen(onBack: () -> Unit, sharedProgress: Float = 1f) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopHeader(title = "Job Opportunity", onBack = onBack, sharedProgress = sharedProgress)

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp)) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Referidos no disponibles (API pendiente).")
        }
    }
}
