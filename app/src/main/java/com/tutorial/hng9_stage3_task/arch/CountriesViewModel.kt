package com.tutorial.hng9_stage3_task.arch

import com.tutorial.hng9_stage3_task.models.MapperNew
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorial.hng9_stage3_task.utils.ApiService
import com.tutorial.hng9_stage3_task.models.Resource
import com.tutorial.hng9_stage3_task.models.main.Countries
import com.tutorial.hng9_stage3_task.models.main.CountriesItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CountriesViewModel : ViewModel() {


    private var _allCountriesFlow = MutableStateFlow<Resource<List<MapperNew>>>(Resource.Loading())
    val allCountriesFlow get() = _allCountriesFlow.asStateFlow()
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
//                                    Resource.Successful(countries)

                            } else {
                                _allCountriesFlow.value = Resource.Empty()
                            }
                        }
                    }
                    else -> {
                        _allCountriesFlow.value = Resource.Failure(result.errorBody().toString())
                    }
                }
            } catch (e: Exception) {
                _allCountriesFlow.value = Resource.Failure(e.toString())
                Log.d("Inside try catch 2", "$e")
            }


        }
    }

    private fun dataMapper(data:Countries):List<MapperNew>{

        val list = CharRange('A','Z').toMutableList()
        val mainList = mutableListOf<MapperNew>()
        list.forEach { char->
            val newList = mutableListOf<CountriesItem>()
            data.forEach { countriesItem ->
                val case = countriesItem.name?.common?.startsWith(prefix = char.toString(),ignoreCase = true)
                if (case == true){
                    newList.add(countriesItem)
                }
            }
            mainList.add(MapperNew(char.toString(),newList))
        }
        mainList.forEach {
            Log.d("MappingItems","$it")
        }
        return mainList
    }



}

