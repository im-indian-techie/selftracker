package com.ashin.selftracker.model.repository

import com.ashin.selftracker.model.pojo.LocationInfo
import com.ashin.selftracker.model.pojo.User
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject


class LocationListRepositoryImplementation @Inject constructor(val realm: Realm) {
    suspend fun getLocations(email:String):List<LocationInfo>
    {
        val user= realm.query<User>(query = "email == $0",email).first().find()
        return user!!.locations
    }
}