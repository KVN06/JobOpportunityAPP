package com.kvn.jobopportunityapp.ui.screens

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.DpOffset
import kotlinx.coroutines.delay
import android.graphics.BitmapFactory
import org.json.JSONArray
import androidx.compose.ui.window.Dialog
import com.kvn.jobopportunityapp.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable

/**
 * Clean ProfileScreen: gradient header, overlapping avatar (editable), persisted avatar, centered menu,
 * safe image decoding, editable user info and improved icon mapping.
 */

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    onCVClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    sharedProgress: Float = 1f
) {
    val context = LocalContext.current
    val prefs: SharedPreferences = context.getSharedPreferences("job_opportunity_prefs", Context.MODE_PRIVATE)
    val appPreferences = remember { com.kvn.jobopportunityapp.data.AppPreferences(context) }
    // Shared VM to broadcast avatar changes
    val sharedUserViewModel: com.kvn.jobopportunityapp.ui.viewmodel.SharedUserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    // persisted user info
    var userName by remember { mutableStateOf(prefs.getString("user_name", "Vibefks Tech") ?: "Vibefks Tech") }
    var userRole by remember { mutableStateOf(prefs.getString("user_role", "Desarrollador del APP") ?: "Desarrollador del APP") }
    var userEmail by remember { mutableStateOf(prefs.getString("user_email", "contacto@vibefkstech.com") ?: "contacto@vibefkstech.com") }

    // avatar state
    var avatarUri by remember { mutableStateOf(prefs.getString("avatar_uri", "")?.ifBlank { null }) }
    var avatarBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var userPickedAvatar by remember { mutableStateOf(false) }

    // cropper state (declare before usage in imagePicker)
    var showCropper by remember { mutableStateOf(false) }
    var pendingCropUri by remember { mutableStateOf<String?>(null) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            // Persist read permission so the URI works after app restarts
            try {
                context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (_: Throwable) { /* ignore if already granted */ }
            val s = it.toString()
            // Open cropper before persisting so user can adjust like WhatsApp
            pendingCropUri = s
            showCropper = true
        }
    }

    // menu & dialogs
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showSecurityDialog by remember { mutableStateOf(false) }
    var failedUri by remember { mutableStateOf<String?>(null) }
    var showEditUserDialog by remember { mutableStateOf(false) }
    var showHeaderMenu by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }
    // future editable sections
    var showEditExperience by remember { mutableStateOf(false) }
    var showEditEducation by remember { mutableStateOf(false) }
    var showEditSkills by remember { mutableStateOf(false) }
    var showEditPreferences by remember { mutableStateOf(false) }

    // settings toggles persisted
    var themeDark by remember { mutableStateOf(prefs.getBoolean("theme_dark", false)) }
    var pushNotif by remember { mutableStateOf(prefs.getBoolean("notif_push", true)) }
    var jobsNotif by remember { mutableStateOf(prefs.getBoolean("notif_jobs", true)) }
    var trainingNotif by remember { mutableStateOf(prefs.getBoolean("notif_training", true)) }
    var emailNotif by remember { mutableStateOf(prefs.getBoolean("notif_email", false)) }
    var biometricAuth by remember { mutableStateOf(prefs.getBoolean("auth_biometric", false)) }
    var locationAllowed by remember { mutableStateOf(prefs.getBoolean("allow_location", true)) }

    // persisted list sections (stored as JSON arrays in prefs)
    fun readList(key: String): List<String> {
        val raw = prefs.getString(key, null) ?: return emptyList()
        return try {
            val a = JSONArray(raw)
            List(a.length()) { i -> a.optString(i) }
        } catch (_: Throwable) { emptyList() }
    }

    fun saveList(key: String, list: List<String>) {
        val a = JSONArray()
        list.forEach { a.put(it) }
        prefs.edit().putString(key, a.toString()).apply()
    }

    // Simple local model for recent applications stored as JSON in prefs
    data class ApplicationItem(val id: String, val title: String, val company: String, val date: String, val status: String)

    var experienceList by remember { mutableStateOf(readList("profile_experience")) }
    var educationList by remember { mutableStateOf(readList("profile_education")) }
    var skillsList by remember { mutableStateOf(readList("profile_skills")) }
    var preferencesList by remember { mutableStateOf(readList("profile_preferences")) }

    val listState = rememberLazyListState()
    val headerHeight = 260.dp
    // tuned sizes for cleaner styling to match reference image
    val avatarSize = 140.dp
    val avatarOverlapAdjust = 6.dp
    val avatarOuterPadding = 6.dp
    // keep the small avatar large enough so the edit badge (pencil) remains visible
    val smallAvatarSize = 100.dp

    val scrolled by remember(listState) {
        derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 }
    }

        // outer ring extra radius used for safe paddings
        val outerRingExtra = 14.dp

        // Create a continuous scroll fraction (0..1) driven by list scroll offset so
        // the avatar smoothly animates size and vertical position in a subtle way.
        val density = LocalDensity.current
    // maximum scroll (pixels) to reach full effect; increase to reduce movement sensitivity
    val maxScrollForEffectPx = with(density) { 150.dp.toPx() }

        // Use derivedStateOf with remember to avoid creating state during composition
        val currentScrollPx by remember(listState, maxScrollForEffectPx) {
            derivedStateOf {
                if (listState.firstVisibleItemIndex > 0) maxScrollForEffectPx
                else listState.firstVisibleItemScrollOffset.toFloat().coerceAtLeast(0f)
            }
        }

        val rawFraction by remember(currentScrollPx, maxScrollForEffectPx) {
            derivedStateOf {
                (currentScrollPx / maxScrollForEffectPx).coerceIn(0f, 1f)
            }
        }

    // Use raw fraction driven by scroll (0..1)
    val animFraction = rawFraction

    // animate a single fraction with a spring to drive both scale and Y movement
    val animatedFraction by animateFloatAsState(
        targetValue = animFraction,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow)
    )

    // derive avatar size from the animated fraction (smooth because fraction is spring-animated)
    val avatarSizeAnimated = with(density) {
        val fullPx = avatarSize.toPx()
        val smallPx = smallAvatarSize.toPx()
        ((fullPx - (fullPx - smallPx) * animatedFraction)).toDp()
    }

    // non-scrolled position (avatar overlaps header & content)
    val initialLift = 12.dp
    // base non-scrolled Y on the full avatar size so when the avatar shrinks the
    // scrolled target will be higher (net upward movement)
    val nonScrolledY = headerHeight - (avatarSize / 2) - initialLift

    // Reduced upward lift (dp) when scrolled so the avatar doesn't move too far up
    val scrolledLift = 55.dp

    // Interpolate by subtracting a small lift from the non-scrolled Y; this keeps
    // the final position closer to the original and avoids covering the username.
    // target Y derived from the animated fraction so movement matches the size animation
    val avatarY = nonScrolledY - (scrolledLift * animatedFraction)

    // avatarY already defined above; duplicated declaration removed

    // Defer heavy avatar image work slightly so first frames don't stutter on slow devices.
    var loadAvatarDelayed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        // small delay to let the initial composition/layout settle
        delay(300L)
        loadAvatarDelayed = true
        // Push current persisted avatar to shared VM so top bar shows it
    // sync from preferences/AppPreferences in case of previous value
    val persisted = appPreferences.avatarUri.ifBlank { avatarUri }
    sharedUserViewModel.setAvatarUri(persisted)
    }

    // we still keep avatarBitmap only for potential fallbacks; decode off the IO dispatcher
    LaunchedEffect(avatarUri, loadAvatarDelayed) {
        avatarBitmap = null
        if (!avatarUri.isNullOrBlank() && loadAvatarDelayed) {
            try {
                avatarBitmap = withContext(Dispatchers.IO) {
                    val stream = context.contentResolver.openInputStream(Uri.parse(avatarUri))
                    stream?.use { BitmapFactory.decodeStream(it) }
                }
            } catch (_: Throwable) {
                avatarBitmap = null
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        // compute top padding in place: avatarY + avatar radius + small gap
        val topPadding = avatarY + (avatarSizeAnimated / 2) + 8.dp

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = topPadding, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Empty spacer to preserve visual spacing without showing header text
            item {
                Spacer(modifier = Modifier.fillMaxWidth().height(72.dp))
            }

            // Profile info card with extra top margin so it doesn't stick to the top
            item {
                Spacer(modifier = Modifier.height(8.dp))
                ProfileInfoCard(name = userName, role = userRole, email = userEmail, onEditClick = { showEditUserDialog = true })
            }

            item {
                // Progress card: align with HomeScreen statistics
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = CardBackground)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        Text(
                            text = "Tu Progreso",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                            StatBlock(icon = Icons.AutoMirrored.Filled.Assignment, label = "Postulaciones", value = "6")
                            Spacer(modifier = Modifier.width(8.dp))
                            StatBlock(icon = Icons.Default.Event, label = "Entrevistas", value = "2")
                            Spacer(modifier = Modifier.width(8.dp))
                            StatBlock(icon = Icons.Default.CheckCircle, label = "Respuestas", value = "4")
                        }
                    }
                }
            }

            // Portfolio / CV
            item {
                PortfolioCard(
                    experienceCount = experienceList.size,
                    educationCount = educationList.size,
                    skillsCount = skillsList.size,
                    preferencesCount = preferencesList.size,
                    onEditExperience = { showEditExperience = true },
                    onEditEducation = { showEditEducation = true },
                    onEditSkills = { showEditSkills = true },
                    onEditPreferences = { showEditPreferences = true }
                )
            }

            // Aplicaciones recientes (máx. 3)
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // read recent applications from prefs (JSON array of objects)
                val rawApps = prefs.getString("recent_applications", null)
                val recentApps = remember(rawApps) {
                    try {
                        if (rawApps.isNullOrBlank()) emptyList()
                        else {
                            val arr = JSONArray(rawApps)
                            List(arr.length()) { i ->
                                val o = arr.optJSONObject(i)
                                ApplicationItem(
                                    id = o?.optString("id") ?: i.toString(),
                                    title = o?.optString("title") ?: "Oferta #${i + 1}",
                                    company = o?.optString("company") ?: "Empresa desconocida",
                                    date = o?.optString("date") ?: "--",
                                    status = o?.optString("status") ?: "En revisión"
                                )
                            }
                        }
                    } catch (_: Throwable) { emptyList() }
                }

                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Aplicaciones recientes", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Últimas postulaciones (máx. 3)", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }

                        TextButton(onClick = { onNavigate("applications") }) { Text(text = "Ver todas") }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (recentApps.isEmpty()) {
                        Card(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)), colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F7F9))) {
                            Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Aún no has aplicado a ofertas", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = "Cuando apliques, aparecerán aquí las más recientes.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                }

                                TextButton(onClick = { onNavigate("jobs") }) { Text(text = "Explorar") }
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            recentApps.take(3).forEach { app ->
                                Card(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable { onNavigate("application/${app.id}") }, colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                                    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = app.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(text = app.company + " • " + app.date, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                        }

                                        // status chip
                                        Surface(shape = RoundedCornerShape(12.dp), color = when (app.status.lowercase()) {
                                            "aprobada", "aceptada", "contratado" -> Color(0xFFE8F5E9)
                                            "rechazada", "no seleccionada" -> Color(0xFFFFEBEE)
                                            else -> Color(0xFFFFF8E1)
                                        }) {
                                            Text(text = app.status, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = TextPrimary)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Single expandable 'Secciones' card: shows options as individual cards with "Ver más"
            item {
                Spacer(modifier = Modifier.height(6.dp))
                val items = listOf(
                    Triple("Historial de actividad", "Registros de tus postulaciones y acciones", "history"),
                    Triple("Contactos profesionales", "Tu red y referencias", "contacts"),
                    Triple("Mensajes", "Conversaciones con reclutadores", "messages"),
                    Triple("Guardados", "Ofertas y recursos guardados", "saved"),
                    Triple("Alertas de empleo", "Criterios y alertas activas", "alerts"),
                    Triple("Disponibilidad", "Horarios y franjas para entrevistas", "availability"),
                    Triple("Empresas seguidas", "Novedades de empresas que sigues", "followed_companies"),
                    Triple("Integraciones", "Vincula LinkedIn, GitHub u otras", "integrations"),
                    Triple("Referidos e invitaciones", "Invita a alguien o gestiona referidos", "referrals"),
                    Triple("Historial de feedback", "Feedback enviado y respuestas", "feedback_history"),
                    Triple("Pagos y suscripciones", "Planes, facturación y recibos", "billing"),
                    Triple("Preferencias personales", "Preferencias específicas del usuario", "preferences_personal")
                )

                // Inline expandable card for 'Secciones' to avoid cross-file reference issues
                var sectionsExpanded by remember { mutableStateOf(false) }
                Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clip(RoundedCornerShape(14.dp)), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Secciones", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Accede a las secciones relacionadas con tu perfil", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }

                            IconButton(onClick = { sectionsExpanded = !sectionsExpanded }) {
                                Icon(imageVector = if (sectionsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = if (sectionsExpanded) "Cerrar" else "Abrir")
                            }
                        }

                            // Non-animated expand/collapse to improve performance on slow devices
                            if (sectionsExpanded) {
                            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items.forEach { (t, s, key) ->
                                    Card(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)), colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FB)), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(text = t, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(text = s, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                            }

                                            TextButton(onClick = { onNavigate(key) }) { Text(text = "Ver más") }
                                        }
                                    }
                                }
                                }
                            }
                    }
                }
            }

                // Account actions: eliminar cuenta + cerrar sesión (destacado)
                item {
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        Spacer(modifier = Modifier.height(8.dp))
                        // Full-width rectangular logout action styled as a card-like button
                        Card(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable { showLogoutConfirm = true }, colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Cerrar sesión", color = Color(0xFFD32F2F), style = MaterialTheme.typography.titleMedium)
                                    Text(text = "Salir de tu cuenta", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }

        // Header gradient (improved): background below the avatar; actions drawn on top
        Box(modifier = Modifier.fillMaxWidth().height(headerHeight).background(brush = Brush.linearGradient(listOf(GradientStart, GradientEnd))).statusBarsPadding().zIndex(1f)) {
                        // Row with actions (drawn above avatar)
                        // center vertically so title aligns with avatar circle visually
                        // add a little extra top padding so the icons/text aren't glued to the status bar
                        Row(modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = 14.dp).zIndex(6f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                // back button with subtle background for contrast
                Surface(shape = CircleShape, color = Color.Black.copy(alpha = 0.12f), modifier = Modifier.size(44.dp)) {
                    IconButton(onClick = { onBackClick() }, modifier = Modifier.size(44.dp)) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                }

                    // title centered vertically
                    // keep the title fixed; overlay the username (no layout shift) using alpha animation
                    Box(contentAlignment = Alignment.Center) {
                        // slightly larger title so header reads stronger
                        Text(text = "Job Opportunity", color = Color.White, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                        // Instant name+role alpha (no animation) to avoid costly animations
                        val nameAlpha = if (scrolled) 1f else 0f
                        // show "Nombre | Rol" in the header when scrolled; truncate if too long
                        Text(
                            text = "$userName | $userRole",
                            color = Color.White.copy(alpha = nameAlpha),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.align(Alignment.BottomCenter).offset(y = 20.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                // three-dots menu with background
                Box {
                    Surface(shape = CircleShape, color = Color.Black.copy(alpha = 0.12f), modifier = Modifier.size(44.dp)) {
                        IconButton(onClick = { showHeaderMenu = true }, modifier = Modifier.size(44.dp)) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Más opciones", tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                    }

                    DropdownMenu(expanded = showHeaderMenu, onDismissRequest = { showHeaderMenu = false }) {
                        DropdownMenuItem(text = { Text("Editar perfil") }, onClick = { showHeaderMenu = false; showEditUserDialog = true })
                        DropdownMenuItem(text = { Text("Configuración") }, onClick = { showHeaderMenu = false; onSettingsClick() })
                        DropdownMenuItem(text = { Text("Compartir perfil") }, onClick = { showHeaderMenu = false /* future: share action */ })
                    }
                }
            }
        }

    // Avatar overlapping and clickable (drawn above header background but below action icons)
    Box(modifier = Modifier.fillMaxWidth().zIndex(4f), contentAlignment = Alignment.TopCenter) {
        Box(modifier = Modifier.offset(y = avatarY), contentAlignment = Alignment.Center) {
            // outer dark stroke ring (thicker & darker)
            Box(modifier = Modifier
                .size(avatarSizeAnimated + 28.dp)
                .clip(CircleShape)
                .border(BorderStroke(8.dp, Color(0xFF1F2B2F)), CircleShape))

            // white ring (larger gap)
            Box(modifier = Modifier
                .size(avatarSizeAnimated + 16.dp)
                .clip(CircleShape)
                .background(Color.White))

            // inner avatar circle (clickable)
            Surface(
                modifier = Modifier
                    .size(avatarSizeAnimated)
                    .clip(CircleShape)
                    .clickable { imagePicker.launch(arrayOf("image/*")) },
                shape = CircleShape,
                color = Color.Transparent,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (avatarUri != null) {
                        // Use a painter so we can react to loading/error states and avoid blank placeholders
                        val painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(Uri.parse(avatarUri))
                                .crossfade(true)
                                .size(Size.ORIGINAL)
                                .build()
                        )

                        when (painter.state) {
                            is coil.compose.AsyncImagePainter.State.Success -> {
                                Image(painter = painter, contentDescription = "Avatar", modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = androidx.compose.ui.layout.ContentScale.Crop)
                            }
                            else -> {
                                // while loading or on error, fall back to bitmap if present, otherwise initials
                                if (avatarBitmap != null) {
                                    Image(bitmap = avatarBitmap!!.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = androidx.compose.ui.layout.ContentScale.Crop)
                                } else {
                                    // initials fallback: take up to two initials from the user's name
                                    val initials = remember(userName) {
                                        userName.split(" ").filter { it.isNotBlank() }.mapNotNull { it.trim().firstOrNull()?.uppercaseChar() }
                                            .take(2).joinToString(separator = "")
                                    }

                                    // default person avatar (gray circle with person icon)
                                    Box(modifier = Modifier.fillMaxSize().clip(CircleShape).background(Color(0xFFEEEEEE)), contentAlignment = Alignment.Center) {
                                        Icon(imageVector = Icons.Default.Person, contentDescription = "Avatar por defecto", tint = Color(0xFF757575), modifier = Modifier.size(48.dp))
                                    }
                                }
                            }
                        }
                    } else if (avatarBitmap != null) {
                        Image(bitmap = avatarBitmap!!.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = androidx.compose.ui.layout.ContentScale.Crop)
                    } else {
                        // No image available — show a neutral gray person icon instead of initials
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFFEEEEEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar por defecto",
                                tint = Color(0xFF757575),
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }

            // edit badge (larger, offset to match image)
            Box(modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-10).dp, y = (-12).dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(Primary)
                .clickable { showMenu = true }, contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar foto", tint = Color.White, modifier = Modifier.size(20.dp))
            }

            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }, offset = DpOffset(x = (-8).dp, y = 12.dp)) {
                DropdownMenuItem(text = { Text("Cambiar foto") }, leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) }, onClick = { showMenu = false; imagePicker.launch(arrayOf("image/*")) })

                if (avatarUri != null) {
                    DropdownMenuItem(text = { Text("Eliminar foto") }, leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Error) }, onClick = { showMenu = false; showDeleteConfirm = true })
                }

                DropdownMenuItem(text = { Text("Ver perfil público") }, leadingIcon = { Icon(Icons.Default.Visibility, contentDescription = null) }, onClick = { showMenu = false /* future */ })
                DropdownMenuItem(text = { Text("Compartir perfil") }, leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) }, onClick = { showMenu = false /* future */ })
                DropdownMenuItem(text = { Text("Ajustes de privacidad") }, leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }, onClick = { showMenu = false /* future */ })
            }
        }
    }



        // Delete confirmation dialog (constrained)
        if (showDeleteConfirm) {
            Dialog(onDismissRequest = { showDeleteConfirm = false }) {
                Card(modifier = Modifier.widthIn(max = 340.dp).wrapContentHeight().padding(8.dp), shape = RoundedCornerShape(14.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Eliminar foto de perfil", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "¿Estás seguro que deseas eliminar la foto de perfil?", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(modifier = Modifier.fillMaxWidth(), onClick = { showDeleteConfirm = false }) { Text("Cancelar") }
                            TextButton(modifier = Modifier.fillMaxWidth(), onClick = {
                                prefs.edit().remove("avatar_uri").apply(); avatarUri = null; showDeleteConfirm = false
                            }) { Text("Eliminar") }
                        }
                    }
                }
            }
        }

        // Security dialog shown only when user just picked an image and it fails to decode/access
        if (showSecurityDialog) {
            Dialog(onDismissRequest = { showSecurityDialog = false }) {
                Card(modifier = Modifier.widthIn(max = 340.dp).wrapContentHeight().padding(8.dp), shape = RoundedCornerShape(14.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Error al cargar la imagen", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "No se puede acceder a la imagen seleccionada. ¿Deseas intentarlo de nuevo o eliminar la referencia?", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.textButtonColors(contentColor = Error), onClick = {
                                failedUri?.let { prefs.edit().remove("avatar_uri").apply() }
                                avatarUri = null; showSecurityDialog = false
                            }) { Text("Eliminar referencia") }
                            TextButton(modifier = Modifier.fillMaxWidth(), onClick = { showSecurityDialog = false; imagePicker.launch(arrayOf("image/*")) }) { Text("Reintentar") }
                            TextButton(modifier = Modifier.fillMaxWidth(), onClick = { showSecurityDialog = false }) { Text("Cerrar") }
                        }
                    }
                }
            }
        }

        // Edit user dialog
        if (showEditUserDialog) {
            EditUserDialog(currentName = userName, currentRole = userRole, currentEmail = userEmail, onDismiss = { showEditUserDialog = false }, onSave = { n, r, e ->
                prefs.edit().putString("user_name", n).putString("user_role", r).putString("user_email", e).apply(); userName = n; userRole = r; userEmail = e; showEditUserDialog = false
            })
        }

        // Edit list dialogs for portfolio sections
        if (showEditExperience) {
            EditListDialog(title = "Editar Experiencia", items = experienceList, onDismiss = { showEditExperience = false }, onSave = {
                experienceList = it; saveList("profile_experience", it)
            })
        }

        if (showEditEducation) {
            EditListDialog(title = "Editar Educación", items = educationList, onDismiss = { showEditEducation = false }, onSave = {
                educationList = it; saveList("profile_education", it)
            })
        }

        if (showEditSkills) {
            EditListDialog(title = "Editar Habilidades", items = skillsList, onDismiss = { showEditSkills = false }, onSave = {
                skillsList = it; saveList("profile_skills", it)
            })
        }

        if (showEditPreferences) {
            EditListDialog(title = "Editar Preferencias", items = preferencesList, onDismiss = { showEditPreferences = false }, onSave = {
                preferencesList = it; saveList("profile_preferences", it)
            })
        }

        // Logout confirmation dialog
        if (showLogoutConfirm) {
            Dialog(onDismissRequest = { showLogoutConfirm = false }) {
                Card(modifier = Modifier.widthIn(max = 340.dp).wrapContentHeight().padding(8.dp), shape = RoundedCornerShape(14.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Cerrar sesión", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "¿Estás seguro que deseas cerrar sesión?", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { showLogoutConfirm = false }, modifier = Modifier.weight(1f)) { Text("Cancelar") }
                            TextButton(onClick = {
                                // clear simple persisted keys and call onLogout
                                prefs.edit().remove("auth_token").remove("avatar_uri").remove("user_name").remove("user_email").apply()
                                showLogoutConfirm = false
                                onLogout()
                            }, modifier = Modifier.weight(1f)) { Text("Cerrar sesión") }
                        }
                    }
                }
            }
        }
    }

    // Simple crop/adjust dialog: circle preview with zoom and offset; saves cropped bitmap
    if (showCropper && pendingCropUri != null) {
        CropAvatarDialog(
            imageUri = pendingCropUri!!,
            onDismiss = { showCropper = false; pendingCropUri = null },
            onSave = { cropped ->
                // Save to cache and persist URI
                val uri = saveBitmapToCache(context, cropped)
                prefs.edit().putString("avatar_uri", uri.toString()).apply()
                appPreferences.updateAvatarUri(uri.toString())
                avatarUri = uri.toString()
                // notify header
                sharedUserViewModel.setAvatarUri(uri.toString())
                showCropper = false
                pendingCropUri = null
            }
        )
    }
}

