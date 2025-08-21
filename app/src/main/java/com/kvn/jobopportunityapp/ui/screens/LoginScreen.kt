package com.kvn.jobopportunityapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.kvn.jobopportunityapp.data.UserType
import com.kvn.jobopportunityapp.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: (UserType, String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Función de login con datos quemados
    fun attemptLogin() {
        isLoading = true
        showError = false

        // Simular delay de red usando el scope recordado (no crear scopes ad-hoc)
        coroutineScope.launch {
            delay(1000)

            // Datos quemados para login
            when {
                // Usuario cesante
                (email == "cesante@test.com" || email == "usuario@test.com" || email.isNotBlank()) &&
                        (password == "123456" || password == "password" || password.isNotBlank()) -> {
                    onLoginSuccess(UserType.CESANTE, email)
                }
                // Usuario empresa  
                (email == "empresa@test.com" || email == "admin@test.com") &&
                        (password == "123456" || password == "admin" || password.isNotBlank()) -> {
                    onLoginSuccess(UserType.EMPRESA, email)
                }
                // Permitir cualquier credencial válida
                email.isNotBlank() && password.isNotBlank() -> {
                    // Por defecto, iniciar como cesante
                    onLoginSuccess(UserType.CESANTE, email)
                }
                else -> {
                    showError = true
                    errorMessage = "Por favor, ingresa email y contraseña"
                }
            }
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GradientStart,
                        GradientEnd
                    )
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

    Spacer(modifier = Modifier.height(48.dp))
        // Hero logo block (bigger, circular, with subtle shadow)
        // subtle appearance animation for the whole column
        val appear = remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { appear.value = true }
        val appearAlpha by animateFloatAsState(targetValue = if (appear.value) 1f else 0f, animationSpec = tween(durationMillis = 480))

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(brush = Brush.linearGradient(listOf(GradientStart, GradientEnd))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "JO",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Bienvenido a Job Opportunity",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Encuentra oportunidades relevantes para tu perfil",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Tarjeta de login (aparece con alpha)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(appearAlpha),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // decorative gradient stripe
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(brush = Brush.horizontalGradient(listOf(GradientStart, GradientEnd)))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Accede a tu cuenta para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Campo de email
                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        showError = false
                    },
                    label = { Text("Correo Electrónico") },
                    placeholder = { Text("ejemplo@correo.com") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email"
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary,
                        focusedLeadingIconColor = Primary,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Campo de contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { 
                        password = it
                        showError = false
                    },
                    label = { Text("Contraseña") },
                    placeholder = { Text("••••••••") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Contraseña"
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary,
                        focusedLeadingIconColor = Primary,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Mensaje de error
                if (showError) {
                    Text(
                        text = errorMessage,
                        color = Error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // ¿Olvidaste tu contraseña?
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = Primary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.clickable {
                            // TODO: Implementar recuperación de contraseña
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Botón de iniciar sesión
                Button(
                    onClick = { attemptLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(14.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "INICIAR SESIÓN",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }
                }
                
                // tighten spacing between CTA and register link
                Spacer(modifier = Modifier.height(12.dp))

                // Link a registro
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿No tienes cuenta? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = "Regístrate aquí",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        ),
                        color = Primary,
                        modifier = Modifier.clickable {
                            onNavigateToRegister()
                        }
                    )
                }
            }
        }
    }
}
