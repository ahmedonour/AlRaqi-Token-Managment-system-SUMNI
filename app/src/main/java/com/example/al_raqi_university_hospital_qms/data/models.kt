package com.example.al_raqi_university_hospital_qms.data

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val number: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class HistoryItem(
    val sectionName: String,
    val tokenNumber: Int,
    val timestamp: Long,
    val price: Int
)

@Serializable
data class Section(
    val id: Int,
    val name: String,
    val type: String, // Consider making this an enum later for strict types (e.g., SectionType.Clinic)
    val price: Int,
    val queue: List<Token>,
    val doctors: List<String> = emptyList()
)

// Enum for SectionType (optional, but good practice for type safety)
enum class SectionType {
    Clinic, Laboratory, All
}