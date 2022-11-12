package com.tutorial.hng9_stage3_task.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tutorial.hng9_stage3_task.R
import com.tutorial.hng9_stage3_task.arch.CountriesViewModel
import com.tutorial.hng9_stage3_task.databinding.BottomSheetDrawerBinding
import com.tutorial.hng9_stage3_task.utils.RegisterClicks
import kotlinx.coroutines.launch

class BottomSheetDrawer : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDrawerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CountriesViewModel
    private lateinit var onRegisterClicks: RegisterClicks
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
        onRegisterClicks = requireParentFragment() as RegisterClicks

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
                viewModel.clearTimeFilter()
                viewModel.clearContFilter()
                onRegisterClicks.onResetClicked()
                this@BottomSheetDrawer.dismiss()
            }
            resultBtn.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    Log.d("checkBox", "result btn clicked")

                    viewModel.getFiltered(continentFilterList, timeFilterList)
                    onRegisterClicks.onFilterClicked()
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


    private fun doTimeOp(view: CheckBox, text: String) {
        lifecycleScope.launch {
            viewModel.checkIfExistTimeFilter(text).collect { exist ->
                view.isChecked = exist
            }

        }
        view.setOnCheckedChangeListener { _, isChecked ->
            when {
                isChecked -> {
                    lifecycleScope.launch {
                        viewModel.checkIfExistTimeFilter(text).collect { exist ->
                            if (!exist) {
                                viewModel.updateTimeFilterList(text, true)
                                Log.d(
                                    "time checkBox",
                                    "$text is selected-- ${viewModel.timeFilterList.value}"
                                )
                            }
                        }

                    }
                }
                else -> {
                    viewModel.updateTimeFilterList(text, false)
                    timeFilterList.remove(text)
                    Log.d("time checkBox", "$text is removed-- ${viewModel.timeFilterList.value}")
                }
            }

        }
    }

    private fun doContOp(view: CheckBox, text: String) {
        lifecycleScope.launch {
            viewModel.checkIfExistContFilter(text).collect { exist ->
                view.isChecked = exist
            }

        }
        view.setOnCheckedChangeListener { _, isChecked ->
            when {
                isChecked -> {
                    lifecycleScope.launch {
                        viewModel.checkIfExistContFilter(text).collect { exist ->
                            if (!exist) {
                                viewModel.updateContFilterList(text, true)
                                Log.d(
                                    "cont checkBox",
                                    "$text is selected-- ${viewModel.continentFilterList.value}"
                                )
                            }
                        }

                    }
                }
                else -> {
                    viewModel.updateContFilterList(text, false)
                    Log.d(
                        "cont checkBox",
                        "$text is selected-- ${viewModel.continentFilterList.value}"
                    )
                }
            }

        }
    }
}