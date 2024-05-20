package com.ashin.selftracker.model.repository

import android.provider.ContactsContract.CommonDataKinds.Email
import com.ashin.selftracker.model.pojo.LocationInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocationListRepository @Inject constructor(val locationListRepositoryImplementation: LocationListRepositoryImplementation){
    suspend fun getLocation(email: String):Flow<List<LocationInfo>> = flow {
        emit(locationListRepositoryImplementation.getLocations(email))
    }
}