package co.bitfuse.connecthub.data.feed

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookmarked_posts",
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["postId"])],
)
data class BookmarkedPostEntity(
    @PrimaryKey val postId: String,
    val bookmarkedAt: Long,
)
