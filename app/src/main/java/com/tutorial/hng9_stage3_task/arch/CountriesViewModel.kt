package com.tutorial.hng9_stage3_task.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorial.hng9_stage3_task.ApiService
import com.tutorial.hng9_stage3_task.models.Resource
import com.tutorial.hng9_stage3_task.models.main.Countries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CountriesViewModel : ViewModel() {

    private var _allCountriesFlow = MutableStateFlow<Resource<Countries>>(Resource.Loading())
    val allCountriesFlow get() = _allCountriesFlow.asStateFlow()


    suspend fun getAllCountries() {
        viewModelScope.launch {
            val result = ApiService.retrofitApiService.getAllCountries()
            when {
                result.isSuccessful -> {
                    result.body()?.let { countries->
                        if (countries.isNotEmpty()){
                            _allCountriesFlow.value = Resource.Successful(countries)
                        }else{
                            _allCountriesFlow.value = Resource.Empty()
                        }
                    }
                }
                else -> {
                    _allCountriesFlow.value = Resource.Failure(result.errorBody().toString())
                }
            }
        }
    }
}

