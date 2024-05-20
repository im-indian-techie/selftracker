package com.ashin.selftracker.view.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ashin.selftracker.R
import com.ashin.selftracker.databinding.ActivityRouteMapBinding
import com.ashin.selftracker.model.pojo.LocationInfo
import com.ashin.selftracker.model.pojo.User
import com.ashin.selftracker.utils.PreferenceManger
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmList
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class RouteMapActivity : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var binding: ActivityRouteMapBinding
    @Inject
    lateinit var realm: Realm
    private lateinit var map: GoogleMap
    @Inject
    lateinit var preferenceManger: PreferenceManger
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRouteMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        binding.playbackButton.setOnClickListener {
            startPlayback()
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Get location from intent and show it
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val position = LatLng(latitude, longitude)
        map.addMarker(MarkerOptions().position(position).title("Selected Location"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
    }
    private fun startPlayback() {
        val email=preferenceManger.getEmail()
//        val locations = realm.where(LocationInfo::class.java).findAll().sort("timestamp")
        val user= realm.query<User>(query = "email == $0",email).first().find()
        animateLocations(user!!.locations)
    }

    private fun animateLocations(locations: RealmList<LocationInfo>) {
        val handler = Handler(Looper.getMainLooper())
        val iterator = locations.iterator()
        val delay: Long = 1000 // 1 second interval

        fun next() {
            if (iterator.hasNext()) {
                val location = iterator.next()
                showLocationOnMap(location)
                handler.postDelayed({ next() }, delay)
            }
        }

        next()
    }

    private fun showLocationOnMap(location: LocationInfo) {
        val position = LatLng(location.latitude, location.longitude)
        map.addMarker(MarkerOptions().position(position).title("Location at ${Date(location.timestamp)}"))
        map.moveCamera(CameraUpdateFactory.newLatLng(position))
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}