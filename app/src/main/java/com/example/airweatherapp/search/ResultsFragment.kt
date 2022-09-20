package com.example.airweatherapp.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.airweatherapp.R
import com.example.airweatherapp.databinding.FragmentFirstBinding
import com.example.airweatherapp.main_activity.MainActivityViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ResultsFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * View model
     */
    private val viewModel: ResultsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = activity?.intent
        // observe vm
        viewModel.place.observe(this) {
            binding.textviewFirst.text = it.name
        }

        // get search intent
        if (intent?.action == Intent.ACTION_SEARCH) {
            val searchQuery = intent.getStringExtra(SearchManager.QUERY).toString()
            viewModel.getPlace(searchQuery)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_ResultsFragment_to_DetailFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}