package com.tutorial.hng9_stage3_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.tutorial.hng9_stage3_task.arch.CountriesViewModel
import com.tutorial.hng9_stage3_task.databinding.FragmentCountryListBinding
import com.tutorial.hng9_stage3_task.models.Resource
import kotlinx.coroutines.*

class CountryListFragment : Fragment() {
    private var _binding: FragmentCountryListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CountriesViewModel by viewModels()
    private val countriesAdapter by lazy { CountriesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCountryListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.countriesRv.adapter = countriesAdapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.getAllCountries()
            }
        }
        setUpCollector()

        binding.errorText.setOnClickListener {
            findNavController().navigate(R.id.countryInfoFragment)
        }
    }

    private fun setUpCollector(){
        lifecycleScope.launch {
            viewModel.allCountriesFlow.collect { resource ->
                when (resource) {
                    is Resource.Successful -> {
                        binding.progressBar.isVisible = false
                        binding.errorText.text = resource.data?.toString()
                    }
                    is Resource.Failure -> {
                        binding.progressBar.isVisible = false
                        binding.errorText.text = resource.msg
                    }
                    is Resource.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is Resource.Empty -> {
                        binding.errorText.text = "Result is empty"
                    }
                }

            }
        }
    }
}