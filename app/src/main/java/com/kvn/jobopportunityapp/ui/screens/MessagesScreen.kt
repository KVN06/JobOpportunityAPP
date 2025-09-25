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
import com.kvn.jobopportunityapp.ui.viewmodel.MessagesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MessagesScreen(onBack: () -> Unit, sharedProgress: Float = 1f) {
    val vm: MessagesViewModel = viewModel()
    var isLoading by remember { mutableStateOf(true) }
    val messages by vm.items.collectAsState()
    LaunchedEffect(Unit) { delay(350); isLoading = false }
    Column(modifier = Modifier.fillMaxSize()) {
            TopHeader(title = "Job Opportunity", onBack = onBack, sharedProgress = sharedProgress)

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp)) {
            Spacer(modifier = Modifier.height(12.dp))
            if (isLoading) {
                repeat(5) {
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(Color(0xFFF0F2F5))) {}
                    }
                }
            }
            messages.forEach { msg ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(text = msg.from, style = MaterialTheme.typography.titleMedium)
                            Text(text = msg.time, color = Color.Gray)
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(text = msg.preview, color = Color.DarkGray)
                        if (msg.unread) {
                            Spacer(Modifier.height(6.dp))
                            AssistChip(onClick = { }, label = { Text("Nuevo") })
                        }
                    }
                }
            }
        }
    }
}
