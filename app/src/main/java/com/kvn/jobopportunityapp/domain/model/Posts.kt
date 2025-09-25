package com.kvn.jobopportunityapp.domain.model

data class JobPost(
    val id: String,
    val title: String,
    val description: String,
    val salary: String,
    val location: String,
    val geoUrl: String?, // https://maps.google.com/?q=... o geo:lat,lng
    val categories: List<String>,
    val createdAt: Long = System.currentTimeMillis()
)

data class ClassifiedPost(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val location: String,
    val geoUrl: String?,
    val categories: List<String>,
    val createdAt: Long = System.currentTimeMillis()
)

data class TrainingPost(
    val id: String,
    val title: String,
    val provider: String,
    val description: String,
    val link: String,
    val startDate: String, // YYYY-MM-DD
    val endDate: String,   // YYYY-MM-DD
    val createdAt: Long = System.currentTimeMillis()
)
