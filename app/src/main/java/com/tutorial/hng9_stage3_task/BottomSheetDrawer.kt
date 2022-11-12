package com.tutorial.hng9_stage3_task


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tutorial.hng9_stage3_task.arch.CountriesViewModel
import com.tutorial.hng9_stage3_task.databinding.BottomSheetDrawerBinding
import kotlinx.coroutines.launch

class BottomSheetDrawer : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDrawerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CountriesViewModel>()
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
       doTimeOp(binding.gmt3Checkbox,"UTC+03:00")
       doTimeOp(binding.gmt2Checkbox,"UTC+02:00")
       doTimeOp(binding.gmt1Checkbox,"UTC+01:00")
       doTimeOp(binding.gmt0Checkbox,"UTC 00:00")
       doTimeOp(binding.gmt01Checkbox,"UTC-01:00")
       doTimeOp(binding.gmt02Checkbox,"UTC-02:00")
       doTimeOp(binding.gmt03checkbox,"UTC-03:00")

       doContOp(binding.africaCheckbox,"Africa")
       doContOp(binding.antarcticaCheckbox,"Antarctica")
       doContOp(binding.asiaCheckbox,"Asia")
       doContOp(binding.europeCheckbox,"Europe")
       doContOp(binding.northAmCheckbox,"North America")
       doContOp(binding.oceaniaCheckbox,"Oceania")
       doContOp(binding.southAmcheckbox,"South America")

        binding.apply {
            resetBtn.setOnClickListener {
                timeFilterList.clear()
                continentFilterList.clear()
            }
            resultBtn.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.getFiltered(continentFilterList,timeFilterList)
                }

            }
            timeZoneText.setOnClickListener {
                timeToggle = !timeToggle
               if (timeToggle){
                   timeCardView.visibility = View.VISIBLE
                   timeZoneText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                       0,
                       0,
                       R.drawable.ic_baseline_keyboard_arrow_up_24,
                       0
                   )
               }else{
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
               if (continentToggle){
                   conCardView.visibility = View.VISIBLE
                   continentText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                       0,
                       0,
                       R.drawable.ic_baseline_keyboard_arrow_up_24,
                       0
                   )
               }else{
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
                isChecked-> {
                    if (!checkIfExistTime(text)) {
                        timeFilterList.add(text)
                    }
                }
                else -> {
                    timeFilterList.remove(text)
                }
            }

        }
    }
    private fun doContOp(view: CheckBox, text: String) {
        view.isChecked = continentFilterList.contains(text)
        view.setOnCheckedChangeListener { _, isChecked ->
            when {
                isChecked-> {
                    if (!checkIfExistContinent(text)) {
                        continentFilterList.add(text)
                    }
                }
                else -> {
                    continentFilterList.remove(text)
                }
            }

        }
    }
}