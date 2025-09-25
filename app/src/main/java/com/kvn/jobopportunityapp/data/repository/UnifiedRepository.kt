package com.kvn.jobopportunityapp.data.repository

import com.kvn.jobopportunityapp.data.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UnifiedRepository(private val api: ApiService) {
    private suspend inline fun <T> safeList(crossinline block: suspend () -> Response<List<T>>): RepoResult<List<T>> {
        return withContext(Dispatchers.IO) {
            try {
                val r = block()
                if (r.isSuccessful) {
                    RepoResult.Success(r.body().orEmpty())
                } else {
                    RepoResult.Error(r.errorBody()?.string() ?: "Error ${r.code()}", r.code())
                }
            } catch (e: Exception) {
                RepoResult.Error(e.message ?: "Error de red")
            }
        }
    }

    // Collections
    suspend fun jobOffers(): RepoResult<List<JobOfferDto>> = safeList { api.getJobOffers() }
    suspend fun classifieds(): RepoResult<List<ClassifiedDto>> = safeList { api.getClassifieds() }
    suspend fun trainings(): RepoResult<List<TrainingDto>> = safeList { api.getTrainings() }
    suspend fun messages(): RepoResult<List<MessageDto>> = safeList { api.getMessages() }
    suspend fun notifications(): RepoResult<List<NotificationDto>> = safeList { api.getNotifications() }
    suspend fun companies(): RepoResult<List<CompanyDto>> = safeList { api.getCompanies() }
    suspend fun favorites(): RepoResult<List<FavoriteDto>> = safeList { api.getFavorites() }
    suspend fun users(): RepoResult<List<BasicUserDto>> = safeList { api.getUsers() }
    suspend fun categories(): RepoResult<List<CategoryDto>> = safeList { api.getCategories() }
    suspend fun comments(): RepoResult<List<CommentDto>> = safeList { api.getComments() }
    suspend fun unemployed(): RepoResult<List<UnemployedDto>> = safeList { api.getUnemployed() }
    suspend fun trainingUsers(): RepoResult<List<TrainingUserDto>> = safeList { api.getTrainingUsers() }
    suspend fun jobApplications(): RepoResult<List<JobApplicationDto>> = safeList { api.getJobApplications() }
    suspend fun portfolio(): RepoResult<List<PortfolioDto>> = safeList { api.getPortfolio() }
}
