package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.kvn.jobopportunityapp.ui.components.TopHeader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PreferencesPersonalScreen(onBack: () -> Unit, sharedProgress: Float = 1f) {

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopHeader(title = "Job Opportunity", onBack = onBack, sharedProgress = sharedProgress)

        Spacer(Modifier.height(12.dp))
        Text("Preferencias personales pendientes de API.")
    }
}
