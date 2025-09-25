package com.kvn.jobopportunityapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvn.jobopportunityapp.data.AppPreferences
import com.kvn.jobopportunityapp.data.UserType
import com.kvn.jobopportunityapp.data.network.ServiceLocator
import com.kvn.jobopportunityapp.data.repository.AuthRepository
import com.kvn.jobopportunityapp.data.repository.RepoResult
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(private val prefs: AppPreferences): ViewModel() {

    private val repo = AuthRepository(ServiceLocator.api(prefs))

    var uiState = mutableStateOf(AuthUiState())
        private set

    fun login(email: String, password: String, onSuccess: (UserType, String, String) -> Unit) {
        uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            when (val r = repo.login(email, password)) {
                is RepoResult.Success -> {
                    val user = r.data.user
                    prefs.saveAuthToken(r.data.token)
                    val type = runCatching { UserType.valueOf(user.userType.uppercase()) }.getOrDefault(UserType.CESANTE)
                    prefs.saveUserSession(type, user.email, user.name)
                    uiState.value = AuthUiState()
                    onSuccess(type, user.email, user.name)
                }
                is RepoResult.Error -> {
                    uiState.value = AuthUiState(error = r.message)
                }
            }
        }
    }

    fun register(fullName: String, email: String, password: String, userType: UserType, onSuccess: (UserType, String, String) -> Unit) {
        uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            when (val r = repo.register(fullName, email, password, userType.name.lowercase())) {
                is RepoResult.Success -> {
                    val user = r.data.user
                    prefs.saveAuthToken(r.data.token)
                    val type = runCatching { UserType.valueOf(user.userType.uppercase()) }.getOrDefault(userType)
                    prefs.saveUserSession(type, user.email, user.name)
                    uiState.value = AuthUiState()
                    onSuccess(type, user.email, user.name)
                }
                is RepoResult.Error -> {
                    uiState.value = AuthUiState(error = r.message)
                }
            }
        }
    }
}
