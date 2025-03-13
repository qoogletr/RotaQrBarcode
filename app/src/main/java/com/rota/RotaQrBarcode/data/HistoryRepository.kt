import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val scannedCodeDao: ScannedCodeDao) {
    val allCodes: Flow<List<ScannedCode>> = scannedCodeDao.getAllCodes()

    suspend fun insert(code: ScannedCode) {
        scannedCodeDao.insert(code)
    }

    suspend fun delete(code: ScannedCode) {
        scannedCodeDao.delete(code)
    }

    suspend fun deleteAll() {
        scannedCodeDao.deleteAll()
    }
}