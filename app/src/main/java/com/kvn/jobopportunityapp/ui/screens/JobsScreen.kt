package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kvn.jobopportunityapp.data.AppPreferences
import com.kvn.jobopportunityapp.ui.navigation.LocalOverlayNavigator
import com.kvn.jobopportunityapp.ui.theme.*
import com.kvn.jobopportunityapp.ui.viewmodel.JobsUiState
import com.kvn.jobopportunityapp.ui.viewmodel.JobsViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun JobsScreen() {
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val prefs = remember { AppPreferences(context) }
    val jobsVm = remember { JobsViewModel(prefs) }
    val jobsState by jobsVm.state
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { jobsVm.loadJobs() }

    val nav = LocalOverlayNavigator.current
    val swipeState = rememberSwipeRefreshState(isRefreshing = jobsState is JobsUiState.Loading)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        JobsSearchBar(
            searchText = searchText,
            onSearchTextChange = { searchText = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        FilterChips()

        Spacer(modifier = Modifier.height(20.dp))

        SwipeRefresh(state = swipeState, onRefresh = { jobsVm.loadJobs(force = true) }) {
            when (val s = jobsState) {
                is JobsUiState.Loading -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(6) { JobListItemSkeleton() }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }
                is JobsUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${s.message}", color = Error)
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { jobsVm.loadJobs(force = true) }) { Text("Reintentar") }
                        Spacer(Modifier.height(24.dp))
                    }
                }
                is JobsUiState.Data, JobsUiState.Idle -> {
                    val allJobs = (s as? JobsUiState.Data)?.jobs.orEmpty()
                    val remoteJobs = if (searchText.isBlank()) allJobs else allJobs.filter {
                        val q = searchText.trim().lowercase()
                        (it.title ?: "").lowercase().contains(q) ||
                        (it.companyName ?: it.companyNameAlt ?: "").lowercase().contains(q) ||
                        (it.location ?: it.city ?: "").lowercase().contains(q)
                    }

                    if (remoteJobs.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No hay resultados", color = TextSecondary)
                            Spacer(Modifier.height(8.dp))
                            if (searchText.isNotBlank()) {
                                Text("Prueba con otro término de búsqueda", color = TextSecondary)
                            }
                            Spacer(Modifier.height(24.dp))
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Empleos (${remoteJobs.size})",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary
                            )
                            TextButton(onClick = { jobsVm.loadJobs(force = true) }) { Text("Refrescar") }
                        }

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(remoteJobs) { job ->
                                JobListItem(
                                    company = job.companyName ?: job.companyNameAlt ?: "Empresa",
                                    position = job.title ?: "(Sin título)",
                                    location = job.location ?: job.city ?: "--",
                                    salary = job.salary ?: job.salaryRange ?: "--",
                                    type = "--",
                                    isNew = false,
                                    onClick = {
                                        val param = listOf(
                                            job.title ?: "Oferta",
                                            job.companyName ?: job.companyNameAlt ?: "",
                                            job.location ?: job.city ?: "",
                                            job.salary ?: job.salaryRange ?: "",
                                            job.description ?: ""
                                        ).joinToString("|")
                                        nav?.invoke("job_detail", param)
                                    },
                                    onApply = {
                                        val sp = context.getSharedPreferences("job_opportunity_prefs", android.content.Context.MODE_PRIVATE)
                                        val raw = sp.getString("recent_applications", null)
                                        val arr = try { if (raw.isNullOrBlank()) JSONArray() else JSONArray(raw) } catch (_: Throwable) { JSONArray() }
                                        val date = try { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) } catch (_: Throwable) { "" }
                                        val obj = org.json.JSONObject().apply {
                                            put("id", job.id)
                                            put("title", job.title ?: "Oferta")
                                            put("company", job.companyName ?: job.companyNameAlt ?: "Empresa")
                                            put("date", date)
                                            put("status", "En revisión")
                                        }
                                        arr.put(0, obj)
                                        while (arr.length() > 10) arr.remove(arr.length() - 1)
                                        sp.edit().putString("recent_applications", arr.toString()).apply()
                                        scope.launch {
                                            android.widget.Toast.makeText(context, "Postulación registrada", android.widget.Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                )
                            }
                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JobsSearchBar(
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
        TagFilterChip(text = "Todos", isSelected = true)
        TagFilterChip(text = "Remoto", isSelected = false)
        TagFilterChip(text = "Presencial", isSelected = false)
        TagFilterChip(text = "Híbrido", isSelected = false)
    }
}

@Composable
private fun TagFilterChip(text: String, isSelected: Boolean) {
    Surface(
        modifier = Modifier.clip(RoundedCornerShape(20.dp)),
        color = if (isSelected) Primary else SurfaceVariant,
        onClick = {}
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
    isNew: Boolean,
    onClick: () -> Unit = {},
    onApply: () -> Unit = {}
) {
    val context = LocalContext.current
    val saved = rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
                    IconButton(onClick = { saved.value = !saved.value }) {
                        Icon(
                            imageVector = if (saved.value) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Guardar",
                            tint = if (saved.value) Primary else TextSecondary
                        )
                    }
                    IconButton(onClick = {
                        val share = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                            setType("text/plain")
                            putExtra(android.content.Intent.EXTRA_TEXT, "$position - $company\n$location | $salary")
                        }
                        context.startActivity(android.content.Intent.createChooser(share, "Compartir oferta"))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir",
                            tint = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = {
                        if (location.isNotBlank()) {
                            val uri = android.net.Uri.parse("geo:0,0?q=" + android.net.Uri.encode(location))
                            val mapIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri)
                            context.startActivity(mapIntent)
                        }
                    },
                    label = { Text(location.ifBlank { "Ubicación no especificada" }) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                AssistChip(
                    onClick = {},
                    label = { Text(if (salary.isBlank()) "Salario N/D" else salary) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
                AssistChip(
                    onClick = {},
                    label = { Text(if (type.isBlank()) "Tipo N/D" else type) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onApply() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text(
                    "Postularse",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun JobListItemSkeleton() {
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
            Box(modifier = Modifier.fillMaxWidth(0.7f).height(20.dp).background(SurfaceVariant))
            Spacer(Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth(0.4f).height(16.dp).background(SurfaceVariant))
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(width = 90.dp, height = 28.dp).background(SurfaceVariant))
                Box(modifier = Modifier.size(width = 80.dp, height = 28.dp).background(SurfaceVariant))
                Box(modifier = Modifier.size(width = 70.dp, height = 28.dp).background(SurfaceVariant))
            }
            Spacer(Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth().height(40.dp).background(SurfaceVariant))
        }
    }
}
