import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodescanner.R
import com.example.barcodescanner.data.ScannedCode
import com.example.barcodescanner.databinding.ItemScannedCodeBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter : ListAdapter<ScannedCode, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemScannedCodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemScannedCodeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        fun bind(code: ScannedCode) {
            binding.apply {
                valueText.text = code.value
                typeText.text = code.type
                timestampText.text = dateFormat.format(Date(code.timestamp))
                
                statusChip.apply {
                    text = if (code.serverSent) {
                        context.getString(R.string.sent)
                    } else {
                        context.getString(R.string.pending)
                    }
                    setChipBackgroundColorResource(
                        if (code.serverSent) R.color.success_color else R.color.accent
                    )
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ScannedCode>() {
            override fun areItemsTheSame(oldItem: ScannedCode, newItem: ScannedCode): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ScannedCode, newItem: ScannedCode): Boolean {
                return oldItem == newItem
            }
        }
    }
}