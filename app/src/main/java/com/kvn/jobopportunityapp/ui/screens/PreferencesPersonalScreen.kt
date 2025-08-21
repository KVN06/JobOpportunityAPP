package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import com.kvn.jobopportunityapp.ui.components.TopHeader
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PreferencesPersonalScreen(onBack: () -> Unit, sharedProgress: Float = 1f) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
    TopHeader(title = "Job Opportunity", onBack = onBack, sharedProgress = sharedProgress)

        Card(modifier = Modifier.padding(top = 16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Preferencias personales", style = MaterialTheme.typography.titleMedium)
                Text(text = "Aquí podrás editar tus preferencias personales (notificaciones, visibilidad, etc.). Este es un placeholder para implementar la lógica.")
            }
        }
    }
}
