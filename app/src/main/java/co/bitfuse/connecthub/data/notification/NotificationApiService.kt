package co.bitfuse.connecthub.data.notification

interface NotificationApiService {
    suspend fun getNotifications(): List<NotificationDto>
}
