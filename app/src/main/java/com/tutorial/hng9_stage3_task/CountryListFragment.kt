package com.tutorial.hng9_stage3_task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tutorial.hng9_stage3_task.arch.CountriesViewModel
import com.tutorial.hng9_stage3_task.databinding.FragmentCountryListBinding
import com.tutorial.hng9_stage3_task.models.Resource
import com.tutorial.hng9_stage3_task.models.main.CountriesItem
import com.tutorial.hng9_stage3_task.utils.ParentAdapter
import com.tutorial.hng9_stage3_task.utils.RegisterClicks
import kotlinx.coroutines.*

class CountryListFragment : Fragment(), RegisterClicks {
    private var _binding: FragmentCountryListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CountriesViewModel by viewModels()
//    private val countriesAdapter by lazy { CountriesAdapter() }
    private val countriesAdapter by lazy { ParentAdapter(this) }
//    private val childAdapter by lazy { ChildAdapter() }

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


    }

    private fun setUpCollector(){
        lifecycleScope.launch {
            viewModel.allCountriesFlow.collect { resource ->
                when (resource) {
                    is Resource.Successful -> {
                        binding.progressBar.isVisible = false
                        resource.data?.let { countriesAdapter.submitList(it) }
                        binding.errorText.setOnClickListener {
                            val route = CountryListFragmentDirections.actionCountryListFragmentToCountryInfoFragment(null)
                            findNavController().navigate(route)
                        }
                        binding.filterBtn.setOnClickListener {
                            BottomSheetDrawer().show(childFragmentManager,"BOTTOM_SHEET")
                        }
                    }
                    is Resource.Failure -> {
                        binding.progressBar.isVisible = binding.countriesRv.layoutManager?.itemCount!! <= 0

                        binding.errorText.text = resource.msg
                        Snackbar.make(requireView(), "ERROR! Try refreshing news", Snackbar.LENGTH_LONG)
                            .setAction("Refresh") { lifecycleScope.launch { viewModel.getAllCountries() } }.show()
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

    private fun setUpFilterCollector(){
        lifecycleScope.launch {
            viewModel.filteredList.collect { resource ->
                when (resource) {
                    is Resource.Successful -> {
                        binding.progressBar.isVisible = false

                        resource.data?.let { countriesAdapter.submitList(it) }
                        binding.errorText.setOnClickListener {
                            val route = CountryListFragmentDirections.actionCountryListFragmentToCountryInfoFragment(null)
                            findNavController().navigate(route)
                        }
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


    override fun onChildClicked(data: CountriesItem) {
        val route = CountryListFragmentDirections.actionCountryListFragmentToCountryInfoFragment(data)
        findNavController().navigate(route)    }
}