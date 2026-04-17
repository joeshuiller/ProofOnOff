package com.janes.saenz.puerta.proofonoff.core

import android.content.Context
import androidx.room.RoomDatabase
import com.janes.saenz.puerta.proofonoff.data.dataBase.db.DbData
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostCommentsDao
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostDao
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostFavoriteDao
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class DatabaseModuleTest {

    private lateinit var mockContext: Context
    private lateinit var mockDbData: DbData

    @Before
    fun setUp() {
        mockContext = mockk(relaxed = true)
        mockDbData = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `provideDatabase should return a RoomDatabase instance`() {
        // Mocking Room.databaseBuilder is complex, we verify it calls the builder
        mockkStatic(androidx.room.Room::class)
        val mockBuilder = mockk<RoomDatabase.Builder<DbData>>(relaxed = true)

        every {
            androidx.room.Room.databaseBuilder(any(), DbData::class.java, "proof_on_off_db")
        } returns mockBuilder

        every { mockBuilder.build() } returns mockDbData

        val result = DatabaseModule.provideDatabase(mockContext)

        assertNotNull(result)
        assertEquals(mockDbData, result)
        verify { androidx.room.Room.databaseBuilder(mockContext, DbData::class.java, "proof_on_off_db") }
    }

    @Test
    fun `providePostDao should return PostDao from database`() {
        val mockDao = mockk<PostDao>()
        every { mockDbData.postDao() } returns mockDao

        val result = DatabaseModule.providePostDao(mockDbData)

        assertEquals(mockDao, result)
        verify { mockDbData.postDao() }
    }

    @Test
    fun `providePostCommentsDao should return PostCommentsDao from database`() {
        val mockDao = mockk<PostCommentsDao>()
        every { mockDbData.postCommentsDao() } returns mockDao

        val result = DatabaseModule.providePostCommentsDao(mockDbData)

        assertEquals(mockDao, result)
        verify { mockDbData.postCommentsDao() } // Nota: Asegura que el nombre coincida con tu clase DbData
    }

    @Test
    fun `provideFavoriteDao should return PostFavoriteDao from database`() {
        val mockDao = mockk<PostFavoriteDao>()
        every { mockDbData.favoriteDao() } returns mockDao

        val result = DatabaseModule.provideFavoriteDao(mockDbData)

        assertEquals(mockDao, result)
        verify { mockDbData.favoriteDao() }
    }
}
