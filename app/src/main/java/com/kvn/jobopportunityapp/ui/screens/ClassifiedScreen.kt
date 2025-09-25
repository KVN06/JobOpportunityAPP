package com.kvn.jobopportunityapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kvn.jobopportunityapp.data.AppPreferences
import com.kvn.jobopportunityapp.domain.model.ClassifiedPost
import com.kvn.jobopportunityapp.ui.theme.Background
import com.kvn.jobopportunityapp.ui.theme.CardBackground
import com.kvn.jobopportunityapp.ui.theme.Error
import com.kvn.jobopportunityapp.ui.theme.Primary
import com.kvn.jobopportunityapp.ui.theme.TextPrimary
import com.kvn.jobopportunityapp.ui.theme.TextSecondary
import com.kvn.jobopportunityapp.ui.viewmodel.ClassifiedsRemoteViewModel

@Composable
fun ClassifiedScreen() {
	val context = LocalContext.current
	val prefs = remember { AppPreferences(context) }
	val vm = remember { ClassifiedsRemoteViewModel(prefs) }
	val items by vm.items.collectAsState()
	val loading by vm.loading.collectAsState()
	val error by vm.error.collectAsState()
	LaunchedEffect(Unit) { vm.load() }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(Background)
			.padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
	) {
		Text(
			"Clasificados",
			style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
			color = TextPrimary
		)
		Spacer(Modifier.height(12.dp))
		when {
			loading -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Primary) }
			error != null -> Text("Error: $error", color = Error)
			else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
				items(items) { item ->
					ClassifiedCard(post = item)
				}
				item { Spacer(Modifier.height(12.dp)) }
			}
		}
	}
}

@Composable
private fun ClassifiedCard(post: ClassifiedPost) {
	val context = LocalContext.current
	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(containerColor = CardBackground),
		shape = RoundedCornerShape(16.dp),
		elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
	) {
		Column(Modifier.padding(14.dp)) {
			// Header con icono y acciones
			Row(verticalAlignment = Alignment.CenterVertically) {
				Surface(
					modifier = Modifier.size(40.dp).clip(CircleShape),
					color = Color(0xFFEFF6FF)
				) {
					Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
						Icon(Icons.Filled.Storefront, contentDescription = null, tint = Primary)
					}
				}
				Spacer(Modifier.size(12.dp))
				Text(
					text = post.title,
					style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
					color = TextPrimary,
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					modifier = Modifier.weight(1f)
				)
				Row {
					IconButton(onClick = {
						val share = Intent(Intent.ACTION_SEND).apply {
							setType("text/plain")
							putExtra(Intent.EXTRA_TEXT, "${post.title} - ${post.price}\n${post.description}")
						}
						context.startActivity(Intent.createChooser(share, "Compartir clasificado"))
					}) {
						Icon(Icons.Filled.Share, contentDescription = "Compartir", tint = Primary)
					}
				}
			}

			Spacer(Modifier.height(8.dp))

			// Chips de información
			Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
				InfoChip(icon = Icons.Filled.MonetizationOn, text = post.price.ifBlank { "A convenir" })
				if (post.location.isNotBlank()) {
					InfoChip(icon = Icons.Filled.LocationOn, text = post.location, onClick = {
						val uri = Uri.parse("geo:0,0?q=" + Uri.encode(post.location))
						val mapIntent = Intent(Intent.ACTION_VIEW, uri)
						context.startActivity(mapIntent)
					})
				}
			}

			if (post.description.isNotBlank()) {
				Spacer(Modifier.height(10.dp))
				Text(
					text = post.description,
					style = MaterialTheme.typography.bodyMedium,
					color = TextSecondary,
					maxLines = 3,
					overflow = TextOverflow.Ellipsis
				)
			}
		}
	}
}

@Composable
private fun InfoChip(
	icon: androidx.compose.ui.graphics.vector.ImageVector,
	text: String,
	onClick: (() -> Unit)? = null
) {
	Surface(
		onClick = { onClick?.invoke() },
		enabled = onClick != null,
		shape = RoundedCornerShape(10.dp),
		color = Color(0xFFF3F4F6)
	) {
		Row(
			modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(6.dp)
		) {
			Icon(icon, contentDescription = null, tint = TextSecondary)
			Text(text = text, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
		}
	}
}


