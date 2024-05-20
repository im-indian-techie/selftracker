package com.ashin.selftracker.model.repository

import com.ashin.selftracker.model.pojo.User
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.isValid
import io.realm.kotlin.ext.query
import javax.inject.Inject

class LoginSignUpRepositoryImplementation @Inject constructor(private val realm: Realm) {
    suspend fun SignUp(user: User):String
    {
        var status=""
        realm.writeBlocking {
            try {
               val data=copyToRealm(user)
                if (data.isValid())
                {
                    status="Successfully registered"
                }
                else
                {
                    status="Registration failed"
                }
            }
            catch (e: Exception)
            {
               status= e.message.toString()
            }

        }
        return status
    }
    suspend fun Login(email:String): User?
    {
        return realm.query<User>(query = "email == $0",email).first().find()
    }
}