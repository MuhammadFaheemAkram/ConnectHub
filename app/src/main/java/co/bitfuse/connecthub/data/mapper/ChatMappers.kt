package co.bitfuse.connecthub.data.mapper

import co.bitfuse.connecthub.data.chat.ConversationDto
import co.bitfuse.connecthub.data.chat.ConversationEntity
import co.bitfuse.connecthub.data.chat.MessageDto
import co.bitfuse.connecthub.data.chat.MessageEntity
import co.bitfuse.connecthub.domain.model.Conversation
import co.bitfuse.connecthub.domain.model.Message
import co.bitfuse.connecthub.domain.model.User

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
