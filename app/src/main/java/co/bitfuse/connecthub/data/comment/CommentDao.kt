package co.bitfuse.connecthub.data.comment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY createdAt ASC")
    fun observeComments(postId: String): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertComments(comments: List<CommentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertComment(comment: CommentEntity)

    @Query("DELETE FROM comments WHERE id = :commentId AND isOwnComment = 1")
    suspend fun deleteOwnComment(commentId: String)

    @Query("SELECT * FROM comments WHERE id = :commentId LIMIT 1")
    suspend fun getComment(commentId: String): CommentEntity?
}
