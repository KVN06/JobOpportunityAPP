package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kvn.jobopportunityapp.ui.theme.*
import com.kvn.jobopportunityapp.ui.navigation.LocalOverlayNavigator
import com.kvn.jobopportunityapp.ui.navigation.LocalMapPickerResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.Alignment

@Composable
fun PublishJobScreen(onCancel: () -> Unit, onPublished: () -> Unit) {
    val openOverlay = LocalOverlayNavigator.current
    val mapPickerResult = LocalMapPickerResult.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var geo by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf("") }
    var titleErr by remember { mutableStateOf(false) }
    var descErr by remember { mutableStateOf(false) }
    var salaryErr by remember { mutableStateOf(false) }
    LaunchedEffect(mapPickerResult?.value) {
        mapPickerResult?.value?.let { picked ->
            geo = picked
            mapPickerResult.value = null
        }
    }
    // Recibir resultado del map picker a través de un efecto que lee del parámetro de overlay param cuando vuelve
    // Estrategia simple: si geo está vacío o queremos reemplazar, ofrecer botón para abrir picker y al volver, si overlayParam se ha llenado en MainActivity como mapPickerResult, necesitamos una vía para leerlo aquí.
    // Resolución: si el usuario regresa desde el picker, MainActivity cierra overlay y no hay un bus global. Usaremos un truco: pasaremos el valor actual del result por LocalOverlayNavigator como una apertura con route "map_picker" y este Composable solo actualizará el campo cuando onPicked cierre el overlay y recompose. Para facilitar, añadimos un pequeño botón que abre el picker y no necesitamos observar nada adicional porque no hay back-stack conservado. El resultado se pierde.

    PublishFormScaffold(
        header = "Publicar Empleo",
        onCancel = onCancel,
        onPublish = {
            titleErr = title.isBlank()
            descErr = description.isBlank()
            salaryErr = salary.isBlank()
            if (titleErr || descErr || salaryErr) return@PublishFormScaffold
            onPublished()
        }
    ) {
        TextFieldRow("Título", title, isError = titleErr) { title = it; if (titleErr && it.isNotBlank()) titleErr = false }
        TextAreaRow("Descripción", description, isError = descErr) { description = it; if (descErr && it.isNotBlank()) descErr = false }
        TextFieldRow("Salario", salary, keyboardType = KeyboardType.Decimal, isError = salaryErr) { salary = it; if (salaryErr && it.isNotBlank()) salaryErr = false }
        TextFieldRow("Ubicación", location) { location = it }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { openOverlay?.invoke("map_picker", null) }) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Primary)
                Spacer(Modifier.width(8.dp))
                Text("Elegir ubicación en mapa")
            }
        }
        Spacer(Modifier.height(8.dp))
        TextFieldRow("Geolocalización (URL Maps)", geo) { geo = it }
        CategoryChipsInput(categories, onChange = { categories = it })
    }
}

@Composable
fun PublishClassifiedScreen(onCancel: () -> Unit, onPublished: () -> Unit) {
    val openOverlay = LocalOverlayNavigator.current
    val mapPickerResult = LocalMapPickerResult.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var geo by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf("") }
    var titleErr by remember { mutableStateOf(false) }
    var descErr by remember { mutableStateOf(false) }
    var priceErr by remember { mutableStateOf(false) }
    LaunchedEffect(mapPickerResult?.value) {
        mapPickerResult?.value?.let { picked ->
            geo = picked
            mapPickerResult.value = null
        }
    }

    PublishFormScaffold(
        header = "Publicar Clasificado",
        onCancel = onCancel,
        onPublish = {
            titleErr = title.isBlank()
            descErr = description.isBlank()
            priceErr = price.isBlank()
            if (titleErr || descErr || priceErr) return@PublishFormScaffold
            onPublished()
        }
    ) {
        TextFieldRow("Título", title, isError = titleErr) { title = it; if (titleErr && it.isNotBlank()) titleErr = false }
        TextAreaRow("Descripción", description, isError = descErr) { description = it; if (descErr && it.isNotBlank()) descErr = false }
        TextFieldRow("Precio", price, keyboardType = KeyboardType.Decimal, isError = priceErr) { price = it; if (priceErr && it.isNotBlank()) priceErr = false }
        TextFieldRow("Ubicación", location) { location = it }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { openOverlay?.invoke("map_picker", null) }) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Primary)
                Spacer(Modifier.width(8.dp))
                Text("Elegir ubicación en mapa")
            }
        }
        Spacer(Modifier.height(8.dp))
        TextFieldRow("Geolocalización (URL Maps)", geo) { geo = it }
        CategoryChipsInput(categories, onChange = { categories = it })
    }
}

