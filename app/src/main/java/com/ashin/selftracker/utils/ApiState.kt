package com.ashin.selftracker.utils

sealed class ApiState {
    object Loading:ApiState()
    object Empty:ApiState()
    class Success(val data:Any):ApiState()
    class Failure(val msg:String):ApiState()
}