import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class GeneratorViewModel : ViewModel() {
    private var currentBitmap: Bitmap? = null

    fun setGeneratedBitmap(bitmap: Bitmap) {
        currentBitmap = bitmap
    }

    fun shareCurrentCode(context: Context) {
        currentBitmap?.let { bitmap ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val file = saveBitmapToCache(context, bitmap)
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        file
                    )
                    shareImage(context, uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun saveCurrentCode(context: Context) {
        currentBitmap?.let { bitmap ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val fileName = "barcode_${System.currentTimeMillis()}.png"
                    val file = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        fileName
                    )
                    FileOutputStream(file).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun saveBitmapToCache(context: Context, bitmap: Bitmap): File {
        val file = File(context.cacheDir, "temp_barcode.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file
    }

    private fun shareImage(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share barcode"))
    }
}