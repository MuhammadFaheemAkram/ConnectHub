package co.bitfuse.connecthub.feature

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import co.bitfuse.connecthub.core.designsystem.theme.ConnectHubTheme
import co.bitfuse.connecthub.feature.comments.CommentUiModel
import co.bitfuse.connecthub.feature.createpost.CreatePostContent
import co.bitfuse.connecthub.feature.createpost.CreatePostUiState
import co.bitfuse.connecthub.feature.feed.PostUiModel
import co.bitfuse.connecthub.feature.postdetail.PostDetailContent
import co.bitfuse.connecthub.feature.postdetail.PostDetailUiState
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class Phase4ComposeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun postDetailDisplaysPostAndCommentPreview() {
        var openedComments = false
        composeRule.setContent {
            ConnectHubTheme {
                PostDetailContent(
                    uiState = PostDetailUiState(
                        isLoading = false,
                        post = samplePost(),
                        commentsPreview = listOf(sampleComment()),
                    ),
                    onRetry = {},
                    onLikeClick = {},
                    onBookmarkClick = {},
                    onCommentDraftChange = {},
                    onAddComment = {},
                    onOpenComments = { openedComments = true },
                )
            }
        }

        composeRule.onNodeWithText("Detailed post body").assertIsDisplayed()
        composeRule.onNodeWithText("Helpful comment").assertIsDisplayed()
        composeRule.onNodeWithText("View all").performClick()

        assertTrue(openedComments)
    }

    @Test
    fun createPostButtonSubmitsWhenContentIsValid() {
        var submitted = false
        composeRule.setContent {
            ConnectHubTheme {
                CreatePostContent(
                    uiState = CreatePostUiState(content = "Hello ConnectHub"),
                    onContentChange = {},
                    onImageUrlChange = {},
                    onSubmit = { submitted = true },
                )
            }
        }

        composeRule.onNodeWithText("Post").assertIsEnabled()
        composeRule.onNodeWithText("Post").performClick()

        assertTrue(submitted)
    }

    @Test
    fun createPostButtonIsDisabledForBlankContent() {
        composeRule.setContent {
            ConnectHubTheme {
                CreatePostContent(
                    uiState = CreatePostUiState(),
                    onContentChange = {},
                    onImageUrlChange = {},
                    onSubmit = {},
                )
            }
        }

        composeRule.onNodeWithText("Post").assertIsNotEnabled()
    }

    private fun samplePost(): PostUiModel = PostUiModel(
        id = "post-1",
        authorName = "Maya Chen",
        authorHeadline = "Android Engineer",
        timeAgo = "1m ago",
        content = "Detailed post body",
        imageUrl = null,
        likeCount = 4,
        commentCount = 1,
        isLiked = false,
        isBookmarked = false,
    )

    private fun sampleComment(): CommentUiModel = CommentUiModel(
        id = "comment-1",
        authorName = "Omar Malik",
        authorHeadline = "Product Designer",
        text = "Helpful comment",
        timeAgo = "1m ago",
        isOwnComment = false,
    )
}
