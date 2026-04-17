package com.janes.saenz.puerta.proofonoff.data.dataBase.source

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.PostEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostDao
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

class PostDataSourceImplTest {

    private lateinit var apiDao: PostDao
    private lateinit var dataSource: PostDataSourceImpl

    @Before
    fun setUp() {
        apiDao = mockk()
        dataSource = PostDataSourceImpl(apiDao)
    }

    @Test
    fun `clearAndInsertPosts should call DAO clearAndInsertPosts`() = runTest {
        // GIVEN
        val posts = listOf(mockk<PostEntity>(), mockk<PostEntity>())
        coEvery { apiDao.clearAndInsertPosts(posts) } returns Unit

        // WHEN
        dataSource.clearAndInsertPosts(posts)

        // THEN
        coVerify(exactly = 1) { apiDao.clearAndInsertPosts(posts) }
    }

    @Test
    fun `observeAllPosts should call DAO observeAllPosts and return flow`() {
        // GIVEN
        val expectedList = listOf(mockk<PostEntity>())
        val expectedFlow = flowOf(expectedList)
        every { apiDao.observeAllPosts() } returns expectedFlow

        // WHEN
        val result = dataSource.observeAllPosts()

        // THEN
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { apiDao.observeAllPosts() }
    }

    @Test
    fun `observeFilteredPosts should call DAO with provided filters`() {
        // GIVEN
        val id = 1
        val title = "Test Title"
        val expectedFlow = flowOf(listOf(mockk<PostEntity>()))
        every { apiDao.observeFilteredPosts(id, title) } returns expectedFlow

        // WHEN
        val result = dataSource.observeFilteredPosts(id, title)

        // THEN
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { apiDao.observeFilteredPosts(id, title) }
    }

    @Test
    fun `observeFilteredPosts should call DAO even with null filters`() {
        // GIVEN
        val id = null
        val title = null
        val expectedFlow = flowOf(emptyList<PostEntity>())
        every { apiDao.observeFilteredPosts(id, title) } returns expectedFlow

        // WHEN
        val result = dataSource.observeFilteredPosts(id, title)

        // THEN
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { apiDao.observeFilteredPosts(id, title) }
    }

    @Test
    fun `getPostById should call DAO getPostById and return flow`() {
        // GIVEN
        val postId = 1
        val expectedPost = mockk<PostEntity>()
        val expectedFlow = flowOf(expectedPost)
        every { apiDao.getPostById(postId) } returns expectedFlow

        // WHEN
        val result = dataSource.getPostById(postId)

        // THEN
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { apiDao.getPostById(postId) }
    }
}
