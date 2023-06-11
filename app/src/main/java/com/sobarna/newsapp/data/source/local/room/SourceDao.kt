package com.sobarna.newsapp.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sobarna.newsapp.data.source.local.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDao {

    @Query("SELECT DISTINCT * FROM sources")
    fun getAllSource(): Flow<List<SourceEntity>>



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllSources(sourceEntity: List<SourceEntity>)

    @Query("DELETE from sources")
    suspend fun deleteAllSources()
}