@Composable
fun SectionCard(title: String, subtitle: String, onEdit: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clip(RoundedCornerShape(12.dp)), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }

            TextButton(onClick = onEdit) { Text(text = "Editar") }
        }
    }
}

// Util: save bitmap to app cache and return content Uri via FileProvider-less approach using MediaStore for API 29+, or cache file Uri fallback
private fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    return try {
        // Prefer MediaStore on Android Q+
        val resolver = context.contentResolver
        val contentValues = android.content.ContentValues().apply {
            put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, "avatar_${System.currentTimeMillis()}.png")
            put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (android.os.Build.VERSION.SDK_INT >= 29) {
                put(android.provider.MediaStore.Images.Media.RELATIVE_PATH, "Pictures/JobOpportunity")
                put(android.provider.MediaStore.Images.Media.IS_PENDING, 1)
            }
        }
        val uri = resolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            resolver.openOutputStream(uri)?.use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            if (android.os.Build.VERSION.SDK_INT >= 29) {
                contentValues.clear()
                contentValues.put(android.provider.MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            }
            uri
        } else {
            // Fallback to cache
            val file = java.io.File(context.cacheDir, "avatar_${System.currentTimeMillis()}.png")
            java.io.FileOutputStream(file).use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
            Uri.fromFile(file)
        }
    } catch (_: Throwable) {
        // absolute fallback: cache
        val file = java.io.File(context.cacheDir, "avatar_${System.currentTimeMillis()}.png")
        java.io.FileOutputStream(file).use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
        Uri.fromFile(file)
    }
}

