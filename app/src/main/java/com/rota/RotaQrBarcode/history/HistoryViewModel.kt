import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.barcodescanner.data.CodeDatabase
import com.example.barcodescanner.data.HistoryRepository

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HistoryRepository
    val allCodes = repository.allCodes

    init {
        val dao = CodeDatabase.getInstance(application).scannedCodeDao()
        repository = HistoryRepository(dao)
    }

    suspend fun delete(code: ScannedCode) {
        repository.delete(code)
    }

    suspend fun deleteAll() {
        repository.deleteAll()
    }
}