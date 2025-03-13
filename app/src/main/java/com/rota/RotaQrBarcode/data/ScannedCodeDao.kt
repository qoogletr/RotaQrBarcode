import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScannedCodeDao {
    @Query("SELECT * FROM scanned_codes ORDER BY timestamp DESC")
    fun getAllCodes(): Flow<List<ScannedCode>>

    @Insert
    suspend fun insert(code: ScannedCode)

    @Delete
    suspend fun delete(code: ScannedCode)

    @Query("DELETE FROM scanned_codes")
    suspend fun deleteAll()
}