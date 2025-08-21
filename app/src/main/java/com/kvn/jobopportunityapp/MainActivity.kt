package com.kvn.jobopportunityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.lerp
import com.kvn.jobopportunityapp.data.AppPreferences
import com.kvn.jobopportunityapp.data.UserType
import com.kvn.jobopportunityapp.ui.components.JobOpportunityBottomBar
import com.kvn.jobopportunityapp.ui.components.JobOpportunityTopBar
import com.kvn.jobopportunityapp.ui.components.PublishOptionsDialog
import com.kvn.jobopportunityapp.ui.screens.*
import com.kvn.jobopportunityapp.ui.theme.JobOpportunityAPPTheme

enum class AppRoute { AUTH_LOGIN, AUTH_REGISTER, MAIN }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val appPreferences = remember { AppPreferences(context) }
            
            JobOpportunityAPPTheme(
                darkTheme = appPreferences.isDarkTheme
            ) {
                JobOpportunityApp(appPreferences = appPreferences)
            }
        }
    }
}

@Composable
fun JobOpportunityApp(appPreferences: AppPreferences) {
    var selectedBottomNavIndex by remember { mutableIntStateOf(0) }
    // single overlay state: null | "profile" | "settings" | "history" | ...
    var overlayState by remember { mutableStateOf<String?>(null) }
    var showPublishDialog by remember { mutableStateOf(false) }

    var isAuthenticated by remember { mutableStateOf(appPreferences.isLoggedIn) }
    var currentUserType by remember { mutableStateOf(appPreferences.userType) }

    PublishOptionsDialog(
        userType = currentUserType,
        isVisible = showPublishDialog,
        onDismiss = { showPublishDialog = false },
        onOptionSelected = { route ->
            // TODO: Navigate to the corresponding publish screen
        }
    )

    // Simple in-memory navigation for auth vs main flow
    // Toggle to force showing the auth screen to diagnose startup flicker/blank screen.
    // Set to true temporarily if you want to force the Login screen on startup.
    val forceShowLogin = false
    var route by remember { mutableStateOf(
        if (!forceShowLogin && isAuthenticated) AppRoute.MAIN else AppRoute.AUTH_LOGIN
    ) }

    when (route) {
        AppRoute.AUTH_LOGIN -> {
            LoginScreen(
                onLoginSuccess = { userType, email ->
                    currentUserType = userType
                    isAuthenticated = true
                    appPreferences.saveUserSession(
                        userType = userType,
                        email = email,
                        name = email.substringBefore("@").replaceFirstChar { it.uppercaseChar() }
                    )
                    route = AppRoute.MAIN
                },
                onNavigateToRegister = {
                    route = AppRoute.AUTH_REGISTER
                }
            )
        }
        AppRoute.AUTH_REGISTER -> {
            RegisterScreen(
                onRegisterSuccess = { userType, name, email ->
                    // Save session and go to main
                    currentUserType = userType
                    isAuthenticated = true
                    appPreferences.saveUserSession(
                        userType = userType,
                        email = email,
                        name = name
                    )
                    route = AppRoute.MAIN
                },
                onNavigateToLogin = {
                    route = AppRoute.AUTH_LOGIN
                }
            )
        }
        AppRoute.MAIN -> {
            // continue to the main scaffold below
        }
    }

    // shared progress drives header/content shared animation (0f = main, 1f = overlay)
    val targetProgress = if (overlayState == null) 0f else 1f
    val sharedProgress by animateFloatAsState(targetValue = targetProgress, animationSpec = tween(durationMillis = 360))

    // Only render main UI when in MAIN route
    if (route == AppRoute.MAIN) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            topBar = {
                JobOpportunityTopBar(
                    onProfileClick = { overlayState = "profile" },
                    sharedProgress = sharedProgress
                )
            },
            bottomBar = {
                JobOpportunityBottomBar(
                    selectedIndex = selectedBottomNavIndex,
                    userType = currentUserType,
                    onItemSelected = { index ->
                        selectedBottomNavIndex = index
                    },
                    onPublishClick = {
                        showPublishDialog = true
                    }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AnimatedContent(
                    targetState = selectedBottomNavIndex,
                    transitionSpec = {
                        // Uniform vertical animation for all bottom tabs
                        slideInVertically(initialOffsetY = { fullHeight -> fullHeight / 3 }, animationSpec = tween(360, easing = EaseOutCubic)) +
                                fadeIn(animationSpec = tween(300)) togetherWith
                                slideOutVertically(targetOffsetY = { fullHeight -> -fullHeight / 3 }, animationSpec = tween(300, easing = EaseInCubic)) +
                                fadeOut(animationSpec = tween(260))
                    },
                    label = "BottomNavAnimation"
                ) { index ->
                    when (index) {
                        0 -> HomeScreen()
                        1 -> JobsScreen()
                        2 -> ClassifiedScreen()
                        3 -> TrainingScreen()
                    }
                }
            }
        }
    }

    // Overlay stack handled by overlayState; animate transitions between overlays
    AnimatedContent(
        targetState = overlayState,
        transitionSpec = {
            val isBackToProfile = (targetState == "profile")
            if (isBackToProfile) {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth / 3 }, animationSpec = tween(280)) + fadeIn(animationSpec = tween(220)) togetherWith
                        slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut(animationSpec = tween(200))
            } else {
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn(animationSpec = tween(220)) togetherWith
                        slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth / 3 }) + fadeOut(animationSpec = tween(180))
            }
        },
        label = "OverlayTransitions"
    ) { state ->
        when (state) {
            "profile" -> {
                ProfileScreen(
                    onBackClick = { overlayState = null },
                    onSettingsClick = { overlayState = "settings" },
                    onLogout = {
                        appPreferences.clearUserSession()
                        isAuthenticated = false
                        route = AppRoute.AUTH_LOGIN
                        overlayState = null
                    },
                    onNavigate = { key -> overlayState = key },
                    sharedProgress = sharedProgress
                )
            }
            "settings" -> {
                SettingsScreen(appPreferences = appPreferences, onBackClick = { overlayState = "profile" }, onDeleteAccount = {
                    appPreferences.clearUserSession()
                    isAuthenticated = false
                    route = AppRoute.AUTH_LOGIN
                    overlayState = null
                }, onLogout = {
                    appPreferences.clearUserSession()
                    isAuthenticated = false
                    route = AppRoute.AUTH_LOGIN
                    overlayState = null
                })
            }
            "history" -> { HistoryScreen(onBack = { overlayState = "profile" }, sharedProgress = sharedProgress) }
            "contacts" -> { ContactsScreen(onBack = { overlayState = "profile" }, sharedProgress = sharedProgress) }
            "messages" -> { MessagesScreen(onBack = { overlayState = "profile" }, sharedProgress = sharedProgress) }
            "saved" -> { SavedScreen(onBack = { overlayState = "profile" }, sharedProgress = sharedProgress) }
            "alerts" -> { AlertsScreen(onBack = { overlayState = "profile" }, sharedProgress = sharedProgress) }
            "availability" -> { AvailabilityScreen(onBack = { overlayState = "profile" }, sharedProgress = sharedProgress) }
            "followed_companies" -> { FollowedCompaniesScreen(onBack = { overlayState = "profile" }, sharedProgress = sharedProgress) }
            "integrations" -> { IntegrationsScreen(onBack = { overlayState = "profile" }, sharedProgress = sharedProgress) }
            "referrals" -> { ReferralsScreen(onBack = { overlayState = "profile" }, sharedProgress = sharedProgress) }
            "feedback_history" -> { FeedbackHistoryScreen(onBack = { overlayState = "profile" }, sharedProgress = sharedProgress) }
            "billing" -> { BillingScreen(onBack = { overlayState = "profile" }, sharedProgress = sharedProgress) }
            "preferences_personal" -> { PreferencesPersonalScreen(onBack = { overlayState = "profile" }, sharedProgress = sharedProgress) }
            else -> { /* no overlay */ }
        }
    }
}