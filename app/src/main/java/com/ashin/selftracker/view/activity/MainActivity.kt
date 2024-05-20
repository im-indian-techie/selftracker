package com.ashin.selftracker.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ashin.selftracker.databinding.ActivityMainBinding
import com.ashin.selftracker.utils.PreferenceManger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val activity=this
    @Inject
    lateinit var preferenceManger: PreferenceManger
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        Handler().postDelayed(Runnable {
           if(!preferenceManger.getEmail().equals(""))
           {
               startActivity(Intent(activity,LocationListActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
               finish()
           }
            else
           {
               startActivity(Intent(activity,LoginSignUpActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
               finish()
           }
        },1000)
    }
}