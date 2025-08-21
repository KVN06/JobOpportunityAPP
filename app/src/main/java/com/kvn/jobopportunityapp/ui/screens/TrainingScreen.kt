package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kvn.jobopportunityapp.ui.theme.*

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import com.kvn.jobopportunityapp.ui.components.CourseInfoChip

@Composable
fun TrainingScreen() {

    var selectedCategory by remember { mutableIntStateOf(0) }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Capacitaciones",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(20.dp))
            TrainingCategoryTabs(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )
            Spacer(modifier = Modifier.height(20.dp))
            when (selectedCategory) {
                0 -> PopularCourses()
                1 -> TechCourses()
                2 -> BusinessCourses()
                3 -> DesignCourses()
            }
        }
    }
}

@Composable
fun TrainingCategoryTabs(
    selectedCategory: Int,
    onCategorySelected: (Int) -> Unit
) {
    val categories = listOf("Populares", "Tecnología", "Negocios", "Diseño")
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEachIndexed { index, category ->
            TrainingCategoryTab(
                text = category,
                isSelected = selectedCategory == index,
                onClick = { onCategorySelected(index) }
            )
        }
    }
}
@Composable
fun TrainingCategoryTab(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
        color = if (isSelected) Primary else SurfaceVariant,
        onClick = onClick
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
            ),
            color = if (isSelected) Color.White else TextSecondary
        )
    }
}
@Composable
fun PopularCourses() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(6) { index ->
            val courses = listOf(
                CourseItem(
                    title = "Android Development con Kotlin",
                    instructor = "Universidad del Cauca - Popayán",
                    duration = "40 horas",
                    level = "Intermedio",
                    rating = 4.8f,
                    price = "GRATIS",
                    icon = Icons.Default.Android,
                    color = TextPrimary
                ),
                CourseItem(
                    title = "UX/UI Design Fundamentals",
                    instructor = "SENA Popayán",
                    duration = "30 horas",
                    level = "Principiante",
                    rating = 4.7f,
                    price = "GRATIS",
                    icon = Icons.Default.Brush,
                    color = Secondary
                ),
                CourseItem(
                    title = "Data Science con Python",
                    instructor = "Tech Institute",
                    duration = "60 horas",
                    level = "Intermedio",
                    rating = 4.9f,
                    price = "$95.000",
                    icon = Icons.Default.Analytics,
                    color = Primary
                ),
                CourseItem(
                    title = "Marketing Digital",
                    instructor = "Business School",
                    duration = "25 horas",
                    level = "Principiante",
                    rating = 4.6f,
                    price = "$65.000",
                    icon = Icons.Default.TrendingUp,
                    color = Secondary
                ),
                CourseItem(
                    title = "JavaScript Full Stack",
                    instructor = "Code Academy",
                    duration = "80 horas",
                    level = "Avanzado",
                    rating = 4.8f,
                    price = "$120.000",
                    icon = Icons.Default.Code,
                    color = Secondary
                ),
                CourseItem(
                    title = "Project Management",
                    instructor = "MBA Online",
                    duration = "35 horas",
                    level = "Intermedio",
                    rating = 4.5f,
                    price = "$75.000",
                    icon = Icons.Default.Assignment,
                    color = PrimaryVariant
                )
            )
            CourseCard(courses[index])
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun TechCourses() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(5) { index ->
            val techCourses = listOf(
                CourseItem(
                    title = "Machine Learning Avanzado",
                    instructor = "AI Institute",
                    duration = "70 horas",
                    level = "Avanzado",
                    rating = 4.9f,
                    price = "$150.000",
                    icon = Icons.Default.Psychology,
                    color = Primary
                ),
                CourseItem(
                    title = "Cloud Computing AWS",
                    instructor = "Amazon Web Services",
                    duration = "45 horas",
                    level = "Intermedio",
                    rating = 4.7f,
                    price = "$110.000",
                    icon = Icons.Default.Cloud,
                    color = Secondary
                ),
                CourseItem(
                    title = "Cybersecurity Essentials",
                    instructor = "Security Pro",
                    duration = "35 horas",
                    level = "Principiante",
                    rating = 4.6f,
                    price = "$85.000",
                    icon = Icons.Default.Security,
                    color = TextPrimary
                ),
                CourseItem(
                    title = "Blockchain Development",
                    instructor = "Crypto Academy",
                    duration = "50 horas",
                    level = "Avanzado",
                    rating = 4.5f,
                    price = "$130.000",
                    icon = Icons.Default.AccountBalanceWallet,
                    color = PrimaryVariant
                ),
                CourseItem(
                    title = "DevOps Fundamentals",
                    instructor = "Tech Ops",
                    duration = "40 horas",
                    level = "Intermedio",
                    rating = 4.8f,
                    price = "$98.000",
                    icon = Icons.Default.DeveloperMode,
                    color = Secondary
                )
            )
            CourseCard(techCourses[index])
        }
    }
}

