package com.viasverdes.viasverdesespana.data.dbo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "province")
data class ProvinceDBO(
        @PrimaryKey val id: Long,
        var name: String
)