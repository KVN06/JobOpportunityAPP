package com.kvn.jobopportunityapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Shared UI-level ViewModel to broadcast user-facing info (e.g., avatar) across screens.
 */
class SharedUserViewModel : ViewModel() {
    private val _avatarUri = MutableStateFlow<String?>(null)
    val avatarUri: StateFlow<String?> = _avatarUri.asStateFlow()

    fun setAvatarUri(uri: String?) {
        _avatarUri.value = uri
    }
}