@Composable
fun PublishTrainingScreen(onCancel: () -> Unit, onPublished: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var provider by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    var titleErr by remember { mutableStateOf(false) }
    var providerErr by remember { mutableStateOf(false) }
    var startErr by remember { mutableStateOf(false) }
    var endErr by remember { mutableStateOf(false) }

    PublishFormScaffold(
        header = "Publicar Capacitación",
        onCancel = onCancel,
        onPublish = {
            titleErr = title.isBlank()
            providerErr = provider.isBlank()
            startErr = !isValidDate(start)
            endErr = !isValidDate(end)
            if (titleErr || providerErr || startErr || endErr) return@PublishFormScaffold
            onPublished()
        }
    ) {
        TextFieldRow("Título", title, isError = titleErr) { title = it; if (titleErr && it.isNotBlank()) titleErr = false }
        TextFieldRow("Proveedor", provider, isError = providerErr) { provider = it; if (providerErr && it.isNotBlank()) providerErr = false }
        TextAreaRow("Descripción", description) { description = it }
        TextFieldRow("Enlace", link) { link = it }
        TextFieldRow("Fecha de inicio (YYYY-MM-DD)", start, isError = startErr) { start = it; if (startErr) startErr = !isValidDate(it) }
        TextFieldRow("Fecha de fin (YYYY-MM-DD)", end, isError = endErr) { end = it; if (endErr) endErr = !isValidDate(it) }
    }
}

@Composable
private fun PublishFormScaffold(
    header: String,
    onCancel: () -> Unit,
    onPublish: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Text(
            text = header,
            style = MaterialTheme.typography.headlineSmall,
            color = TextPrimary
        )
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 12.dp)
        ) {
            content()
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Cancelar") }
            Button(
                onClick = onPublish,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) { Text("Publicar", color = Color.White) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextFieldRow(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        isError = isError,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary,
            unfocusedBorderColor = BorderColor,
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextAreaRow(
    label: String,
    value: String,
    isError: Boolean = false,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        isError = isError,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Primary,
            unfocusedBorderColor = BorderColor,
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground
        )
    )
}

private fun splitCategories(raw: String): List<String> = raw.split(',').map { it.trim() }.filter { it.isNotEmpty() }

@Composable
private fun CategoryChipsInput(value: String, onChange: (String) -> Unit) {
    var input by remember { mutableStateOf("") }
    val items = remember(value) { splitCategories(value) }
    Column(Modifier.fillMaxWidth()) {
        // Chips actuales
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            items.forEach { c ->
                AssistChip(
                    onClick = {},
                    label = { Text(c) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Eliminar",
                            tint = TextSecondary,
                            modifier = Modifier.clickable {
                                val updated = items.filter { it != c }
                                onChange(updated.joinToString(", "))
                            }
                        )
                    }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        // Añadir nueva categoría
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Añadir categoría") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = BorderColor,
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground
                ),
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                val t = input.trim()
                if (t.isNotEmpty() && !items.contains(t)) {
                    val updated = items.toMutableList().apply { add(t) }
                    onChange(updated.joinToString(", "))
                }
                input = ""
            }, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
                Text("Agregar", color = Color.White)
            }
        }
    }
}

private fun isValidDate(s: String): Boolean {
    val parts = s.split("-")
    if (parts.size != 3) return false
    val (y, m, d) = parts
    val yi = y.toIntOrNull() ?: return false
    val mi = m.toIntOrNull() ?: return false
    val di = d.toIntOrNull() ?: return false
    if (y.length != 4 || m.length != 2 || d.length != 2) return false
    if (yi < 2000 || mi !in 1..12 || di !in 1..31) return false
    return true
}
