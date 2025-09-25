package com.kvn.jobopportunityapp.ui.screens

import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import android.content.Context
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.views.overlay.MapEventsOverlay

private fun parseLatLngFromUrl(url: String?): Pair<Double, Double>? {
    if (url.isNullOrBlank()) return null
    // supports formats: https://maps.google.com/?q=lat,lng  or geo:lat,lng
    val q = when {
        url.startsWith("geo:") -> url.removePrefix("geo:")
        url.contains("?q=") -> url.substringAfter("?q=")
        else -> url
    }
    val parts = q.split(",")
    return if (parts.size >= 2) parts[0].toDoubleOrNull()?.let { lat ->
        parts[1].toDoubleOrNull()?.let { lng -> lat to lng }
    } else null
}

@Composable
fun MapPreview(latLngUrl: String?, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx -> createMap(ctx, latLngUrl) },
        update = { map ->
            // no-op on update for now
        }
    )
}

private fun createMap(context: Context, latLngUrl: String?): MapView {
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
    Configuration.getInstance().userAgentValue = context.packageName
    val map = MapView(context)
    map.setTileSource(TileSourceFactory.MAPNIK)
    map.setMultiTouchControls(true)
    val (lat, lng) = parseLatLngFromUrl(latLngUrl) ?: (2.444814 to -76.614739) // Popayán por defecto
    val point = GeoPoint(lat, lng)
    map.controller.setZoom(15.0)
    map.controller.setCenter(point)
    val marker = Marker(map)
    marker.position = point
    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
    map.overlays.add(marker)
    return map
}

@Composable
fun MapPicker(
    initialUrl: String? = null,
    modifier: Modifier = Modifier,
    onPicked: (lat: Double, lng: Double, geoUrl: String) -> Unit,
    onCancel: () -> Unit
) {
    val selected = remember {
        val (lat, lng) = parseLatLngFromUrl(initialUrl) ?: (2.444814 to -76.614739)
        mutableStateOf(GeoPoint(lat, lng))
    }
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
                Configuration.getInstance().userAgentValue = ctx.packageName
                val map = MapView(ctx)
                map.setTileSource(TileSourceFactory.MAPNIK)
                map.setMultiTouchControls(true)
                val start = selected.value
                map.controller.setZoom(15.0)
                map.controller.setCenter(start)
                val marker = Marker(map).apply {
                    position = start
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Ubicación seleccionada"
                    isDraggable = true
                    setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                        override fun onMarkerDragEnd(marker: Marker?) {
                            marker?.position?.let { p -> selected.value = p }
                        }
                        override fun onMarkerDrag(marker: Marker?) {}
                        override fun onMarkerDragStart(marker: Marker?) {}
                    })
                }
                map.overlays.add(marker)

                val events = object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                        p ?: return false
                        selected.value = p
                        map.controller.setCenter(p)
                        return true
                    }
                    override fun longPressHelper(p: GeoPoint?): Boolean = false
                }
                map.overlays.add(0, MapEventsOverlay(events))
                map
            },
            update = { map ->
                // Reposicionar marker si cambió el estado
                val m = map.overlays.filterIsInstance<Marker>().firstOrNull()
                val p = selected.value
                if (m != null && m.position != p) {
                    m.position = p
                    map.invalidate()
                }
            }
        )
        Surface(tonalElevation = 4.dp, shadowElevation = 8.dp, color = Color.White, modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()) {
            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Lat: %.5f  Lng: %.5f".format(selected.value.latitude, selected.value.longitude), style = MaterialTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onCancel) { Text("Cancelar") }
                    Button(onClick = {
                        val p = selected.value
                        val url = "geo:${p.latitude},${p.longitude}"
                        onPicked(p.latitude, p.longitude, url)
                    }) { Text("Confirmar") }
                }
            }
        }
    }
}
