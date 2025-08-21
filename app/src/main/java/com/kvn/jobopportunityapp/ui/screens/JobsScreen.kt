package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kvn.jobopportunityapp.ui.theme.*

@Composable
fun JobsScreen() {
    var searchText by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        // Barra de búsqueda
        SearchBar(
            searchText = searchText,
            onSearchTextChange = { searchText = it }
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Filtros
        FilterChips()
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Lista de empleos
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(10) { index ->
                JobListItem(
                    company = listOf("TechCorp Popayán", "DigitalPopayán SAS", "InnovaPopayán", "PopayánTech", "DevPopayán")[index % 5],
                    position = listOf(
                        "Senior Android Developer",
                        "UX/UI Designer",
                        "Data Scientist",
                        "Product Manager",
                        "Backend Engineer"
                    )[index % 5],
                    location = listOf("Popayán", "Popayán", "Popayán", "Popayán", "Popayán")[index % 5],
                    salary = "$2.500.000 - $4.500.000",
                    type = listOf("Tiempo completo", "Medio tiempo", "Freelance")[index % 3],
                    isNew = index < 3
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
        val visible = remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible.value = true }
        AnimatedVisibility(
            visible = visible.value,
            enter = fadeIn(animationSpec = tween(350, easing = androidx.compose.animation.core.FastOutSlowInEasing)) +
                    slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(350, easing = androidx.compose.animation.core.FastOutSlowInEasing)
                    ),
            exit = fadeOut(animationSpec = tween(200)) +
                    slideOutVertically(
                        targetOffsetY = { it / 2 },
                        animationSpec = tween(200)
                    )
        ) {
            // ...existing code...
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { 
            Text(
                text = "Buscar empleos, empresas...",
                color = TextSecondary
            ) 
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = TextSecondary
            )
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = { onSearchTextChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar",
                        tint = TextSecondary
                    )
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary,
            unfocusedBorderColor = BorderColor,
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground
        )
    )
}

@Composable
private fun FilterChips() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilterChip(text = "Todos", isSelected = true)
        FilterChip(text = "Remoto", isSelected = false)
        FilterChip(text = "Presencial", isSelected = false)
        FilterChip(text = "Híbrido", isSelected = false)
    }
}

@Composable
private fun FilterChip(text: String, isSelected: Boolean) {
    Surface(
        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
        color = if (isSelected) Primary else SurfaceVariant,
        onClick = { }
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = if (isSelected) Color.White else TextSecondary
        )
    }
}

@Composable
private fun JobListItem(
    company: String,
    position: String,
    location: String,
    salary: String,
    type: String,
    isNew: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = position,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary
                        )
                        if (isNew) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = TextPrimary
                            ) {
                                Text(
                                    text = "NUEVO",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = company,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Primary
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Guardar",
                            tint = TextSecondary
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir",
                            tint = TextSecondary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                JobInfoItem(Icons.Default.LocationOn, location)
                JobInfoItem(Icons.Default.AttachMoney, salary)
                JobInfoItem(Icons.Default.Schedule, type)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                )
            ) {
                Text(
                    text = "Postularse",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun JobInfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}
