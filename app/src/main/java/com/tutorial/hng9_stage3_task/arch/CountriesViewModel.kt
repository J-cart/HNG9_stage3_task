package com.tutorial.hng9_stage3_task.arch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorial.hng9_stage3_task.models.MapperNew
import com.tutorial.hng9_stage3_task.models.Resource
import com.tutorial.hng9_stage3_task.models.main.Countries
import com.tutorial.hng9_stage3_task.models.main.CountriesItem
import com.tutorial.hng9_stage3_task.utils.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CountriesViewModel : ViewModel() {


    private var _allCountriesFlow = MutableStateFlow<Resource<List<MapperNew>>>(Resource.Loading())
    val allCountriesFlow get() = _allCountriesFlow.asStateFlow()

    private var _filteredList = MutableStateFlow<Resource<List<MapperNew>>>(Resource.Loading())
    val filteredList get() = _filteredList.asStateFlow()

     var rawList = MutableStateFlow<Resource<List<CountriesItem>>>(Resource.Empty())

//private var _allCountriesFlow = MutableStateFlow<Resource<Countries>>(Resource.Loading())
//    val allCountriesFlow get() = _allCountriesFlow.asStateFlow()


    suspend fun getAllCountries() {
        Log.d("Result", "inside getAllCountries")
        viewModelScope.launch {
            try {
                val result = ApiService.retrofitApiService.getAllCountries()
                when {
                    result.isSuccessful -> {
                        result.body()?.let { countries ->
                            if (countries.isNotEmpty()) {

                                _allCountriesFlow.value = Resource.Successful(dataMapper(countries))
                                rawList.value = Resource.Successful(countries)
//                                    Resource.Successful(countries)

                            } else {
                                _allCountriesFlow.value = Resource.Empty()
                                rawList.value = Resource.Empty()
                            }
                        }
                    }
                    else -> {
                        _allCountriesFlow.value = Resource.Failure(result.errorBody().toString())
                        rawList.value = Resource.Empty()
                    }
                }
            } catch (e: Exception) {
                _allCountriesFlow.value = Resource.Failure(e.toString())
                rawList.value = Resource.Empty()
                Log.d("Inside try catch 2", "$e")
            }


        }
    }

    suspend fun getFiltered(continents: List<String>, timeZones: List<String>) {
        Log.d("filter Result", "inside filters method")
        viewModelScope.launch {
            rawList.collect { resource ->
                Log.d("filter Result", "raw List${rawList.value}")
                when (resource) {
                    is Resource.Successful -> {
                        resource.data?.let {
                            val filter = filteredResult(continents, timeZones, it)
                            Log.d("filter Result", "filtered item $filter")
                            if (filter.isNotEmpty()) {
                                _filteredList.value =
                                    Resource.Successful(dataMapper(filter))
                            } else {
                                _filteredList.value = Resource.Empty()
                            }

                        }
                    }
                    is Resource.Empty -> {
                        _filteredList.value = Resource.Empty()
                    }
                    else -> Unit
                }

            }
        }
    }

    private fun dataMapper(data: List<CountriesItem>): List<MapperNew> {

        val list = CharRange('A', 'Z').toMutableList()
        val mainList = mutableListOf<MapperNew>()
        list.forEach { char ->
            val newList = mutableListOf<CountriesItem>()
            data.forEach { countriesItem ->
                val case = countriesItem.name?.common?.startsWith(
                    prefix = char.toString(),
                    ignoreCase = true
                )
                if (case == true) {
                    newList.add(countriesItem)
                }
            }
            mainList.add(MapperNew(char.toString(), newList))
        }

        Log.d("MappingItems", "$mainList")

        return mainList
    }

    private fun filteredResult(
        continents: List<String>,
        timeZone: List<String>,
        data: List<CountriesItem>
    ): List<CountriesItem> {
        val list = mutableListOf<CountriesItem>()
        val continent = data.filter { items ->
            continents.any { continent ->
                items.continents?.contains(continent) ?: false
            }
        }

        val time = data.filter { items ->
            timeZone.any { time ->
                items.timezones?.contains(time) ?: false
            }
        }
        list.addAll(continent)
        list.addAll(time)
        Log.d("filter viewModel","$list")
        return list
    }


}

