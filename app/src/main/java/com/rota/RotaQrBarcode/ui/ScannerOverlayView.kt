import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.barcodescanner.R

class ScannerOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val scannerRect = RectF()
    private val cornerLength = 60f
    private val cornerStrokeWidth = 8f
    private var maskColor = Color.parseColor("#80000000")
    private val cornerColor = Color.WHITE
    
    private val scanLine = RectF()
    private val scanLinePaint = Paint().apply {
        style = Paint.Style.FILL
        shader = LinearGradient(
            0f, 0f, 0f, 100f,
            intArrayOf(Color.TRANSPARENT, Color.WHITE, Color.WHITE, Color.TRANSPARENT),
            floatArrayOf(0f, 0.3f, 0.7f, 1f),
            Shader.TileMode.CLAMP
        )
    }
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = cornerColor
        style = Paint.Style.STROKE
        strokeWidth = cornerStrokeWidth
    }
    
    private val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = maskColor
        style = Paint.Style.FILL
    }

    init {
        // Tarama çizgisi animasyonunu başlat
        val animation = AnimationUtils.loadAnimation(context, R.anim.scan_line)
        startAnimation(animation)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Maskeleme alanını çiz
        canvas.drawRect(0f, 0f, width.toFloat(), scannerRect.top, maskPaint)
        canvas.drawRect(0f, scannerRect.bottom, width.toFloat(), height.toFloat(), maskPaint)
        canvas.drawRect(0f, scannerRect.top, scannerRect.left, scannerRect.bottom, maskPaint)
        canvas.drawRect(scannerRect.right, scannerRect.top, width.toFloat(), scannerRect.bottom, maskPaint)

        // Köşeleri çiz
        // Sol üst köşe
        canvas.drawLine(scannerRect.left, scannerRect.top, scannerRect.left + cornerLength, scannerRect.top, paint)
        canvas.drawLine(scannerRect.left, scannerRect.top, scannerRect.left, scannerRect.top + cornerLength, paint)

        // Sağ üst köşe
        canvas.drawLine(scannerRect.right - cornerLength, scannerRect.top, scannerRect.right, scannerRect.top, paint)
        canvas.drawLine(scannerRect.right, scannerRect.top, scannerRect.right, scannerRect.top + cornerLength, paint)

        // Sol alt köşe
        canvas.drawLine(scannerRect.left, scannerRect.bottom - cornerLength, scannerRect.left, scannerRect.bottom, paint)
        canvas.drawLine(scannerRect.left, scannerRect.bottom, scannerRect.left + cornerLength, scannerRect.bottom, paint)

        // Sağ alt köşe
        canvas.drawLine(scannerRect.right - cornerLength, scannerRect.bottom, scannerRect.right, scannerRect.bottom, paint)
        canvas.drawLine(scannerRect.right, scannerRect.bottom - cornerLength, scannerRect.right, scannerRect.bottom, paint)

        // Tarama çizgisini çiz
        canvas.drawRect(scanLine, scanLinePaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        
        // Tarama alanını hesapla
        val scannerSize = Math.min(w, h) * 0.7f
        val left = (w - scannerSize) / 2
        val top = (h - scannerSize) / 2
        scannerRect.set(left, top, left + scannerSize, top + scannerSize)
        
        // Tarama çizgisi boyutlarını güncelle
        scanLine.set(
            scannerRect.left,
            scannerRect.top,
            scannerRect.right,
            scannerRect.top + 10f
        )
    }
}