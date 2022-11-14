package com.tutorial.hng9_stage3_task.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.tutorial.hng9_stage3_task.databinding.FragmentCountryInfoBinding


class CountryInfoFragment : Fragment() {

    private var _binding: FragmentCountryInfoBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<CountryInfoFragmentArgs>()
    private var index = 0
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
            val imageList = mutableListOf<String>()
            countriesItem.flags?.png?.let { imageList.add(it) }
            countriesItem.coatOfArms?.png?.let { imageList.add(it) }


            Log.d("Countries", countriesItem.toString())
            Log.d("imageList", imageList.toString())
            binding.apply {
                pageName.text = countriesItem.name?.common
                backBtn.setOnClickListener {
                    findNavController().navigateUp()
                }

                popText.text = countriesItem.population.toString()
                regText.text = countriesItem.region
                capText.text = countriesItem.capital.toString().replace("[", "").replace("]", "")
                subRegText.text = countriesItem.subregion

                continentText.text = countriesItem.continents?.get(0)?: "Not Available"
                offNameText.text = countriesItem.name?.official ?: "Not Available"
                denonymText.text = String.format("F: %s,M: %s",countriesItem.demonyms?.eng?.f,countriesItem.demonyms?.eng?.m)
                startOfWkText.text = countriesItem.startOfWeek


                bordersText.text = countriesItem.borders?.joinToString(",")?: "Not Available"
                areaText.text = countriesItem.area.toString()
                coordinatesText.text =
                    countriesItem.latlng.toString().replace("[", "").replace("]", "")
                capCoordText.text = countriesItem.capitalInfo?.latlng?.get(0)?.toString() ?: "Not Available"

                timeZoneText.text = countriesItem.timezones?.let {
                    if (it.size >3){
                        String.format("%s ...",it.subList(0,2)).replace("[", "").replace("]", "")
                    } else{
                        String.format("%s",it).replace("[", "").replace("]", "")
                        it.toString().replace("[", "").replace("]", "")
                    }
                }?:"Not Available"


                nccText.text = countriesItem.ccn3
                dialingCodeText.text = String.format("%s%s", countriesItem.idd?.root , (countriesItem.idd?.suffixes)).replace("[", "").replace("]", "")
                drivingSideText.text = countriesItem.car?.side ?: "Not Available"

                imageAssets.load(imageList[index])
                nextBtn.setOnClickListener {
                    index = (index + 1) % imageList.size
                    imageAssets.load(imageList[index])
                    Log.d("imageList", index.toString())
                }
            }
        } ?: "No data passed"
    }


}