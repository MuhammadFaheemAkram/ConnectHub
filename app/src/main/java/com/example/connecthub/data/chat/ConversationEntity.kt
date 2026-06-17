package com.example.connecthub.data.chat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: String,
    val participantId: String,
    val participantName: String,
    val participantAvatarUrl: String,
    val participantBio: String,
    val participantFollowersCount: Int,
    val participantFollowingCount: Int,
    val lastMessage: String,
    val unreadCount: Int,
    val updatedAt: Long,
)
