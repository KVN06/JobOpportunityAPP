package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kvn.jobopportunityapp.data.UserType
import com.kvn.jobopportunityapp.ui.theme.*

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween

@Composable
fun AuthScreen(
    onLoginSuccess: (UserType, String, String) -> Unit
) {
    var currentScreen by remember { mutableStateOf("login") } // "login" o "register"
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
        when (currentScreen) {
            "login" -> {
                LoginScreen(
                    onLoginSuccess = { userType, email ->
                        val name = email.substringBefore("@").replaceFirstChar { it.uppercaseChar() }
                        onLoginSuccess(userType, email, name)
                    },
                    onNavigateToRegister = { currentScreen = "register" }
                )
            }
            "register" -> {
                RegisterScreen(
                    onRegisterSuccess = onLoginSuccess,
                    onNavigateToLogin = { currentScreen = "login" }
                )
            }
        }
    }
}
