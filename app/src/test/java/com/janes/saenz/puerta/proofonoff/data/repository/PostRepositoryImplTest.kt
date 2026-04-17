package com.janes.saenz.puerta.proofonoff.data.repository

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.PostEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostDataSource
import com.janes.saenz.puerta.proofonoff.data.mapper.PostDataBaseMapper
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostRepositoryImplTest {

    private lateinit var dataSource: PostDataSource
    private lateinit var mapper: PostDataBaseMapper
    private lateinit var repository: PostRepositoryImpl

    @Before
    fun setUp() {
        dataSource = mockk()
        mapper = mockk()
        repository = PostRepositoryImpl(dataSource, mapper)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `clearAndInsertPosts should map list and call dataSource in IO`() = runTest {
        // GIVEN
        val domainPosts = listOf(mockk<Posts>())
        val entities = listOf(mockk<PostEntity>())

        every { mapper.fromDomainToEntity(any()) } returns entities.first()
        coEvery { dataSource.clearAndInsertPosts(entities) } returns Unit

        // WHEN
        repository.clearAndInsertPosts(domainPosts)

        // THEN
        coVerify(exactly = 1) { dataSource.clearAndInsertPosts(entities) }
        verify(exactly = domainPosts.size) { mapper.fromDomainToEntity(any()) }
    }

    @Test
    fun `observeAllPosts should emit Loading then Success with mapped list`() = runTest {
        // GIVEN
        val entities = listOf(mockk<PostEntity>())
        val domainPosts = listOf(mockk<Posts>())

        every { dataSource.observeAllPosts() } returns flowOf(entities)
        every { mapper.fromEntityListToDomain(entities) } returns domainPosts

        // WHEN
        val results = repository.observeAllPosts().toList()

        // THEN
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)
        assertEquals(domainPosts, (results[1] as Resource.Success).data)
    }

    @Test
    fun `observeFilteredPosts should pass parameters correctly to dataSource`() = runTest {
        // GIVEN
        val id = 101
        val title = "Android"
        val entities = listOf(mockk<PostEntity>())
        val domainPosts = listOf(mockk<Posts>())

        every { dataSource.observeFilteredPosts(id, title) } returns flowOf(entities)
        every { mapper.fromEntityListToDomain(entities) } returns domainPosts

        // WHEN
        val results = repository.observeFilteredPosts(id, title).toList()

        // THEN
        assertEquals(domainPosts, (results[1] as Resource.Success).data)
        verify { dataSource.observeFilteredPosts(id, title) }
    }

    @Test
    fun `getPostById should emit Success with single mapped post`() = runTest {
        // GIVEN
        val id = 1
        val entity = mockk<PostEntity>()
        val domainPost = mockk<Posts>()

        every { dataSource.getPostById(id) } returns flowOf(entity)
        every { mapper.fromEntityToDomain(entity) } returns domainPost

        // WHEN
        val results = repository.getPostById(id).toList()

        // THEN
        assertTrue(results[1] is Resource.Success)
        assertEquals(domainPost, (results[1] as Resource.Success).data)
        verify { dataSource.getPostById(id) }
    }
}