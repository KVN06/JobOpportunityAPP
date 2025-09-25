package com.kvn.jobopportunityapp.data.repository

import com.kvn.jobopportunityapp.data.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

sealed class RepoResult<out T> {
    data class Success<T>(val data: T): RepoResult<T>()
    data class Error(val message: String, val code: Int? = null): RepoResult<Nothing>()
}

class AuthRepository(private val api: ApiService) {

    suspend fun login(email: String, password: String): RepoResult<AuthResponse> = safe {
        api.login(LoginRequest(email, password))
    }

    suspend fun register(fullName: String, email: String, password: String, userType: String): RepoResult<AuthResponse> = safe {
        api.register(RegisterRequest(fullName, email, password, userType))
    }

    suspend fun profile(): RepoResult<AuthResponse> = safe { api.getProfile() }

    private suspend inline fun <T> safe(crossinline block: suspend () -> Response<T>): RepoResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                val r = block()
                if (r.isSuccessful) {
                    val body = r.body()
                    if (body != null) RepoResult.Success(body) else RepoResult.Error("Respuesta vacía", r.code())
                } else {
                    RepoResult.Error(r.errorBody()?.string() ?: "Error ${r.code()}", r.code())
                }
            } catch (e: Exception) {
                RepoResult.Error(e.message ?: "Error de red")
            }
        }
    }
}
