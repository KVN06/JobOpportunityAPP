package com.kvn.jobopportunityapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvn.jobopportunityapp.data.AppPreferences
import com.kvn.jobopportunityapp.data.network.ServiceLocator
import com.kvn.jobopportunityapp.data.repository.RepoResult
import com.kvn.jobopportunityapp.data.repository.UnifiedRepository
import com.kvn.jobopportunityapp.domain.model.*
import com.kvn.jobopportunityapp.data.mapper.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClassifiedsRemoteViewModel(
    private val prefs: AppPreferences,
    private val remote: UnifiedRepository = UnifiedRepository(ServiceLocator.api(prefs))
) : ViewModel() {
    private val _items = MutableStateFlow<List<ClassifiedPost>>(emptyList())
    val items: StateFlow<List<ClassifiedPost>> = _items.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { load() }

    fun load(force: Boolean = false) {
        if (_items.value.isNotEmpty() && !force) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            when (val r = remote.classifieds()) {
                is RepoResult.Success<*> -> _items.value = (r.data as List<com.kvn.jobopportunityapp.data.network.ClassifiedDto>).map { it.toClassifiedPost() }
                is RepoResult.Error -> _error.value = r.message
            }
            _loading.value = false
        }
    }
}

class MessagesViewModel(
    private val prefs: AppPreferences,
    private val remote: UnifiedRepository = UnifiedRepository(ServiceLocator.api(prefs))
) : ViewModel() {
    private val _items = MutableStateFlow<List<MessageItem>>(emptyList())
    val items: StateFlow<List<MessageItem>> = _items.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { load() }

    fun load(force: Boolean = false) {
        if (_items.value.isNotEmpty() && !force) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            when (val r = remote.messages()) {
                is RepoResult.Success<*> -> _items.value = (r.data as List<com.kvn.jobopportunityapp.data.network.MessageDto>).map { it.toMessageItem() }
                is RepoResult.Error -> _error.value = r.message
            }
            _loading.value = false
        }
    }
}

class ContactsViewModel(
    private val prefs: AppPreferences,
    private val remote: UnifiedRepository = UnifiedRepository(ServiceLocator.api(prefs))
) : ViewModel() {
    private val _items = MutableStateFlow<List<ContactItem>>(emptyList())
    val items: StateFlow<List<ContactItem>> = _items.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { load() }

    fun load(force: Boolean = false) {
        if (_items.value.isNotEmpty() && !force) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            when (val r = remote.users()) {
                is RepoResult.Success<*> -> _items.value = (r.data as List<com.kvn.jobopportunityapp.data.network.BasicUserDto>).map { it.toContactItem() }
                is RepoResult.Error -> _error.value = r.message
            }
            _loading.value = false
        }
    }
}

class AlertsViewModel(
    private val prefs: AppPreferences,
    private val remote: UnifiedRepository = UnifiedRepository(ServiceLocator.api(prefs))
) : ViewModel() {
    private val _items = MutableStateFlow<List<AlertItem>>(emptyList())
    val items: StateFlow<List<AlertItem>> = _items.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { load() }

    fun load(force: Boolean = false) {
        if (_items.value.isNotEmpty() && !force) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            when (val r = remote.notifications()) {
                is RepoResult.Success<*> -> _items.value = (r.data as List<com.kvn.jobopportunityapp.data.network.NotificationDto>).map { it.toAlertItem() }
                is RepoResult.Error -> _error.value = r.message
            }
            _loading.value = false
        }
    }
}

class FollowedCompaniesViewModel(
    private val prefs: AppPreferences,
    private val remote: UnifiedRepository = UnifiedRepository(ServiceLocator.api(prefs))
) : ViewModel() {
    private val _items = MutableStateFlow<List<CompanyUpdate>>(emptyList())
    val items: StateFlow<List<CompanyUpdate>> = _items.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { load() }

    fun load(force: Boolean = false) {
        if (_items.value.isNotEmpty() && !force) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            when (val r = remote.companies()) {
                is RepoResult.Success<*> -> _items.value = (r.data as List<com.kvn.jobopportunityapp.data.network.CompanyDto>).map { it.toCompanyUpdate() }
                is RepoResult.Error -> _error.value = r.message
            }
            _loading.value = false
        }
    }
}

class TrainingsRemoteViewModel(
    private val prefs: AppPreferences,
    private val remote: UnifiedRepository = UnifiedRepository(ServiceLocator.api(prefs))
) : ViewModel() {
    private val _items = MutableStateFlow<List<TrainingPost>>(emptyList())
    val items: StateFlow<List<TrainingPost>> = _items.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { load() }

    fun load(force: Boolean = false) {
        if (_items.value.isNotEmpty() && !force) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            when (val r = remote.trainings()) {
                is RepoResult.Success<*> -> _items.value = (r.data as List<com.kvn.jobopportunityapp.data.network.TrainingDto>).map { it.toTrainingPost() }
                is RepoResult.Error -> _error.value = r.message
            }
            _loading.value = false
        }
    }
}

class FavoritesRemoteViewModel(
    private val prefs: AppPreferences,
    private val remote: UnifiedRepository = UnifiedRepository(ServiceLocator.api(prefs))
) : ViewModel() {
    private val _items = MutableStateFlow<List<SavedItem>>(emptyList())
    val items: StateFlow<List<SavedItem>> = _items.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { load() }

    fun load(force: Boolean = false) {
        if (_items.value.isNotEmpty() && !force) return
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            when (val r = remote.favorites()) {
                is RepoResult.Success<*> -> _items.value = (r.data as List<com.kvn.jobopportunityapp.data.network.FavoriteDto>).map { it.toSavedItem() }
                is RepoResult.Error -> _error.value = r.message
            }
            _loading.value = false
        }
    }
}
// Las siguientes pantallas estaban basadas en datos ficticios. Para cumplir "solo API",
// retiramos sus repos fake. Si se necesitan, deben implementarse contra endpoints reales.
