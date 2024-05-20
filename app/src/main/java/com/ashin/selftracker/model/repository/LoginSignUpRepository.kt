package com.ashin.selftracker.model.repository

import com.ashin.selftracker.model.pojo.User
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginSignUpRepository @Inject constructor(private val repositoryImplementation: LoginSignUpRepositoryImplementation) {
     suspend fun signUp(user: User):Flow<String> = flow {
         emit(repositoryImplementation.SignUp(user))
     }.flowOn(Dispatchers.IO)

     suspend fun login(email:String):Flow<User?> = flow {
         emit(repositoryImplementation.Login(email))
     }.flowOn(Dispatchers.IO)
}