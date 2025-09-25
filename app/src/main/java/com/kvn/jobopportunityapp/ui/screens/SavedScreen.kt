package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kvn.jobopportunityapp.ui.components.TopHeader
import com.kvn.jobopportunityapp.ui.viewmodel.FavoritesRemoteViewModel
import androidx.compose.ui.platform.LocalContext
import com.kvn.jobopportunityapp.data.AppPreferences

@Composable
fun SavedScreen(onBack: () -> Unit, sharedProgress: Float = 1f) {
    val context = LocalContext.current
    val prefs = remember { com.kvn.jobopportunityapp.data.AppPreferences(context) }
    val vm = remember { FavoritesRemoteViewModel(prefs) }
    val items by vm.items.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    LaunchedEffect(Unit) { vm.load() }
    Column(modifier = Modifier.fillMaxSize()) {
        TopHeader(title = "Job Opportunity", onBack = onBack, sharedProgress = sharedProgress)

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp)) {
            Spacer(modifier = Modifier.height(12.dp))
            if (loading) {
                repeat(4) {
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .background(Color(0xFFF0F2F5))) {}
                    }
                }
            }
            if (error != null) {
                Text(text = "Error: $error", color = Color.Red)
                Spacer(Modifier.height(8.dp))
            }
            items.forEach { item ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(text = item.subtitle, color = Color.Gray)
                        Spacer(Modifier.height(6.dp))
                        AssistChip(onClick = { /* no-op */ }, label = { Text(item.type.uppercase()) })
                    }
                }
            }
        }
    }
}
