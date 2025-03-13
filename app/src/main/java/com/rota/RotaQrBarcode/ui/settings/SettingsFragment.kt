package com.rota.RotaQrBarcode.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.rota.RotaQrBarcode.BuildConfig
import com.rota.RotaQrBarcode.R
import com.rota.RotaQrBarcode.databinding.FragmentSettingsBinding
import com.rota.RotaQrBarcode.utils.PreferencesManager
import kotlinx.coroutines.launch
import java.util.*

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        loadCurrentSettings()
        setupListeners()
        setupVersionInfo()
    }

    private fun loadCurrentSettings() {
        binding.apply {
            // Server settings
            serverAddressInput.setText(PreferencesManager.serverAddress)
            serverPortInput.setText(PreferencesManager.serverPort.toString())
            serverEndpointInput.setText(PreferencesManager.serverEndpoint)
            
            // Scanner settings
            autoSendSwitch.isChecked = PreferencesManager.autoSendEnabled
            vibrationSwitch.isChecked = PreferencesManager.vibrateEnabled
            soundSwitch.isChecked = PreferencesManager.playSoundEnabled
            
            // Theme settings
            darkModeSwitch.isChecked = PreferencesManager.darkModeEnabled
            
            // Format settings
            when (PreferencesManager.sendFormat) {
                SendFormat.PLAIN_TEXT -> formatRadioGroup.check(R.id.formatPlainText)
                SendFormat.JSON_SIMPLE -> formatRadioGroup.check(R.id.formatJsonSimple)
                SendFormat.JSON_DETAILED -> formatRadioGroup.check(R.id.formatJsonDetailed)
                SendFormat.JSON_WITH_DEVICE -> formatRadioGroup.check(R.id.formatJsonWithDevice)
                SendFormat.XML -> formatRadioGroup.check(R.id.formatXml)
                SendFormat.CSV -> formatRadioGroup.check(R.id.formatCsv)
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            saveButton.setOnClickListener {
                saveSettings()
            }

            testButton.setOnClickListener {
                testConnection()
            }

            darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
                PreferencesManager.darkModeEnabled = isChecked
                requireActivity().recreate()
            }
        }
    }

    private fun setupVersionInfo() {
        binding.versionText.text = getString(
            R.string.version_info,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE,
            getLastUpdateTime()
        )
    }

    private fun saveSettings() {
        PreferencesManager.apply {
            serverAddress = binding.serverAddressInput.text.toString()
            serverPort = binding.serverPortInput.text.toString().toIntOrNull() ?: 8080
            serverEndpoint = binding.serverEndpointInput.text.toString()
            
            autoSendEnabled = binding.autoSendSwitch.isChecked
            vibrateEnabled = binding.vibrationSwitch.isChecked
            playSoundEnabled = binding.soundSwitch.isChecked
            
            sendFormat = when (binding.formatRadioGroup.checkedRadioButtonId) {
                R.id.formatPlainText -> SendFormat.PLAIN_TEXT
                R.id.formatJsonSimple -> SendFormat.JSON_SIMPLE
                R.id.formatJsonDetailed -> SendFormat.JSON_DETAILED
                R.id.formatJsonWithDevice -> SendFormat.JSON_WITH_DEVICE
                R.id.formatXml -> SendFormat.XML
                R.id.formatCsv -> SendFormat.CSV
                else -> SendFormat.JSON_SIMPLE
            }
        }

        Snackbar.make(binding.root, R.string.settings_saved, Snackbar.LENGTH_SHORT).show()
    }

    private fun testConnection() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.testButton.isEnabled = false
            binding.testProgressBar.isVisible = true

            try {
                val result = viewModel.testConnection(
                    binding.serverAddressInput.text.toString(),
                    binding.serverPortInput.text.toString().toIntOrNull() ?: 8080,
                    binding.serverEndpointInput.text.toString()
                )

                if (result) {
                    showTestResult(true, getString(R.string.connection_success))
                } else {
                    showTestResult(false, getString(R.string.connection_failed))
                }
            } catch (e: Exception) {
                showTestResult(false, getString(R.string.connection_error, e.message))
            } finally {
                binding.testButton.isEnabled = true
                binding.testProgressBar.isVisible = false
            }
        }
    }

    private fun showTestResult(success: Boolean, message: String) {
        val icon = if (success) R.drawable.ic_success else R.drawable.ic_error
        val color = if (success) R.color.success_color else R.color.error_color

        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(requireContext().getColor(color))
            .show()
    }

    private fun getLastUpdateTime(): String {
        return try {
            val date = Date(BuildConfig.TIMESTAMP)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            sdf.format(date)
        } catch (e: Exception) {
            "2025-03-07 20:41:18" // Current build time
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}