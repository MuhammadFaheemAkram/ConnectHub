package com.example.connecthub.data.mapper

import com.example.connecthub.data.comment.CommentDto
import com.example.connecthub.data.comment.CommentEntity
import com.example.connecthub.domain.model.Comment
import com.example.connecthub.domain.model.User

fun CommentDto.toEntity(): CommentEntity = CommentEntity(
    id = id,
    postId = postId,
    authorId = author.id,
    authorName = author.name,
    authorAvatarUrl = author.avatarUrl,
    authorBio = author.bio,
    authorFollowersCount = author.followersCount,
    authorFollowingCount = author.followingCount,
    text = text,
    createdAt = createdAt,
    isOwnComment = isOwnComment,
)

fun CommentEntity.toDomain(): Comment = Comment(
    id = id,
    postId = postId,
    author = User(
        id = authorId,
        name = authorName,
        avatarUrl = authorAvatarUrl,
        bio = authorBio,
        followersCount = authorFollowersCount,
        followingCount = authorFollowingCount,
    ),
    text = text,
    createdAt = createdAt,
    isOwnComment = isOwnComment,
)
