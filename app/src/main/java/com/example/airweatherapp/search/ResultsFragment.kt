package com.example.airweatherapp.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.airweatherapp.CommonViewModel
import com.example.airweatherapp.ResponseStatus
import com.example.airweatherapp.databinding.FragmentResultsBinding
import com.example.airweatherapp.weatherText

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ResultsFragment : Fragment() {

    private var _binding: FragmentResultsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * View model
     */
    private val viewModel: CommonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = activity?.intent
        // observe vm
        viewModel.place.observe(this) { place ->
            binding.apply {
                tvData.text = weatherText(place)
                tvHeading.text = place.name
            }
        }

        // observe status
        viewModel.status.observe(this) { status ->
            binding.apply {
                when (status) {
                    ResponseStatus.DONE -> {
                        loadingScreen.loadingScreen.visibility = View.GONE
                        mainScreen.visibility = View.VISIBLE
                    }
                    else -> {
                        loadingScreen.loadingScreen.visibility = View.VISIBLE
                        mainScreen.visibility = View.GONE
                    }
                }
            }
        }

        // get search intent
        if (intent?.action == Intent.ACTION_SEARCH) {
            val searchQuery = intent.getStringExtra(SearchManager.QUERY).toString()
            viewModel.getPlace(searchQuery)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}