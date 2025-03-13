package com.rota.RotaQrBarcode.ui.history

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rota.RotaQrBarcode.R
import com.rota.RotaQrBarcode.databinding.FragmentHistoryBinding
import com.rota.RotaQrBarcode.utils.showSnackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment(), MenuProvider {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: HistoryViewModel by viewModels { HistoryViewModel.Factory }
    private val historyAdapter = HistoryAdapter { code ->
        // Handle item click
        HistoryDetailBottomSheet.newInstance(code)
            .show(childFragmentManager, "HistoryDetail")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupMenu()
        setupObservers()
        
        // Update last sync time
        binding.lastSyncText.text = getString(
            R.string.last_sync,
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(Date(System.currentTimeMillis()))
        )
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.historyState.collect { state ->
                when (state) {
                    is HistoryState.Success -> {
                        binding.progressBar.isVisible = false
                        binding.emptyView.isVisible = state.codes.isEmpty()
                        historyAdapter.submitList(state.codes)
                    }
                    is HistoryState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is HistoryState.Error -> {
                        binding.progressBar.isVisible = false
                        binding.root.showSnackbar(state.message)
                    }
                }
            }
        }

        // Observe statistics
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.statistics.collect { stats ->
                binding.statsCard.apply {
                    totalScansText.text = stats.totalScans.toString()
                    sentScansText.text = stats.sentScans.toString()
                    pendingScansText.text = stats.pendingScans.toString()
                }
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.history_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_clear -> {
                showClearHistoryDialog()
                true
            }
            R.id.action_sync -> {
                viewModel.syncPendingCodes()
                true
            }
            else -> false
        }
    }

    private fun showClearHistoryDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm_delete)
            .setMessage(R.string.confirm_clear_history)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.clearHistory()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}