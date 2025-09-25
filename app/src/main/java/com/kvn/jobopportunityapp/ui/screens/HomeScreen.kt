package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kvn.jobopportunityapp.ui.theme.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.kvn.jobopportunityapp.data.AppPreferences
import com.kvn.jobopportunityapp.ui.viewmodel.JobsUiState
import com.kvn.jobopportunityapp.ui.viewmodel.JobsViewModel
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Offset

@Composable
fun HomeScreen() {
    val visible = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val prefs = remember { AppPreferences(context) }
    val jobsVm = remember { JobsViewModel(prefs) }
    LaunchedEffect(Unit) {
        visible.value = true
        jobsVm.loadJobs()
    }
    AnimatedVisibility(
        visible = visible.value,
        enter = fadeIn(animationSpec = tween(350)) +
                slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(350)
                ),
        exit = fadeOut(animationSpec = tween(200)) +
                slideOutVertically(
                    targetOffsetY = { it / 2 },
                    animationSpec = tween(200)
                )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { WelcomeSection() }
            item { QuickActionsSection() }
            item { HomeJobsSection(jobsVm) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun WelcomeSection() {
    val bottomNav = com.kvn.jobopportunityapp.ui.navigation.LocalBottomBarNavigator.current
    val overlayNav = com.kvn.jobopportunityapp.ui.navigation.LocalOverlayNavigator.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(GradientStart, GradientEnd)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "¡Bienvenido de vuelta!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Encuentra tu próxima oportunidad laboral",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { bottomNav?.invoke(1) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Buscar empleos", color = GradientStart, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { overlayNav?.invoke("profile", null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp, brush = Brush.horizontalGradient(listOf(Color.White.copy(0.7f), Color.White.copy(0.7f))))
                    ) {
                        Text("Mi perfil")
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionsSection() {
    Column {
        Text(
            text = "Acciones Rápidas",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(4) { index ->
                val actions = listOf(
                    QuickAction("Buscar Empleos", Icons.Default.Search, Primary),
                    QuickAction("Mi Perfil", Icons.Default.Person, Secondary),
                    QuickAction("Postulaciones", Icons.AutoMirrored.Filled.Assignment, Warning),
                    QuickAction("Mensajes", Icons.AutoMirrored.Filled.Chat, Success)
                )
                QuickActionCard(actions[index])
            }
        }
    }
}

@Composable
private fun QuickActionCard(action: QuickAction) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(action.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.title,
                    tint = action.color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = action.title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun HomeJobsSection(jobsVm: JobsViewModel) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Empleos",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )
            // Botón opcional
            TextButton(onClick = { /* Navegar a Jobs tab si aplica */ }) {
                Text(
                    text = "Ver todos",
                    color = Primary
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        when (val state = jobsVm.state.value) {
            is JobsUiState.Loading -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    repeat(3) { JobCardSkeleton() }
                }
            }
            is JobsUiState.Error -> {
                Text(
                    text = "Error: ${state.message}",
                    color = Error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            is JobsUiState.Data, JobsUiState.Idle -> {
                val list = (state as? JobsUiState.Data)?.jobs.orEmpty().take(3)
                if (list.isEmpty()) {
                    Text(
                        text = "Sin empleos disponibles",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        list.forEach { job ->
                            JobCard(
                                company = job.companyName ?: job.companyNameAlt ?: "Empresa",
                                position = job.title ?: "(Sin título)",
                                location = job.location ?: job.city ?: "--",
                                salary = job.salary ?: job.salaryRange ?: "--"
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------- Skeletons / Shimmer (Home) ----------

@Composable
private fun HomeShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "homeShimmer")
    val x by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "homeShimmerX"
    )
    val base = CardBackground
    val hi = base.copy(alpha = 0.6f)
    return Brush.linearGradient(listOf(base, hi, base), start = Offset(x - 600f, 0f), end = Offset(x, 0f))
}

@Composable
private fun HomeShimmerBox(modifier: Modifier, corner: Int = 12) {
    Box(modifier = modifier.clip(RoundedCornerShape(corner.dp)).background(HomeShimmerBrush()))
}

@Composable
private fun JobCardSkeleton() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            HomeShimmerBox(modifier = Modifier.fillMaxWidth(0.7f).height(20.dp), corner = 6)
            Spacer(Modifier.height(8.dp))
            HomeShimmerBox(modifier = Modifier.fillMaxWidth(0.4f).height(16.dp), corner = 6)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HomeShimmerBox(modifier = Modifier.size(width = 90.dp, height = 28.dp), corner = 14)
                HomeShimmerBox(modifier = Modifier.size(width = 80.dp, height = 28.dp), corner = 14)
            }
        }
    }
}

@Composable
private fun JobCard(
    company: String,
    position: String,
    location: String,
    salary: String
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = position,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = TextPrimary
                    )
                    Text(
                        text = company,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Primary
                    )
                }
                Icon(
                    imageVector = Icons.Default.BookmarkBorder,
                    contentDescription = "Guardar",
                    tint = TextSecondary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoChip(Icons.Default.LocationOn, location)
                InfoChip(Icons.Default.AttachMoney, salary)
            }
        }
    }
}

@Composable
private fun InfoChip(icon: ImageVector, text: String) {
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

// Se removieron estadísticas con números fijos para cumplir "solo API".

data class QuickAction(
    val title: String,
    val icon: ImageVector,
    val color: Color
)
