package com.viasverdes.viasverdesespana.data.dbo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "itinerary")
data class ViaVerdeDBO(
        @PrimaryKey val id: Long,
        val codVV: String,
        var name: String,
        var length: Float,
        var provinces: String,
        var localization: String,
        var naturaText: String,
        var userTypes: String
)