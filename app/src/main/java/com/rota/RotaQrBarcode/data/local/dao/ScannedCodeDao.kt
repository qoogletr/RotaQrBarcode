package com.rota.RotaQrBarcode.data.local.dao

import androidx.room.*
import com.rota.RotaQrBarcode.data.local.entity.ScannedCode
import kotlinx.coroutines.flow.Flow

@Dao
interface ScannedCodeDao {
    @Query("SELECT * FROM scanned_codes ORDER BY timestamp DESC")
    fun getAllCodes(): Flow<List<ScannedCode>>
    
    @Query("SELECT * FROM scanned_codes WHERE serverSent = 0 ORDER BY timestamp ASC")
    fun getPendingCodes(): Flow<List<ScannedCode>>
    
    @Query("SELECT * FROM scanned_codes WHERE id = :id")
    suspend fun getCodeById(id: Long): ScannedCode?
    
    @Insert
    suspend fun insert(code: ScannedCode): Long
    
    @Update
    suspend fun update(code: ScannedCode)
    
    @Delete
    suspend fun delete(code: ScannedCode)
    
    @Query("DELETE FROM scanned_codes")
    suspend fun deleteAll()
    
    @Query("UPDATE scanned_codes SET serverSent = 1 WHERE id = :id")
    suspend fun markAsSent(id: Long)
    
    @Query("SELECT COUNT(*) FROM scanned_codes")
    fun getCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM scanned_codes WHERE serverSent = 1")
    fun getSentCount(): Flow<Int>
}