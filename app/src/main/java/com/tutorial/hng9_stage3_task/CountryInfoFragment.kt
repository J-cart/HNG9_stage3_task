package com.tutorial.hng9_stage3_task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tutorial.hng9_stage3_task.databinding.FragmentCountryInfoBinding


class CountryInfoFragment : Fragment() {

    private var _binding:FragmentCountryInfoBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCountryInfoBinding.inflate(inflater, container, false)
        return binding.root
    }


}