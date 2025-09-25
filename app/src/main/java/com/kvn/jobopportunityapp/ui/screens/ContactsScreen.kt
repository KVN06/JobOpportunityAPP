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
import com.kvn.jobopportunityapp.ui.viewmodel.ContactsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ContactsScreen(onBack: () -> Unit, sharedProgress: Float = 1f) {
    val vm: ContactsViewModel = viewModel()
    var isLoading by remember { mutableStateOf(true) }
    val items by vm.items.collectAsState()
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
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .background(Color(0xFFF0F2F5))
                        ) {}
                    }
                }
            }
            items.forEach { c ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = c.name, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(text = "${c.role} · ${c.company}", color = Color.Gray)
                    }
                }
            }
        }
    }
}
