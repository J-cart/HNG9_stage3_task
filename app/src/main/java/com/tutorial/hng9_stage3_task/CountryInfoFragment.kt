package com.tutorial.hng9_stage3_task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.tutorial.hng9_stage3_task.databinding.FragmentCountryInfoBinding


class CountryInfoFragment : Fragment() {

    private var _binding:FragmentCountryInfoBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<CountryInfoFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCountryInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.countryItem?.let { countriesItem ->
            binding.receiverText.text = countriesItem.toString()
        }?: "No data passed"
    }


}