package com.janes.saenz.puerta.proofonoff.data.dataBase.source

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.FavoriteEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations.PostWithFavorite
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostFavoriteDao
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

class PostFavoriteDataSourceImplTest {

    private lateinit var apiDao: PostFavoriteDao
    private lateinit var dataSource: PostFavoriteDataSourceImpl

    @Before
    fun setUp() {
        apiDao = mockk()
        dataSource = PostFavoriteDataSourceImpl(apiDao)
    }

    @Test
    fun `getPostsWithFavorites should call DAO and return flow of relation`() {
        // GIVEN
        val postId = 1
        val expectedRelation = mockk<PostWithFavorite>()
        val expectedFlow = flowOf(expectedRelation)
        every { apiDao.getPostWithFavorites(postId) } returns expectedFlow

        // WHEN
        val result = dataSource.getPostsWithFavorites(postId)

        // THEN
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { apiDao.getPostWithFavorites(postId) }
    }

    @Test
    fun `insertFavorite should call DAO insertFavorite`() = runTest {
        // GIVEN
        val favorite = mockk<FavoriteEntity>()
        coEvery { apiDao.insertFavorite(favorite) } returns Unit

        // WHEN
        dataSource.insertFavorite(favorite)

        // THEN
        coVerify(exactly = 1) { apiDao.insertFavorite(favorite) }
    }

    @Test
    fun `deleteFavoriteByPostId should call DAO deleteFavoriteByPostId`() = runTest {
        // GIVEN
        val postId = 1
        coEvery { apiDao.deleteFavoriteByPostId(postId) } returns Unit

        // WHEN
        dataSource.deleteFavoriteByPostId(postId)

        // THEN
        coVerify(exactly = 1) { apiDao.deleteFavoriteByPostId(postId) }
    }

    @Test
    fun `getAllPostsWithFavorites should call DAO and return list flow`() {
        // GIVEN
        val expectedList = listOf(mockk<PostWithFavorite>())
        val expectedFlow = flowOf(expectedList)
        every { apiDao.getAllPostsWithFavorites() } returns expectedFlow

        // WHEN
        val result = dataSource.getAllPostsWithFavorites()

        // THEN
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { apiDao.getAllPostsWithFavorites() }
    }

    @Test
    fun `isFavorite should call DAO and return boolean flow`() {
        // GIVEN
        val postId = 1
        val expectedFlow = flowOf(true)
        every { apiDao.isFavorite(postId) } returns expectedFlow

        // WHEN
        val result = dataSource.isFavorite(postId)

        // THEN
        assertEquals(expectedFlow, result)
        verify(exactly = 1) { apiDao.isFavorite(postId) }
    }
}