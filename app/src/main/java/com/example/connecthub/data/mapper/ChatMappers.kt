package com.example.connecthub.data.mapper

import com.example.connecthub.data.chat.ConversationDto
import com.example.connecthub.data.chat.ConversationEntity
import com.example.connecthub.data.chat.MessageDto
import com.example.connecthub.data.chat.MessageEntity
import com.example.connecthub.domain.model.Conversation
import com.example.connecthub.domain.model.Message
import com.example.connecthub.domain.model.User

fun ConversationDto.toEntity(): ConversationEntity = ConversationEntity(
    id = id,
    participantId = participant.id,
    participantName = participant.name,
    participantAvatarUrl = participant.avatarUrl,
    participantBio = participant.bio,
    participantFollowersCount = participant.followersCount,
    participantFollowingCount = participant.followingCount,
    lastMessage = lastMessage,
    unreadCount = unreadCount,
    updatedAt = updatedAt,
)

fun ConversationEntity.toDomain(): Conversation = Conversation(
    id = id,
    participant = User(
        id = participantId,
        name = participantName,
        avatarUrl = participantAvatarUrl,
        bio = participantBio,
        followersCount = participantFollowersCount,
        followingCount = participantFollowingCount,
    ),
    lastMessage = lastMessage,
    unreadCount = unreadCount,
    updatedAt = updatedAt,
)

fun MessageDto.toEntity(): MessageEntity = MessageEntity(
    id = id,
    conversationId = conversationId,
    senderId = senderId,
    text = text,
    createdAt = createdAt,
    isMine = isMine,
)

fun MessageEntity.toDomain(): Message = Message(
    id = id,
    conversationId = conversationId,
    senderId = senderId,
    text = text,
    createdAt = createdAt,
    isMine = isMine,
)
