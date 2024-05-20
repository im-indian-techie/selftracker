package com.ashin.selftracker.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashin.selftracker.R
import com.ashin.selftracker.databinding.ActivityLocationListBinding
import com.ashin.selftracker.model.pojo.LocationInfo
import com.ashin.selftracker.utils.ApiState
import com.ashin.selftracker.utils.MyLocationService
import com.ashin.selftracker.utils.PreferenceManger
import com.ashin.selftracker.utils.ProgressDialog
import com.ashin.selftracker.view.adapter.LocationListAdapter
import com.ashin.selftracker.viewmodel.LocationListViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationListActivity : AppCompatActivity() {
    private val activity=this
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private lateinit var binding: ActivityLocationListBinding
    private lateinit var locationListAdapter: LocationListAdapter
    private val viewModel:LocationListViewModel by viewModels()
    private lateinit var progressDialog:ProgressDialog
    private val list:RealmList<LocationInfo> = realmListOf()
    @Inject
    lateinit var preferenceManger: PreferenceManger
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        checkLocationPermissions()
        progressDialog=ProgressDialog(activity)
        val email=preferenceManger.getEmail()
        viewModel.getLocationList(email!!)
        locationListAdapter= LocationListAdapter(list)
        binding.flbLogout.setOnClickListener {
            preferenceManger.clearPrefs()
            startActivity(Intent(activity,LoginSignUpActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY))
            finish()
        }
        binding.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(activity)
            adapter=locationListAdapter

        }
        lifecycleScope.launch {
            viewModel._locationListFlow.collect{
                when(it)
                {
                    is ApiState.Empty -> {

                    }
                    is ApiState.Failure -> {
                        binding.recyclerview.isVisible=false
                        binding.tvNodata.isVisible=true
                        progressDialog.dismiss()
                        Toast.makeText(activity,it.msg,Toast.LENGTH_SHORT).show()
                    }
                    is ApiState.Loading -> {
                        progressDialog.show()
                        Log.d("loading","Loading")
                    }
                    is ApiState.Success -> {
                        binding.recyclerview.isVisible=true
                        binding.tvNodata.isVisible=false
                        progressDialog.dismiss()
                        list.clear()
                        list.addAll(it.data as Collection<LocationInfo>)
                        locationListAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }
    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        } else {
            startLocationService()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startLocationService()
            } else {
                Toast.makeText(this, "Location permissions are required to use this app", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun startLocationService() {
        Log.d("location2","location2")
        val serviceIntent = Intent(this, MyLocationService::class.java)
        startService(serviceIntent)
    }

    override fun onResume() {
        super.onResume()
    }

}