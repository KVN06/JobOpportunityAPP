package com.kvn.jobopportunityapp.domain.model

data class SavedItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val type: String // job | company | resource
)

data class MessageItem(
    val id: String,
    val from: String,
    val preview: String,
    val time: String,
    val unread: Boolean
)

data class ContactItem(
    val id: String,
    val name: String,
    val role: String,
    val company: String
)

data class AlertItem(
    val id: String,
    val query: String,
    val frequency: String,
    val active: Boolean
)

data class HistoryItem(
    val id: String,
    val action: String,
    val date: String,
    val details: String
)

data class CompanyUpdate(
    val id: String,
    val company: String,
    val headline: String,
    val time: String
)

data class IntegrationAccount(
    val id: String,
    val name: String,
    val connected: Boolean,
    val note: String
)

data class FeedbackItem(
    val id: String,
    val subject: String,
    val status: String,
    val date: String
)

data class AvailabilitySlot(
    val id: String,
    val day: String,
    val from: String,
    val to: String,
    val active: Boolean
)

data class BillingPlan(
    val id: String,
    val name: String,
    val price: String,
    val features: List<String>,
    val current: Boolean
)

data class ReferralItem(
    val id: String,
    val name: String,
    val invitedAt: String,
    val status: String
)

data class PersonalPreference(
    val id: String,
    val label: String,
    val enabled: Boolean
)
