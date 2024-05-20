package com.ashin.selftracker.utils

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.ashin.selftracker.model.pojo.LocationInfo
import com.ashin.selftracker.model.pojo.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MyLocationService:Service() {
    @Inject
    lateinit var realm: Realm
    @Inject
    lateinit var preferenceManger: PreferenceManger
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var email:String
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
         email= preferenceManger.getEmail()!!
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d("location2","location2")
                locationResult ?: return
                for (location in locationResult.locations) {
                    GlobalScope.launch {
                        saveLocation(location)
                    }
                }
            }
        }
    }
    private suspend fun saveLocation(location: Location) {
        CoroutineScope(Dispatchers.IO).launch {
            realm.writeBlocking {
                val locationInfo = LocationInfo().apply {
                    latitude = location.latitude
                    longitude = location.longitude
                    timestamp = System.currentTimeMillis()
                }
                println("User location: ${locationInfo.latitude}")
                val user= realm.query<User>(query = "email == $0",email).first().find()
                findLatest(user!!)?.locations!!.add(locationInfo)

            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("location3","location3")
        val locationRequest = LocationRequest.create().apply {
            interval = 900000 // 15 minutes
            fastestInterval = 900000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("location1","location1")
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


}