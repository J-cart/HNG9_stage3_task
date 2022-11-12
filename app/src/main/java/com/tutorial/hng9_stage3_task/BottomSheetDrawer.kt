package com.tutorial.hng9_stage3_task


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tutorial.hng9_stage3_task.arch.CountriesViewModel
import com.tutorial.hng9_stage3_task.databinding.BottomSheetDrawerBinding
import com.tutorial.hng9_stage3_task.utils.RegisterClicks
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BottomSheetDrawer : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDrawerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel:CountriesViewModel
    private var timeFilterList = mutableListOf<String>()
    private var continentFilterList = mutableListOf<String>()
    private var timeToggle = false
    private var continentToggle = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment())[CountriesViewModel::class.java]

        binding.apply {
            doTimeOp(gmt3Checkbox, "UTC+03:00")
            doTimeOp(gmt2Checkbox, "UTC+02:00")
            doTimeOp(gmt1Checkbox, "UTC+01:00")
            doTimeOp(gmt0Checkbox, "UTC 00:00")
            doTimeOp(gmt01Checkbox, "UTC-01:00")
            doTimeOp(gmt02Checkbox, "UTC-02:00")
            doTimeOp(gmt03checkbox, "UTC-03:00")

            doContOp(africaCheckbox, "Africa")
            doContOp(antarcticaCheckbox, "Antarctica")
            doContOp(asiaCheckbox, "Asia")
            doContOp(europeCheckbox, "Europe")
            doContOp(northAmCheckbox, "North America")
            doContOp(oceaniaCheckbox, "Oceania")
            doContOp(southAmcheckbox, "South America")
            resetBtn.setOnClickListener {
                timeFilterList.clear()
                continentFilterList.clear()
            }
            resultBtn.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    Log.d("checkBox","result btn clicked")
                    viewModel.getFiltered(continentFilterList, timeFilterList)
                    viewModel.filteredList.collect{
                        Log.d("checkBox","filtered list-- $it  flow collected")
                    }
                }

            }
            timeZoneText.setOnClickListener {
                timeToggle = !timeToggle
                if (timeToggle) {
                    timeCardView.visibility = View.VISIBLE
                    timeZoneText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_keyboard_arrow_up_24,
                        0
                    )
                } else {
                    timeCardView.visibility = View.GONE
                    timeZoneText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_keyboard_arrow_down_24,
                        0
                    )
                }
            }
            continentText.setOnClickListener {
                continentToggle = !continentToggle
                if (continentToggle) {
                    conCardView.visibility = View.VISIBLE
                    continentText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_keyboard_arrow_up_24,
                        0
                    )
                } else {
                    conCardView.visibility = View.GONE
                    continentText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_keyboard_arrow_down_24,
                        0
                    )
                }
            }
        }

    }

    private fun checkIfExistTime(item: String): Boolean {
        return timeFilterList.contains(item)
    }

    private fun checkIfExistContinent(item: String): Boolean {
        return continentFilterList.contains(item)
    }

    private fun doTimeOp(view: CheckBox, text: String) {
        view.isChecked = timeFilterList.contains(text)
        view.setOnCheckedChangeListener { _, isChecked ->
            when {
                isChecked -> {
                    if (!checkIfExistTime(text)) {
                        timeFilterList.add(text)
                        Log.d("time checkBox", "$text is selected-- $timeFilterList")
                    }
                }
                else -> {
                    timeFilterList.remove(text)
                    Log.d("time checkBox", "$text is removed-- $timeFilterList")
                }
            }

        }
    }

    private fun doContOp(view: CheckBox, text: String) {
        view.isChecked = continentFilterList.contains(text)
        view.setOnCheckedChangeListener { _, isChecked ->
            when {
                isChecked -> {
                    if (!checkIfExistContinent(text)) {
                        continentFilterList.add(text)
                        Log.d(" cont checkBox", "$text is selected-- $continentFilterList")
                    }
                }
                else -> {
                    continentFilterList.remove(text)
                    Log.d("checkBox", "$text is selected-- $continentFilterList")
                }
            }

        }
    }
}