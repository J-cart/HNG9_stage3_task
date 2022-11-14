package com.tutorial.hng9_stage3_task.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.tutorial.hng9_stage3_task.R
import com.tutorial.hng9_stage3_task.arch.CountriesViewModel
import com.tutorial.hng9_stage3_task.databinding.FragmentCountryListBinding
import com.tutorial.hng9_stage3_task.models.MapperNew
import com.tutorial.hng9_stage3_task.models.Resource
import com.tutorial.hng9_stage3_task.models.main.CountriesItem
import com.tutorial.hng9_stage3_task.utils.ParentAdapter
import com.tutorial.hng9_stage3_task.utils.RegisterClicks
import kotlinx.coroutines.launch

class CountryListFragment : Fragment(), RegisterClicks {
    private var _binding: FragmentCountryListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CountriesViewModel by viewModels()
    private var darkModeToggle = false

    //    private val countriesAdapter by lazy { ParentAdapter(this) }
    private lateinit var countriesAdapter: ParentAdapter

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

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getAllCountries()
            }
        }
        binding.modeBtn.setOnClickListener {
            darkModeToggle = !darkModeToggle
            if (darkModeToggle) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
        }
        binding.modeBtn.setImageResource(
            if (darkModeToggle) {
                R.drawable.ic_baseline_wb_sunny_24

            } else {
                R.drawable.ic_baseline_dark_mode_24

            }
        )

        setUpCollector()
        setUpSpinner()
        binding.toSpinner.setSelection(5)
        binding.langBtn.setOnClickListener { showDialog() }
    }

    private fun setUpCollector(translate: Boolean = false, language: String = "Eng") {
        countriesAdapter = ParentAdapter(this, translate, language)
        lifecycleScope.launch {
            binding.countriesRv.adapter = countriesAdapter
            viewModel.allCountriesFlow.collect { resource ->
                when (resource) {
                    is Resource.Successful -> {
                        binding.progressBar.isVisible = false
                        resource.data?.let {
                            countriesAdapter.submitList(it)
                            setUpSearch(it)
                        }
                        binding.errorText.setOnClickListener {
                            val route =
                                CountryListFragmentDirections.actionCountryListFragmentToCountryInfoFragment(
                                    null
                                )
                            findNavController().navigate(route)
                        }
                        binding.filterBtn.setOnClickListener {
                            BottomSheetDrawer().show(
                                childFragmentManager,
                                "BOTTOM_SHEET"
                            )
                        }
                    }
                    is Resource.Failure -> {
                        binding.progressBar.isVisible =
                            binding.countriesRv.layoutManager?.itemCount!! <= 0

                        binding.errorText.text = resource.msg
                        Snackbar.make(
                            requireView(),
                            "ERROR! Try refreshing page",
                            4000
                        )
                            .setAction("Refresh") { lifecycleScope.launch { viewModel.getAllCountries() } }
                            .show()
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

    private fun setUpFilterCollector() {
        lifecycleScope.launch {
            viewModel.filteredList.collect { resource ->
                when (resource) {
                    is Resource.Successful -> {
                        binding.progressBar.isVisible = false

                        resource.data?.let {
                            countriesAdapter.submitList(it)
                        }
                        binding.errorText.setOnClickListener {
                            val route =
                                CountryListFragmentDirections.actionCountryListFragmentToCountryInfoFragment(
                                    null
                                )
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

    private fun setUpSearch(items: List<MapperNew>) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val itemQuery = setUpSearchCollector(items, query)
                    Log.d("search query", "$itemQuery")
                    if (itemQuery.isNotEmpty()) {

                        countriesAdapter.submitList(itemQuery)
                    } else {
                        Snackbar.make(
                            requireView(),
                            "ERROR! check your query and retry",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    return true
                } ?: Log.d("search query", "No query entered")
                setUpCollector()
                Snackbar.make(
                    requireView(),
                    "ERROR!No query entered check your query and retry",
                    Snackbar.LENGTH_LONG
                ).show()
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

    private fun setUpSearchCollector(items: List<MapperNew>, query: String): List<MapperNew> {
        return items.filter { mappedItems ->
            mappedItems.items.any { countriesItem ->
//                countriesItem.name?.common?.contains(query) ?: false
                countriesItem.name?.common?.lowercase()
                    ?.contains(query) ?: false || countriesItem.name?.common?.uppercase()
                    ?.contains(query) ?: false || countriesItem.name?.common?.contains(query) ?: false
            }
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Coming Soon")
            setMessage("This feature is not available -- yet.")
            setPositiveButton("OK") { d, _ ->
                d.dismiss()
            }
            show()
        }
    }

    fun setUpSpinner() {
        binding.toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                when (pos) {
                    0 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    1 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    2 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    3 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    4 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    5 -> {
                        setUpCollector(false)
                        binding.toSpinner.setSelection(pos)
                    }
                    6 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    7 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    8 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    9 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    10 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    11 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    12 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    13 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    14 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    15 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    16 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    17 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    18 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    19 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    20 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    21 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    22 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    23 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                    24 -> {
                        setUpCollector(true, binding.toSpinner.selectedItem.toString())
                        binding.toSpinner.setSelection(pos)
                    }
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun onChildClicked(data: CountriesItem) {
        val route =
            CountryListFragmentDirections.actionCountryListFragmentToCountryInfoFragment(
                data
            )
        findNavController().navigate(route)
    }

    override fun onFilterClicked() {
        setUpFilterCollector()
    }

    override fun onResetClicked() {
        setUpCollector()
    }
}