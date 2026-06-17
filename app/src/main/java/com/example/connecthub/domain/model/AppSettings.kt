package com.example.connecthub.domain.model

data class AppSettings(
    val darkMode: Boolean = false,
    val dynamicColor: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val language: String = "English",
)
