package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.kvn.jobopportunityapp.ui.components.CategoryTabs
import com.kvn.jobopportunityapp.ui.components.ServicesGrid
import com.kvn.jobopportunityapp.ui.components.ProductsGrid
import com.kvn.jobopportunityapp.ui.components.VehiclesGrid
import com.kvn.jobopportunityapp.ui.components.PropertiesGrid
@Composable
fun ClassifiedScreen() {
	// Example layout for classified screen
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(Color.White)
			.padding(top = 20.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
	) {
		CategoryTabs(selectedCategory = "Servicios", onCategorySelected = {})
		Spacer(modifier = Modifier.height(16.dp))
		ServicesGrid()
		Spacer(modifier = Modifier.height(16.dp))
		ProductsGrid()
		Spacer(modifier = Modifier.height(16.dp))
		VehiclesGrid()
		Spacer(modifier = Modifier.height(16.dp))
		PropertiesGrid()
	}
}
// Removed imports for grid composables; all grid composables are now local


