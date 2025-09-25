package com.kvn.jobopportunityapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvn.jobopportunityapp.data.AppPreferences
import com.kvn.jobopportunityapp.data.network.ServiceLocator
import com.kvn.jobopportunityapp.data.network.JobOfferDto
import com.kvn.jobopportunityapp.data.repository.UnifiedRepository
import com.kvn.jobopportunityapp.data.repository.RepoResult
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

sealed class JobsUiState {
    object Idle: JobsUiState()
    object Loading: JobsUiState()
    data class Error(val message: String): JobsUiState()
    data class Data(val jobs: List<JobOfferDto>): JobsUiState()
}

class JobsViewModel(private val prefs: AppPreferences): ViewModel() {
    private val repo = UnifiedRepository(ServiceLocator.api(prefs))

    private val _state = mutableStateOf<JobsUiState>(JobsUiState.Idle)
    val state: State<JobsUiState> = _state

    fun loadJobs(force: Boolean = false) {
        if (_state.value is JobsUiState.Data && !force) return
        _state.value = JobsUiState.Loading
        viewModelScope.launch {
            when (val r = repo.jobOffers()) {
                is RepoResult.Success -> {
                    if (r.data.isEmpty()) _state.value = JobsUiState.Data(emptyList()) else _state.value = JobsUiState.Data(r.data)
                }
                is RepoResult.Error -> _state.value = JobsUiState.Error(r.message)
            }
        }
    }
}
