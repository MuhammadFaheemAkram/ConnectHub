package com.example.connecthub.data.feed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun observeFeed(): Flow<List<PostEntity>>

    @Query(
        """
        SELECT * FROM posts
        WHERE content LIKE '%' || :query || '%'
            OR authorName LIKE '%' || :query || '%'
            OR authorBio LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
        """,
    )
    fun searchPosts(query: String): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE authorId = :authorId ORDER BY createdAt DESC")
    fun observePostsByAuthor(authorId: String): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :postId LIMIT 1")
    fun observePost(postId: String): Flow<PostEntity?>

    @Query("SELECT * FROM posts WHERE id = :postId LIMIT 1")
    suspend fun getPostById(postId: String): PostEntity?

    @Query("SELECT postId FROM bookmarked_posts")
    suspend fun getBookmarkedPostIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPosts(posts: List<PostEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPost(post: PostEntity)

    @Query("UPDATE posts SET isLiked = :isLiked, likeCount = :likeCount WHERE id = :postId")
    suspend fun updateLikeState(postId: String, isLiked: Boolean, likeCount: Int)

    @Query("UPDATE posts SET isBookmarked = :isBookmarked WHERE id = :postId")
    suspend fun updateBookmarkFlag(postId: String, isBookmarked: Boolean)

    @Query("UPDATE posts SET commentCount = CASE WHEN commentCount + :delta < 0 THEN 0 ELSE commentCount + :delta END WHERE id = :postId")
    suspend fun updateCommentCount(postId: String, delta: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBookmark(bookmarkedPost: BookmarkedPostEntity)

    @Query("DELETE FROM bookmarked_posts WHERE postId = :postId")
    suspend fun deleteBookmark(postId: String)

    @Query("SELECT posts.* FROM posts INNER JOIN bookmarked_posts ON posts.id = bookmarked_posts.postId ORDER BY bookmarked_posts.bookmarkedAt DESC")
    fun observeBookmarkedPosts(): Flow<List<PostEntity>>

    @Transaction
    suspend fun setBookmarkState(
        postId: String,
        isBookmarked: Boolean,
        bookmarkedAt: Long = System.currentTimeMillis(),
    ) {
        updateBookmarkFlag(postId = postId, isBookmarked = isBookmarked)
        if (isBookmarked) {
            upsertBookmark(BookmarkedPostEntity(postId = postId, bookmarkedAt = bookmarkedAt))
        } else {
            deleteBookmark(postId)
        }
    }
}
