package com.ashin.selftracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashin.selftracker.model.repository.LocationListRepository
import com.ashin.selftracker.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationListViewModel @Inject constructor(val locationListRepository: LocationListRepository):ViewModel() {
    val locationListFlow:MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val _locationListFlow:StateFlow<ApiState> = locationListFlow

    fun getLocationList(email:String)=viewModelScope.launch {
        locationListFlow.value=ApiState.Loading
        locationListRepository.getLocation(email).catch {
               ae-> locationListFlow.value=ApiState.Failure(ae.message.toString())
        }.collect{
            when(it)
            {
                null->
                {
                    locationListFlow.value=ApiState.Failure("No user found")
                }
                else->
                {
                    locationListFlow.value=ApiState.Success(it)
                }
            }
        }
    }
}