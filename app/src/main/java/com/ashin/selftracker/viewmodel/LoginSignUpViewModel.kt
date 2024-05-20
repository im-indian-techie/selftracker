package com.ashin.selftracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashin.selftracker.model.pojo.User
import com.ashin.selftracker.model.repository.LoginSignUpRepository
import com.ashin.selftracker.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginSignUpViewModel @Inject constructor(val loginSignUpRepository: LoginSignUpRepository):ViewModel() {
   private val signUpFlow:MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val _signUpFlow:StateFlow<ApiState> = signUpFlow

    private val loginFlow:MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val _loginFlow:StateFlow<ApiState> = loginFlow

   fun signUp(user: User)=viewModelScope.launch(Dispatchers.IO) {
       signUpFlow.value=ApiState.Loading
       loginSignUpRepository.signUp(user).catch {
         ae->signUpFlow.value=ApiState.Failure(ae.message.toString())
       }.collect{
           data-> signUpFlow.value=ApiState.Success(data)
       }
   }
    fun login(email:String)=viewModelScope.launch {
        signUpFlow.value=ApiState.Loading
        loginSignUpRepository.login(email).catch {
                ae->loginFlow.value=ApiState.Failure(ae.message.toString())
        }.collect{
                when(it)
                {
                    null->
                    {
                        loginFlow.value=ApiState.Failure("No user found")
                    }
                    else->
                    {
                        loginFlow.value=ApiState.Success(it)
                    }
                }

        }
    }

}