@Composable
private fun CropAvatarDialog(imageUri: String, onDismiss: () -> Unit, onSave: (Bitmap) -> Unit) {
    val context = LocalContext.current
    var zoom by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    // Load the bitmap at a reasonable size to avoid OOM
    var srcBitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(imageUri) {
        withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(Uri.parse(imageUri))?.use { stream ->
                    val decoded = BitmapFactory.decodeStream(stream)
                    // Limit max dimension to ~2048px to avoid huge memory
                    val maxDim = 2048
                    val w = decoded.width
                    val h = decoded.height
                    val scale = maxOf(1f, maxOf(w, h) / maxDim.toFloat())
                    val resized = if (scale > 1f) {
                        Bitmap.createScaledBitmap(decoded, (w / scale).toInt(), (h / scale).toInt(), true)
                    } else decoded
                    srcBitmap = resized
                }
            } catch (_: Throwable) {
                srcBitmap = null
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Ajustar foto", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(12.dp))

                val boxSize = 260.dp
                val density = LocalDensity.current
                // Tamaño en px de la caja de preview (círculo)
                val boxPx = with(density) { boxSize.toPx() }
                // Rango de pan permitido (depende del zoom actual). Evita dejar ver “huecos”.
                fun maxPan(zoomVal: Float): Float = ((boxPx * (zoomVal - 1f)) / 2f).coerceAtLeast(0f)
                // Asegura que los offsets estén siempre dentro del rango permitido con el zoom actual
                fun clampOffsets() {
                    val max = maxPan(zoom)
                    offsetX = offsetX.coerceIn(-max, max)
                    offsetY = offsetY.coerceIn(-max, max)
                }
                Box(
                    modifier = Modifier.size(boxSize).clip(CircleShape).background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    if (srcBitmap != null) {
                        // Preview with zoom/offset; we use AsyncImagePainter for URI, but here bitmap is fine
                        Image(
                            bitmap = srcBitmap!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .transformable(
                                    state = rememberTransformableState { zoomChange, panChange, _ ->
                                        // Actualiza zoom dentro de límites y re-clampa offsets
                                        zoom = (zoom * zoomChange).coerceIn(1f, 4f)
                                        val max = maxPan(zoom)
                                        offsetX = (offsetX + panChange.x).coerceIn(-max, max)
                                        offsetY = (offsetY + panChange.y).coerceIn(-max, max)
                                    }
                                )
                                .graphicsLayer {
                                    // Apply zoom and translation
                                    scaleX = zoom
                                    scaleY = zoom
                                    translationX = offsetX
                                    translationY = offsetY
                                },
                            // Dejamos Crop para el baseline (rellenar caja) y luego aplicamos zoom/pan por capa
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                // Controls: simple sliders to mimic pinch/drag quickly
                Text("Zoom (también puedes pellizcar la imagen)")
                Slider(
                    value = zoom,
                    onValueChange = { newZ ->
                        zoom = newZ.coerceIn(1f, 4f)
                        clampOffsets()
                    },
                    valueRange = 1f..4f
                )
                Spacer(Modifier.height(8.dp))
                Text("Horizontal")
                run {
                    val max = maxPan(zoom)
                    Slider(value = offsetX, onValueChange = { offsetX = it.coerceIn(-max, max) }, valueRange = -max..max)
                }
                Spacer(Modifier.height(8.dp))
                Text("Vertical")
                run {
                    val max = maxPan(zoom)
                    Slider(value = offsetY, onValueChange = { offsetY = it.coerceIn(-max, max) }, valueRange = -max..max)
                }

                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Button(onClick = {
                        // Render cropped circle from current zoom/offset
                        val src = srcBitmap ?: return@Button
                        val outSize = 512 // output square size
                        val output = Bitmap.createBitmap(outSize, outSize, Bitmap.Config.ARGB_8888)
                        val canvas = android.graphics.Canvas(output)
                        val paint = android.graphics.Paint().apply { isAntiAlias = true }

                        // Draw circle clip
                        val path = android.graphics.Path().apply {
                            addCircle(outSize / 2f, outSize / 2f, outSize / 2f, android.graphics.Path.Direction.CW)
                        }
                        canvas.save()
                        canvas.clipPath(path)

                        // Compute source draw matrix based on zoom and offsets
                        val matrix = android.graphics.Matrix()
                        // Center-crop baseline
                        val scale = maxOf(outSize.toFloat() / src.width, outSize.toFloat() / src.height) * zoom
                        matrix.postScale(scale, scale)
                        // Mapeo consistente de offsets del preview (boxPx) al bitmap final (outSize)
                        val offsetScale = if (boxPx > 0f) outSize / boxPx else 1f
                        val dx = (outSize - src.width * scale) / 2f + offsetX * offsetScale
                        val dy = (outSize - src.height * scale) / 2f + offsetY * offsetScale
                        matrix.postTranslate(dx, dy)

                        canvas.drawBitmap(src, matrix, paint)
                        canvas.restore()
                        onSave(output)
                    }) { Text("Guardar") }
                }
            }
        }
    }
}

