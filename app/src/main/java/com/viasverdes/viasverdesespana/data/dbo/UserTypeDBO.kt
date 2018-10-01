package com.viasverdes.viasverdesespana.data.dbo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user_type")
data class UserTypeDBO(
        @PrimaryKey val id: Long,
        var image: String
)