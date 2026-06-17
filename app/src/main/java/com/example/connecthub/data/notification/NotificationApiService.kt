package com.example.connecthub.data.notification

interface NotificationApiService {
    suspend fun getNotifications(): List<NotificationDto>
}