@Composable
fun PlaceholderEditDialog(title: String, description: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 360.dp).padding(8.dp), shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Cerrar") }
            }
        }
    }
}

@Composable
fun ProfileInfoCard(name: String, role: String, email: String, onEditClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(0.94f).clip(RoundedCornerShape(16.dp)), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(12.dp)).background(brush = Brush.linearGradient(listOf(GradientStart, GradientEnd))), contentAlignment = Alignment.Center) {
                val r = role.lowercase()
                val roleIcon = when {
                    r.contains("empresa") || r.contains("company") || r.contains("empleador") || r.contains("reclut") -> Icons.Default.Business
                    r.contains("cesante") || r.contains("desemple") || r.contains("desocup") || r.contains("unemploy") -> Icons.Default.PersonOff
                    r.contains("desarroll") || r.contains("developer") || r.contains("dev") -> Icons.Default.Build
                    r.contains("admin") || r.contains("reclutador") -> Icons.Default.Business
                    else -> Icons.Default.Person
                }
                Icon(imageVector = roleIcon, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = role, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = email, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }

            IconButton(onClick = onEditClick, modifier = Modifier.size(40.dp)) {
                Surface(shape = CircleShape, color = Primary.copy(alpha = 0.12f)) { Box(contentAlignment = Alignment.Center, modifier = Modifier.size(40.dp)) { Icon(Icons.Default.Edit, contentDescription = "Editar perfil", tint = Primary) } }
            }
        }
    }
}

