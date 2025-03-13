package com.rota.RotaQrBarcode.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "scanned_codes")
data class ScannedCode(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val value: String,
    val type: String,
    val format: String,
    val timestamp: Long = Date().time,
    val serverSent: Boolean = false,
    val notes: String? = null
) : Parcelable {
    
    val formattedTimestamp: String
        get() = DATE_FORMAT.format(Date(timestamp))
        
    companion object {
        private val DATE_FORMAT = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    }
}