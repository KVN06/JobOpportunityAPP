package com.kvn.jobopportunityapp.data.mapper

import com.kvn.jobopportunityapp.data.network.*
import com.kvn.jobopportunityapp.domain.model.*

// Centralización de conversiones DTO -> Modelos dominio existentes.

fun JobOfferDto.toJobPost(): JobPost = JobPost(
    id = id.toString(),
    title = title ?: "Oferta",
    description = description ?: "",
    salary = salary ?: salaryRange ?: "",
    location = location ?: city ?: "",
    geoUrl = null,
    categories = emptyList(),
    createdAt = System.currentTimeMillis()
)

fun TrainingDto.toTrainingPost(): TrainingPost = TrainingPost(
    id = id.toString(),
    title = title ?: "Training",
    provider = "", // No provisto aún por backend
    description = description ?: "",
    link = link ?: "",
    startDate = startDate ?: "",
    endDate = endDate ?: ""
)

fun ClassifiedDto.toClassifiedPost(): ClassifiedPost = ClassifiedPost(
    id = id.toString(),
    title = title ?: "Clasificado",
    description = description ?: "",
    price = price ?: "",
    location = location ?: "",
    geoUrl = null,
    categories = emptyList(),
    createdAt = System.currentTimeMillis()
)

fun CompanyDto.toCompanyUpdate(): CompanyUpdate = CompanyUpdate(
    id = id.toString(),
    company = name ?: "Empresa",
    headline = headline ?: description ?: "",
    time = createdAt ?: ""
)

fun MessageDto.toMessageItem(): MessageItem = MessageItem(
    id = id.toString(),
    from = sender ?: user ?: "--",
    preview = content ?: body ?: "",
    time = createdAt ?: "",
    unread = (read ?: (readFlag == 0)) == true
)

fun NotificationDto.toAlertItem(): AlertItem = AlertItem(
    id = id.toString(),
    query = title ?: type ?: "Alerta",
    frequency = "", // No data yet
    active = true
)

fun FavoriteDto.toSavedItem(): SavedItem = SavedItem(
    id = id.toString(),
    title = (type ?: "Favorito") + " #" + (targetId?.toString() ?: ""),
    subtitle = createdAt ?: "",
    type = type ?: "unknown"
)

fun BasicUserDto.toContactItem(): ContactItem = ContactItem(
    id = id.toString(),
    name = name ?: "Usuario",
    role = "", // No role yet
    company = "" // No company yet
)
