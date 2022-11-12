package com.tutorial.hng9_stage3_task

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
            countriesItem.flags?.svg?.let { imageList.add(it) }
            countriesItem.coatOfArms?.png?.let { imageList.add(it) }
            countriesItem.coatOfArms?.svg?.let { imageList.add(it) }


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

                continentText.text = countriesItem.continents?.get(0)
                offNameText.text = countriesItem.name?.official
                denonymText.text =
                    "F: ${countriesItem.demonyms?.eng?.f}, M:${countriesItem.demonyms?.eng?.m}"
                startOfWkText.text = countriesItem.startOfWeek


                bordersText.text = countriesItem.borders?.joinToString(",")
                areaText.text = countriesItem.area.toString()
                coordinatesText.text =
                    countriesItem.latlng.toString().replace("[", "").replace("]", "")
                capCoordText.text = countriesItem.capitalInfo?.latlng?.get(0)?.toString()

                timeZoneText.text =
                    countriesItem.timezones.toString().replace("[", "").replace("]", "")
                nccText.text = countriesItem.ccn3
                dialingCodeText.text =
                    countriesItem.idd?.root + (countriesItem.idd?.suffixes)?.toString()
                        ?.replace("[", "")?.replace("]", "")
                drivingSideText.text = countriesItem.car?.side


                nextBtn.setOnClickListener {
                    index = (index + 1) % imageList.size
                    imageAssets.load(imageList[index])
                    Log.d("imageList", index.toString())
                }
            }
        } ?: "No data passed"
    }


}