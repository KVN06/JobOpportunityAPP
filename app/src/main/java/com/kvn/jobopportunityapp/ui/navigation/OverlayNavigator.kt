package com.kvn.jobopportunityapp.ui.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.MutableState

// Simple navigator to request an overlay route with an optional string parameter
typealias OverlayNav = (route: String, param: String?) -> Unit

val LocalOverlayNavigator = staticCompositionLocalOf<OverlayNav?> { null }
// Result bus for map picker (geo URL like "geo:lat,lng")
val LocalMapPickerResult = staticCompositionLocalOf<MutableState<String?>?> { null }

// Bottom bar navigator to switch selected tab by index
typealias BottomBarNav = (index: Int) -> Unit
val LocalBottomBarNavigator = staticCompositionLocalOf<BottomBarNav?> { null }
