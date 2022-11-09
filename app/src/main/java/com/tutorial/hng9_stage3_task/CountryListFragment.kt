package com.tutorial.hng9_stage3_task

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tutorial.hng9_stage3_task.arch.CountriesViewModel
import com.tutorial.hng9_stage3_task.databinding.FragmentCountryInfoBinding
import com.tutorial.hng9_stage3_task.databinding.FragmentCountryListBinding
import com.tutorial.hng9_stage3_task.models.Resource
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch

class CountryListFragment : Fragment() {
    private var _binding: FragmentCountryListBinding? = null
    private val binding get() = _binding!!
    private val viewModel:CountriesViewModel by viewModels()
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
        lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.getAllCountries()
            }
            viewModel.allCountriesFlow.collect { resource->
                when(resource){
                    is Resource.Successful->{
                        binding.progressBar.isVisible = false
                        binding.errorText.text = resource.data.toString()
                    }
                    is Resource.Failure->{
                        binding.progressBar.isVisible = false
                        binding.errorText.text =resource.msg
                    }
                    is Resource.Loading->{
                        binding.progressBar.isVisible = true
                    }
                    is Resource.Empty->{
                        binding.errorText.text = "Result is empty"
                    }
                }

            }
        }

    }
}