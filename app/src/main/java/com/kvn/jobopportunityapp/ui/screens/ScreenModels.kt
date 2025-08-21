package com.kvn.jobopportunityapp.ui.screens

// Data model stubs

import androidx.compose.ui.graphics.vector.ImageVector

data class ServiceItem(val id: Int = 0, val title: String = "", val icon: ImageVector? = null, val price: String = "", val details: String = "")
data class ProductItem(val id: Int = 0, val title: String = "", val icon: ImageVector? = null, val price: String = "", val details: String = "")
data class VehicleItem(val id: Int = 0, val title: String = "", val icon: ImageVector? = null, val price: String = "", val condition: String = "")
data class PropertyItem(val id: Int = 0, val title: String = "", val icon: ImageVector? = null, val price: String = "", val details: String = "")

data class CourseItem(
	val id: Int = 0,
	val title: String = "",
	val instructor: String = "",
	val price: String = "",
	val duration: String = "",
	val level: String = "",
	val rating: Float = 0f,
	val color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
	val icon: ImageVector? = null
)
