package com.ashin.selftracker.utils

class Validator {
    fun isValidEmail(email:String):Boolean
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}