@Composable
fun EditUserDialog(currentName: String, currentRole: String, currentEmail: String, onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf(currentName) }
    var role by remember { mutableStateOf(currentRole) }
    var email by remember { mutableStateOf(currentEmail) }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Editar información") }, text = {
        Column {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = role, onValueChange = { role = it }, label = { Text("Rol") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
        }
    }, confirmButton = { TextButton(onClick = { onSave(name.trim(), role.trim(), email.trim()) }) { Text("Guardar") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } })
}

@Composable
fun StatBlock(icon: ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = label, tint = Primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = value, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
    }
}

// Portfolio / CV card with quick access to subsections
@Composable
fun PortfolioCard(
    experienceCount: Int,
    educationCount: Int,
    skillsCount: Int,
    preferencesCount: Int,
    onEditExperience: () -> Unit,
    onEditEducation: () -> Unit,
    onEditSkills: () -> Unit,
    onEditPreferences: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clip(RoundedCornerShape(14.dp)), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Portafolio / CV", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Secciones editables para tu perfil y configuración", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                TextButton(onClick = { /* ver CV completo - future */ }) { Text("Ver CV") }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SmallSection(title = "Experiencia", count = experienceCount, onEdit = onEditExperience, icon = Icons.Default.Work)
                SmallSection(title = "Educación", count = educationCount, onEdit = onEditEducation, icon = Icons.Default.School)
                SmallSection(title = "Habilidades", count = skillsCount, onEdit = onEditSkills, icon = Icons.Default.Star)
                SmallSection(title = "Preferencias", count = preferencesCount, onEdit = onEditPreferences, icon = Icons.Default.Settings)
            }
        }
    }
}

