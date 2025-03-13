import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "scanned_codes")
data class ScannedCode(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val value: String,
    val timestamp: Long = Date().time,
    val type: String,
    val format: String,
    val serverSent: Boolean = false
)