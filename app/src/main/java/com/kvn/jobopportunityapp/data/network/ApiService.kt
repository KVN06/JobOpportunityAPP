package com.kvn.jobopportunityapp.data.network

import retrofit2.http.*
import retrofit2.Response
import com.google.gson.annotations.SerializedName

// DTOs básicos para autenticación y recursos iniciales

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val userType: String
)

data class UserDto(
    val id: String? = null,
    val name: String,
    val email: String,
    val userType: String
)

data class AuthResponse(
    val token: String,
    val user: UserDto
)

// Job Offer (Laravel job-offer)
data class JobOfferDto(
    val id: Int,
    val title: String?,
    val description: String?,
    val salary: String?,
    @SerializedName("salary_range") val salaryRange: String? = null,
    val location: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("company") val companyName: String? = null,
    @SerializedName("company_name") val companyNameAlt: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// Classified
data class ClassifiedDto(
    val id: Int,
    val title: String?,
    val description: String?,
    val price: String?,
    val location: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// Training
data class TrainingDto(
    val id: Int,
    val title: String?,
    val description: String?,
    @SerializedName("start_date") val startDate: String? = null,
    @SerializedName("end_date") val endDate: String? = null,
    val link: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// Message
data class MessageDto(
    val id: Int,
    val sender: String? = null,
    val user: String? = null,
    val content: String? = null,
    val body: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    val read: Boolean? = null,
    @SerializedName("read_flag") val readFlag: Int? = null
)

// Notification
data class NotificationDto(
    val id: Int,
    val title: String? = null,
    val message: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    val type: String? = null
)

// Company
data class CompanyDto(
    val id: Int,
    val name: String?,
    val headline: String? = null,
    val description: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// Favorite (guardados)
data class FavoriteDto(
    val id: Int,
    @SerializedName("target_id") val targetId: Int? = null,
    val type: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// Job Application
data class JobApplicationDto(
    val id: Int,
    @SerializedName("job_offer_id") val jobOfferId: Int? = null,
    val status: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// Portfolio
data class PortfolioDto(
    val id: Int,
    val title: String?,
    val url: String? = null,
    val description: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// TrainingUser (inscripciones)
data class TrainingUserDto(
    val id: Int,
    @SerializedName("training_id") val trainingId: Int?,
    @SerializedName("user_id") val userId: Int?,
    @SerializedName("created_at") val createdAt: String? = null
)

// Unemployed
data class UnemployedDto(
    val id: Int,
    val name: String? = null,
    val email: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// User list
data class BasicUserDto(
    val id: Int,
    val name: String?,
    val email: String?,
    @SerializedName("created_at") val createdAt: String? = null
)

// Category
data class CategoryDto(
    val id: Int,
    val name: String?,
    val description: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// Comment
data class CommentDto(
    val id: Int,
    val body: String?,
    @SerializedName("user_id") val userId: Int? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<AuthResponse>

    @GET("profile")
    suspend fun getProfile(): Response<AuthResponse>

    // JOB OFFERS
    @GET("job-offer")
    suspend fun getJobOffers(): Response<List<JobOfferDto>>

    // CLASSIFIEDS
    @GET("classified")
    suspend fun getClassifieds(): Response<List<ClassifiedDto>>

    // TRAININGS
    @GET("training")
    suspend fun getTrainings(): Response<List<TrainingDto>>

    // MESSAGES
    @GET("message")
    suspend fun getMessages(): Response<List<MessageDto>>

    // NOTIFICATIONS
    @GET("notification")
    suspend fun getNotifications(): Response<List<NotificationDto>>

    // COMPANIES
    @GET("company")
    suspend fun getCompanies(): Response<List<CompanyDto>>

    // FAVORITES
    @GET("favorite")
    suspend fun getFavorites(): Response<List<FavoriteDto>>

    // JOB APPLICATIONS
    @GET("job-application")
    suspend fun getJobApplications(): Response<List<JobApplicationDto>>

    // PORTFOLIO
    @GET("portfolio")
    suspend fun getPortfolio(): Response<List<PortfolioDto>>

    // TRAINING USER
    @GET("training-user")
    suspend fun getTrainingUsers(): Response<List<TrainingUserDto>>

    // UNEMPLOYED
    @GET("unemployed")
    suspend fun getUnemployed(): Response<List<UnemployedDto>>

    // USERS
    @GET("user")
    suspend fun getUsers(): Response<List<BasicUserDto>>

    // CATEGORIES
    @GET("category")
    suspend fun getCategories(): Response<List<CategoryDto>>

    // COMMENTS
    @GET("comment")
    suspend fun getComments(): Response<List<CommentDto>>
}
