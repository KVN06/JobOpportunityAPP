package com.kvn.jobopportunityapp.ui.screens
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.kvn.jobopportunityapp.ui.theme.*
import androidx.compose.runtime.*

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween

data class Experience(
    val title: String,
    val company: String,
    val period: String,
    val description: String
)

data class Education(
    val degree: String,
    val institution: String,
    val year: String,
    val description: String
)

data class Skill(
    val name: String,
    val level: Float // 0.0 to 1.0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CVScreen(
    onBackClick: () -> Unit = {}
) {
    var showAddExperienceDialog by remember { mutableStateOf(false) }
    var showAddEducationDialog by remember { mutableStateOf(false) }
    var showAddSkillDialog by remember { mutableStateOf(false) }
    
    // Datos de ejemplo - en una app real vendrían de una base de datos
    var experiences by remember { mutableStateOf(listOf(
        Experience(
            "Desarrollador Android Senior",
            "TechCorp Popayán",
            "2022 - Presente",
            "Desarrollo de aplicaciones móviles usando Kotlin y Jetpack Compose"
        ),
        Experience(
            "Desarrollador Junior",
            "StartUp Popayán",
            "2020 - 2022",
            "Desarrollo web con React y Node.js"
        )
    )) }
    
    var education by remember { mutableStateOf(listOf(
        Education(
            "Ingeniería de Sistemas",
            "Universidad del Cauca",
            "2018 - 2022",
            "Enfoque en desarrollo de software y bases de datos"
        ),
        Education(
            "Técnico en Programación",
            "SENA Popayán",
            "2017 - 2018",
            "Fundamentos de programación y desarrollo web"
        )
    )) }
    
    var skills by remember { mutableStateOf(listOf(
        Skill("Kotlin", 0.9f),
        Skill("Android Development", 0.85f),
        Skill("Jetpack Compose", 0.8f),
        Skill("Java", 0.75f),
        Skill("React", 0.7f),
        Skill("Node.js", 0.65f),
        Skill("MySQL", 0.7f),
        Skill("Git", 0.8f)
    )) }

    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible.value = true }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { onBackClick() }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            modifier = Modifier.size(24.dp),
                            tint = Primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Atrás",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Primary)
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    ProfileCard()
                }
                item {
                    SectionCard(
                        title = "Experiencia Laboral",
                        icon = Icons.Default.Work,
                        onAddClick = { showAddExperienceDialog = true }
                    ) {
                        experiences.forEach { experience ->
                            ExperienceItem(experience = experience)
                            if (experience != experiences.last()) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
                item {
                    SectionCard(
                        title = "Educación",
                        icon = Icons.Default.School,
                        onAddClick = { showAddEducationDialog = true }
                    ) {
                        education.forEach { edu ->
                            EducationItem(education = edu)
                            if (edu != education.last()) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
                item {
                    SectionCard(
                        title = "Habilidades",
                        icon = Icons.Default.Star,
                        onAddClick = { showAddSkillDialog = true }
                    ) {
                        skills.forEach { skill ->
                            SkillItem(skill = skill)
                            if (skill != skills.last()) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
            // Dialogs
            if (showAddExperienceDialog) {
                AddExperienceDialog(
                    onDismiss = { showAddExperienceDialog = false },
                    onAdd = { newExperience ->
                        experiences = experiences + newExperience
                        showAddExperienceDialog = false
                    }
                )
            }
            if (showAddEducationDialog) {
                AddEducationDialog(
                    onDismiss = { showAddEducationDialog = false },
                    onAdd = { newEducation ->
                        education = education + newEducation
                        showAddEducationDialog = false
                    }
                )
            }
            if (showAddSkillDialog) {
                AddSkillDialog(
                    onDismiss = { showAddSkillDialog = false },
                    onAdd = { newSkill ->
                        skills = skills + newSkill
                        showAddSkillDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de perfil
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Primary, Secondary)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "VibefksTech",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            
            Text(
                text = "Desarrollador Android Senior",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = TextSecondary
                )
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Desarrollador apasionado con 4+ años de experiencia en aplicaciones móviles. Especializado en Android nativo con Kotlin y Jetpack Compose.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Información de contacto
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ContactInfo(
                    icon = Icons.Default.Email,
                    text = "vibefks@tech.co"
                )
                ContactInfo(
                    icon = Icons.Default.Phone,
                    text = "+57 300 123 4567"
                )
                ContactInfo(
                    icon = Icons.Default.LocationOn,
                    text = "Popayán, Cauca"
                )
            }
        }
    }
}

@Composable
fun ContactInfo(
    icon: ImageVector,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 10.sp
            )
        )
    }
}

@Composable
fun SectionCard(
    title: String,
    icon: ImageVector,
    onAddClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }
                
                IconButton(
                    onClick = onAddClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Primary.copy(alpha = 0.1f),
                        contentColor = Primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            content()
        }
    }
}

@Composable
fun ExperienceItem(experience: Experience) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = experience.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = experience.company,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Primary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            
            Text(
                text = experience.period,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary
                )
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = experience.description,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextSecondary
            )
        )
    }
}

@Composable
fun EducationItem(education: Education) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = education.degree,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = education.institution,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Primary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            
            Text(
                text = education.year,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary
                )
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = education.description,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextSecondary
            )
        )
    }
}

@Composable
fun SkillItem(skill: Skill) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = skill.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            )
            
            Text(
                text = "${(skill.level * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary
                )
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Use the newer overload that accepts progress as a lambda to avoid deprecation warnings
        LinearProgressIndicator(
            progress = { skill.level },
            modifier = Modifier.fillMaxWidth(),
            color = Primary,
            trackColor = Primary.copy(alpha = 0.2f)
        )
    }
}

// Dialogs para agregar nueva información
@Composable
fun AddExperienceDialog(
    onDismiss: () -> Unit,
    onAdd: (Experience) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var period by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón de atrás visible
                Row(
                    modifier = Modifier
                        .clickable { onDismiss() }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        modifier = Modifier.size(20.dp),
                        tint = Primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Atrás",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Primary
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text("Agregar Experiencia")
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Cargo") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Empresa") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = period,
                    onValueChange = { period = it },
                    label = { Text("Período (ej: 2020 - 2022)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank() && company.isNotBlank()) {
                        onAdd(Experience(title, company, period, description))
                    }
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun AddEducationDialog(
    onDismiss: () -> Unit,
    onAdd: (Education) -> Unit
) {
    var degree by remember { mutableStateOf("") }
    var institution by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón de atrás visible
                Row(
                    modifier = Modifier
                        .clickable { onDismiss() }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        modifier = Modifier.size(20.dp),
                        tint = Primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Atrás",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Primary
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text("Agregar Educación")
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = degree,
                    onValueChange = { degree = it },
                    label = { Text("Título/Grado") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = institution,
                    onValueChange = { institution = it },
                    label = { Text("Institución") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it },
                    label = { Text("Año (ej: 2018 - 2022)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (degree.isNotBlank() && institution.isNotBlank()) {
                        onAdd(Education(degree, institution, year, description))
                    }
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun AddSkillDialog(
    onDismiss: () -> Unit,
    onAdd: (Skill) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var level by remember { mutableFloatStateOf(0.5f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón de atrás visible
                Row(
                    modifier = Modifier
                        .clickable { onDismiss() }
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás",
                        modifier = Modifier.size(20.dp),
                        tint = Primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Atrás",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Primary
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text("Agregar Habilidad")
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la habilidad") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Nivel: ${(level * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
                Slider(
                    value = level,
                    onValueChange = { level = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(Skill(name, level))
                    }
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
