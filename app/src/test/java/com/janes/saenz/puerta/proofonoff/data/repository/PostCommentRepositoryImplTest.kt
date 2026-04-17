package com.janes.saenz.puerta.proofonoff.data.repository

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.CommentEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations.PostWithComments
import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostCommentsDataSource
import com.janes.saenz.puerta.proofonoff.data.mapper.CommentDataBaseMapper
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Comments
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
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
class PostCommentRepositoryImplTest {

    // Dependencias mockeadas
    private lateinit var dataSource: PostCommentsDataSource
    private lateinit var mapper: CommentDataBaseMapper

    // Clase bajo prueba
    private lateinit var repository: PostCommentRepositoryImpl

    @Before
    fun setUp() {
        dataSource = mockk()
        mapper = mockk()
        repository = PostCommentRepositoryImpl(dataSource, mapper)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getPostWithComments should emit loading and then success with mapped data`() = runTest {
        // GIVEN
        val postId = 1
        val mockEntity = mockk<PostWithComments>()
        val mockCommentsEntities = listOf(mockk<CommentEntity>())
        val mockDomainComments = listOf(mockk<Comments>())

        // Configuramos el mock de la entidad relacional
        every { mockEntity.comments } returns mockCommentsEntities

        // Configuramos el comportamiento del DataSource y el Mapper
        every { dataSource.getPostWithComments(postId) } returns flowOf(mockEntity)
        every { mapper.fromEntityListToDomain(mockCommentsEntities) } returns mockDomainComments

        // WHEN: Recolectamos el flujo
        val results = repository.getPostWithComments(postId).toList()

        // THEN
        assertEquals(2, results.size)
        assertTrue(results[0] is Resource.Loading)
        assertTrue(results[1] is Resource.Success)
        assertEquals(mockDomainComments, (results[1] as Resource.Success).data)

        verify(exactly = 1) { dataSource.getPostWithComments(postId) }
        verify(exactly = 1) { mapper.fromEntityListToDomain(any()) }
    }

    @Test
    fun `insertComments should map domain list to entity and call dataSource`() = runTest {
        // GIVEN
        val domainComments = listOf(mockk<Comments>())
        val entities = listOf(mockk<CommentEntity>())

        every { mapper.fromDomainToEntity(any()) } returns entities.first()
        coEvery { dataSource.insertComments(any()) } returns Unit

        // WHEN
        repository.insertComments(domainComments)

        // THEN
        coVerify(exactly = 1) { dataSource.insertComments(any()) }
        verify(exactly = domainComments.size) { mapper.fromDomainToEntity(any()) }
    }

    @Test
    fun `insertComment should map single domain to entity and call dataSource`() = runTest {
        // GIVEN
        val domainComment = mockk<Comments>()
        val entity = mockk<CommentEntity>()

        every { mapper.fromDomainToEntity(domainComment) } returns entity
        coEvery { dataSource.insertComment(entity) } returns Unit

        // WHEN
        repository.insertComment(domainComment)

        // THEN
        coVerify(exactly = 1) { dataSource.insertComment(entity) }
    }

    @Test
    fun `deleteCommentsByPostId should call dataSource`() = runTest {
        // GIVEN
        val postId = 123
        coEvery { dataSource.deleteCommentsByPostId(postId) } returns Unit

        // WHEN
        repository.deleteCommentsByPostId(postId)

        // THEN
        coVerify(exactly = 1) { dataSource.deleteCommentsByPostId(postId) }
    }
}
