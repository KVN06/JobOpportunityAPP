package com.kvn.jobopportunityapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.kvn.jobopportunityapp.ui.theme.TextSecondary

// Nota: Este archivo solía contener más componentes para Training.
// Para evitar conflictos con funciones duplicadas en TrainingScreen.kt,
// mantenemos solo el chip reutilizable de información del curso.

@Composable
fun CourseInfoChip(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
	Surface(
		modifier = modifier,
		shape = RoundedCornerShape(10.dp),
		color = Color(0xFFF3F4F6)
	) {
		Row(
			modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(6.dp)
		) {
			Icon(icon, contentDescription = null, tint = TextSecondary)
			Text(
				text = text,
				style = MaterialTheme.typography.bodySmall,
				color = TextSecondary
			)
		}
	}
}
