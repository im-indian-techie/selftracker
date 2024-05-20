package com.ashin.selftracker.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.ashin.selftracker.databinding.ActivityLoginSignUpBinding
import com.ashin.selftracker.model.pojo.User
import com.ashin.selftracker.utils.ApiState
import com.ashin.selftracker.utils.MyLocationService
import com.ashin.selftracker.utils.PreferenceManger
import com.ashin.selftracker.utils.ProgressDialog
import com.ashin.selftracker.utils.Validator
import com.ashin.selftracker.viewmodel.LoginSignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginSignUpActivity : AppCompatActivity() {
    private val activity=this
    private lateinit var binding: ActivityLoginSignUpBinding
    private var isLoginPage=true
    val viewModel:LoginSignUpViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    @Inject
    lateinit var preferenceManger: PreferenceManger
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {

        progressDialog=ProgressDialog(activity)
        lifecycleScope.launch {
          viewModel._loginFlow.collect{
              when(it)
              {
                  is ApiState.Empty -> {

                  }
                  is ApiState.Failure -> {
                      progressDialog.dismiss()
                      Toast.makeText(activity,it.msg,Toast.LENGTH_SHORT).show()

                  }
                  is ApiState.Loading -> {
                      progressDialog.show()
                  }
                  is ApiState.Success -> {
                      progressDialog.dismiss()
                      val user=it.data as User
                      preferenceManger.setEmail(user.email)
                      startActivity(Intent(activity,LocationListActivity::class.java))
                  }
              }
          }

      }
        lifecycleScope.launch {
            viewModel._signUpFlow.collect{
                when(it)
                {
                    is ApiState.Empty -> {

                    }
                    is ApiState.Failure -> {
                        progressDialog.dismiss()
                        Toast.makeText(activity,it.msg,Toast.LENGTH_SHORT).show()
                    }
                    is ApiState.Loading -> {
                        progressDialog.show()
                        Log.d("loading","Loading")
                    }
                    is ApiState.Success -> {
                        progressDialog.dismiss()
                        Toast.makeText(activity,it.data.toString(),Toast.LENGTH_SHORT).show()
                        showLogin()
                    }
                }
            }
        }

      binding.tvSignUp.apply {
          setOnClickListener {
             showSignUp()
          }
      }
        binding.btnSubmit.apply {
            setOnClickListener{
                if(isLoginPage) {
                    if (binding.etEmail.text.toString().trim().isEmpty()) {
                        Toast.makeText(activity, "Please enter your email", Toast.LENGTH_SHORT)
                            .show()
                    } else if (binding.etPassword.text.toString().trim().isEmpty()) {
                        Toast.makeText(activity, "Please enter your passord", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else
                    {
                       viewModel.login(binding.etEmail.text.toString().trim())
                    }
                }
                else
                {
                    if(binding.etEmail.text.toString().trim().isEmpty())
                    {
                        Toast.makeText(activity,"Please enter your email",Toast.LENGTH_SHORT).show()
                    }
                    else if (!Validator().isValidEmail(binding.etEmail.text.toString().trim()))
                    {
                        Toast.makeText(activity,"Please enter a valid email",Toast.LENGTH_SHORT).show()
                    }
                    else if(binding.etPassword.text.toString().trim().isEmpty())
                    {
                        Toast.makeText(activity,"Please enter your password",Toast.LENGTH_SHORT).show()
                    }
                    else if(binding.etCnfPassword.text.toString().trim().isEmpty())
                    {
                        Toast.makeText(activity,"Please re-enter your password",Toast.LENGTH_SHORT).show()
                    }
                    else if(binding.etPassword.text.toString().trim()!= binding.etCnfPassword.text.toString().trim())
                    {
                        Toast.makeText(activity,"Password mismatch",Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        val email=binding.etEmail.text.toString().trim()
                        val pass= binding.etPassword.text.toString().trim()
                        val user=User()
                        user.email=email
                        user.password=pass
                        viewModel.signUp(user)
                    }

                }
            }
        }
    }

    override fun onBackPressed() {
        if(!isLoginPage)
        {
            showLogin()
        }
        else {
            super.onBackPressed()
        }
    }
    private fun showSignUp()
    {
        binding.etCnfPassLayout.isVisible=true
        binding.tvTitle.text="SignUp"
        binding.llSignUp.isVisible=false
        isLoginPage=false
        //
        binding.etEmail.setText("")
        binding.etPassword.setText("")
    }
    private fun showLogin()
    {
        binding.etCnfPassLayout.isVisible=false
        binding.tvTitle.text="Login"
        binding.llSignUp.isVisible=true
        isLoginPage=true
    }



}