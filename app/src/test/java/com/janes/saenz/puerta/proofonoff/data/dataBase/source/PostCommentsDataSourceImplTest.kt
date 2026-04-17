package com.janes.saenz.puerta.proofonoff.data.dataBase.source

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.CommentEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations.PostWithComments
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostCommentsDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PostCommentsDataSourceImplTest {

    // Mock del DAO (dependencia)
    private lateinit var apiDao: PostCommentsDao

    // Clase bajo prueba (SUT)
    private lateinit var dataSource: PostCommentsDataSourceImpl

    @Before
    fun setUp() {
        apiDao = mockk()
        dataSource = PostCommentsDataSourceImpl(apiDao)
    }

    @Test
    fun `getPostWithComments should call DAO and return flow`() = runTest {
        // GIVEN
        val postId = 1
        val expectedResult = mockk<PostWithComments>()
        val expectedFlow = flowOf(expectedResult)

        every { apiDao.getPostWithComments(postId) } returns expectedFlow

        // WHEN
        val result = dataSource.getPostWithComments(postId)

        // THEN
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { apiDao.getPostWithComments(postId) }
    }

    @Test
    fun `insertComments should call DAO insertComments`() = runTest {
        // GIVEN
        val comments = listOf(mockk<CommentEntity>(), mockk<CommentEntity>())
        coEvery { apiDao.insertComments(comments) } returns Unit

        // WHEN
        dataSource.insertComments(comments)

        // THEN
        coVerify(exactly = 1) { apiDao.insertComments(comments) }
    }

    @Test
    fun `insertComment should call DAO insertComment`() = runTest {
        // GIVEN
        val comment = mockk<CommentEntity>()
        coEvery { apiDao.insertComment(comment) } returns Unit

        // WHEN
        dataSource.insertComment(comment)

        // THEN
        coVerify(exactly = 1) { apiDao.insertComment(comment) }
    }

    @Test
    fun `deleteCommentsByPostId should call DAO deleteCommentsByPostId`() = runTest {
        // GIVEN
        val postId = 100
        coEvery { apiDao.deleteCommentsByPostId(postId) } returns Unit

        // WHEN
        dataSource.deleteCommentsByPostId(postId)

        // THEN
        coVerify(exactly = 1) { apiDao.deleteCommentsByPostId(postId) }
    }
}