@Composable
fun SmallSection(title: String, count: Int, onEdit: () -> Unit, icon: ImageVector? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onEdit() }) {
        Surface(shape = RoundedCornerShape(10.dp), color = Primary.copy(alpha = 0.08f), modifier = Modifier.size(64.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                if (icon != null) Icon(imageVector = icon, contentDescription = null, tint = Primary, modifier = Modifier.size(28.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
        Text(text = "$count", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
    }
}

@Composable
fun EditListDialog(title: String, items: List<String>, onDismiss: () -> Unit, onSave: (List<String>) -> Unit) {
    val list = remember {
        mutableStateListOf<String>().also { it.addAll(items) }
    }
    var newItem by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 380.dp).padding(8.dp), shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    list.forEachIndexed { idx, it ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Text(text = it, modifier = Modifier.weight(1f))
                            TextButton(onClick = { list.removeAt(idx) }) { Text("Eliminar") }
                        }
                    }
                }

                OutlinedTextField(value = newItem, onValueChange = { newItem = it }, label = { Text("Nuevo") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { if (newItem.isNotBlank()) { list.add(newItem.trim()); newItem = "" } }) { Text("Añadir") }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Cancelar") }
                    TextButton(onClick = { onSave(list); onDismiss() }, modifier = Modifier.weight(1f)) { Text("Guardar") }
                }
            }
        }
    }
}


@Composable
fun SettingsRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(brush = Brush.linearGradient(listOf(GradientStart, GradientEnd))), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }

    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(20.dp))
    }
}


@Composable
fun SettingsToggleRow(icon: ImageVector, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(brush = Brush.linearGradient(listOf(GradientStart, GradientEnd))), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }

        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedThumbColor = Primary, uncheckedThumbColor = Color.LightGray))
    }
}


@Composable
fun SettingsActionRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit, actionColor: Color = Error) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(brush = Brush.linearGradient(listOf(GradientStart, GradientEnd))), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }

        Text(text = "", color = actionColor)
    }
}

@Composable
fun SmallSectionCard(title: String, subtitle: String, modifier: Modifier = Modifier, icon: ImageVector? = null, onClick: () -> Unit) {
    Card(modifier = modifier.clip(RoundedCornerShape(12.dp)).clickable { onClick() }, colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(brush = Brush.linearGradient(listOf(GradientStart, GradientEnd))), contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }

            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
        }
    }
}