@Composable
fun BusinessCourses() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(4) { index ->
            val businessCourses = listOf(
                CourseItem(
                    title = "Liderazgo Empresarial",
                    instructor = "Leadership Academy",
                    duration = "30 horas",
                    level = "Intermedio",
                    rating = 4.7f,
                    price = "$70.000",
                    icon = Icons.Default.Groups,
                    color = Primary
                ),
                CourseItem(
                    title = "Finanzas Corporativas",
                    instructor = "Finance Institute",
                    duration = "40 horas",
                    level = "Avanzado",
                    rating = 4.6f,
                    price = "$90.000",
                    icon = Icons.Default.AccountBalance,
                    color = TextPrimary
                ),
                CourseItem(
                    title = "Estrategia de Negocios",
                    instructor = "Business Pro",
                    duration = "25 horas",
                    level = "Intermedio",
                    rating = 4.8f,
                    price = "$65.000",
                    icon = Icons.Default.BusinessCenter,
                    color = Secondary
                ),
                CourseItem(
                    title = "Recursos Humanos",
                    instructor = "HR Academy",
                    duration = "35 horas",
                    level = "Principiante",
                    rating = 4.5f,
                    price = "$55.000",
                    icon = Icons.Default.People,
                    color = Secondary
                )
            )
            CourseCard(businessCourses[index])
        }
    }
}

@Composable
fun DesignCourses() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(4) { index ->
            val designCourses = listOf(
                CourseItem(
                    title = "Adobe Creative Suite",
                    instructor = "Adobe Certified",
                    duration = "50 horas",
                    level = "Intermedio",
                    rating = 4.8f,
                    price = "$105.000",
                    icon = Icons.Default.Palette,
                    color = PrimaryVariant
                ),
                CourseItem(
                    title = "Fotografía Digital",
                    instructor = "Photo Masters",
                    duration = "30 horas",
                    level = "Principiante",
                    rating = 4.7f,
                    price = "$60.000",
                    icon = Icons.Default.CameraAlt,
                    color = Primary
                ),
                CourseItem(
                    title = "Motion Graphics",
                    instructor = "Animation Studio",
                    duration = "45 horas",
                    level = "Avanzado",
                    rating = 4.6f,
                    price = "$115.000",
                    icon = Icons.Default.Movie,
                    color = Secondary
                ),
                CourseItem(
                    title = "Web Design Responsive",
                    instructor = "Web Academy",
                    duration = "35 horas",
                    level = "Intermedio",
                    rating = 4.9f,
                    price = "$80.000",
                    icon = Icons.Default.WebAsset,
                    color = TextPrimary
                )
            )
            CourseCard(designCourses[index])
        }
    }
}

@Composable
fun CourseCard(course: CourseItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(course.color.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        course.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = course.title,
                                tint = course.color,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = course.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = TextPrimary,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = course.instructor,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Primary,
                            maxLines = 1
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = course.price,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary,
                    maxLines = 1
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CourseInfoChip(Icons.Default.Schedule, course.duration)
                CourseInfoChip(Icons.Default.BarChart, course.level)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Warning,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = course.rating.toString(),
                        style = MaterialTheme.typography.bodySmall.copy(
                        ),
                        color = TextPrimary
                    )
                }
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
                    text = "Inscribirse",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

