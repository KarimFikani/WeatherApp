package com.karimfikani.weatherapp.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.karimfikani.weatherapp.core.RxBus
import com.karimfikani.weatherapp.core.RxEvent
import com.karimfikani.weatherapp.core.UiState
import com.karimfikani.weatherapp.databinding.SearchFragmentBinding
import com.karimfikani.weatherapp.search.data.SearchUiModel
import com.karimfikani.weatherapp.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.uiState.collect {
                    when (it) {
                        is UiState.Idle -> {
                            resetView()
                        }

                        is UiState.Loading -> {
                            showLoadingProgress()
                        }

                        is UiState.Error -> {
                            hideLoadingProgress()
                            showErrorMessage()
                        }

                        is UiState.Success -> {
                            resetView()
                            showSearchResults(it.data)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBar.text.clear()
        binding.searchBar.requestFocus()

        binding.searchBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                searchViewModel.fetchLocationByName(v.text.toString())
            }

            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun resetView() {
        hideLoadingProgress()
        hideErrorMessage()
    }

    private fun showLoadingProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingProgress() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showErrorMessage() {
        binding.errorMessage.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        binding.errorMessage.visibility = View.GONE
    }

    private val itemClickListener: ItemClickListener = object : ItemClickListener {
        override fun onClick(text: String) {
            RxBus.publish(RxEvent.SearchItemClicked(text))
            parentFragmentManager.popBackStack()
        }
    }

    private fun showSearchResults(data: SearchUiModel) {
        val adapter = SearchAdapter(data.locations)
        adapter.setOnItemClickListener(itemClickListener)

        binding.searchResults.adapter = adapter
        binding.searchResults.setHasFixedSize(true)
        binding.searchResults.visibility = View.VISIBLE
    }

    companion object {
        const val TAG = "SearchFragment"

        fun instance() = SearchFragment()
    }
}
