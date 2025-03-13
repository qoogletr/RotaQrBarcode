import android.content.Context
import android.util.AttributeSet
import com.google.android.material.switchmaterial.SwitchMaterial

class AnimatedSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SwitchMaterial(context, attrs, defStyleAttr) {

    override fun setChecked(checked: Boolean) {
        if (isChecked != checked) {
            if (width > 0) {
                animate()
                    .alpha(0f)
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .setDuration(150)
                    .withEndAction {
                        super.setChecked(checked)
                        animate()
                            .alpha(1f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(150)
                            .start()
                    }
                    .start()
            } else {
                super.setChecked(checked)
            }
        }
    }
}