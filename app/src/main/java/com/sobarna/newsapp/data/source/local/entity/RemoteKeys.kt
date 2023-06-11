package com.sobarna.newsapp.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prevKey: Int?,
    val nextKey: Int?
)