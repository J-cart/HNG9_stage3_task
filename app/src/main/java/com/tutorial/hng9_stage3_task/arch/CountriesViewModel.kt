package com.tutorial.hng9_stage3_task.arch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorial.hng9_stage3_task.models.MapperNew
import com.tutorial.hng9_stage3_task.models.Resource
import com.tutorial.hng9_stage3_task.models.main.CountriesItem
import com.tutorial.hng9_stage3_task.utils.ApiService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CountriesViewModel : ViewModel() {


    private var _allCountriesFlow = MutableStateFlow<Resource<List<MapperNew>>>(Resource.Loading())
    val allCountriesFlow get() = _allCountriesFlow.asStateFlow()

    private var _filteredList = MutableStateFlow<Resource<List<MapperNew>>>(Resource.Loading())
    val filteredList get() = _filteredList.asStateFlow()

    private var rawList = MutableStateFlow<Resource<List<CountriesItem>>>(Resource.Empty())

    var timeFilterList = MutableStateFlow<MutableList<String>>(mutableListOf())
    var continentFilterList = MutableStateFlow<MutableList<String>>(mutableListOf())

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
                Log.d("filter Result", "raw List${rawList.value.data}")
                when (resource) {
                    is Resource.Successful -> {
                        resource.data?.let {
                            filteredResult(it).collect{filter->
                                if (filter.isNotEmpty()) {
                                    _filteredList.value =
                                        Resource.Successful(dataMapper(filter))
                                } else {
                                    _filteredList.value = Resource.Empty()
                                }
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
        data: List<CountriesItem>
    ) =
        callbackFlow<List<CountriesItem>> {
            continentFilterList.collect { continents ->
                timeFilterList.collect { timeZone ->
                    val list = mutableListOf<CountriesItem>()
                    val continent = data.filter { items ->
                        continents.any { continent ->
                            items.continents?.contains(continent) ?: false
                        }
                    }.toMutableList()

                    val time = data.filter { items ->
                        timeZone.any { time ->
                            items.timezones?.contains(time) ?: false
                        }
                    }
                    list.addAll(continent)
                    list.addAll(time)
                    trySend(list)

                    Log.d("filter viewModel", "$list")
                }
            }}



    fun updateTimeFilterList(value: String, add: Boolean) {
        timeFilterList.update {
            if (add) {
                it.add(value)
            } else {
                it.remove(value)
            }

            return
        }
    }

    fun updateContFilterList(value: String, add: Boolean) {
        continentFilterList.update {
            if (add) {
                it.add(value)
            } else {
                it.remove(value)
            }
            return
        }
    }

    fun checkIfExistTimeFilter(value: String) =
        callbackFlow {
            timeFilterList.collect {
                trySend(it.contains(value))
            }
        }

    fun checkIfExistContFilter(value: String) =
        callbackFlow {
            continentFilterList.collect {
                trySend(it.contains(value))
            }
        }

    fun clearTimeFilter(){
        timeFilterList.update {
            it.clear()
            return
        }
    }
    fun clearContFilter(){
        continentFilterList.update {
            it.clear()
            return
        }
    }


}

