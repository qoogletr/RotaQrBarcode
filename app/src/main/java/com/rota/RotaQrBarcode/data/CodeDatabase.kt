import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ScannedCode::class], // Veritabanı tablolarınızı buraya ekleyin
    version = 1,
    exportSchema = false // Şema dışa aktarmayı devre dışı bırak
)
abstract class CodeDatabase : RoomDatabase() {
    abstract fun scannedCodeDao(): ScannedCodeDao

    companion object {
        @Volatile
        private var INSTANCE: CodeDatabase? = null

        fun getInstance(context: Context): CodeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CodeDatabase::class.java,
                    "code_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}