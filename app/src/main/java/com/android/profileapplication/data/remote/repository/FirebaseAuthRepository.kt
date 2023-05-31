package com.android.profileapplication.data.remote.repository

import com.android.profileapplication.utility.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor() {

    private var auth: FirebaseAuth = Firebase.auth

    fun registerUser(email: String, password: String): Flow<Resource<FirebaseUser>> = flow {
        emit(Resource.Loading())
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            emit((result.user?.let {
                Resource.Success(data = it)
            }!!))
        } catch (ex: IOException) {
            emit(Resource.Error("Please check Internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }

    fun signInUser(email: String, password: String) = flow<Resource<FirebaseUser>> {
        emit(Resource.Loading())
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit((result.user?.let {
                Resource.Success(data = it)
            }!!))
        } catch (ex: IOException) {
            emit(Resource.Error("Please check Internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }

    fun resetPassword(email: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val result = auth.sendPasswordResetEmail(email).await()
            result.let {
                emit(Resource.Success("Please check email for further details"))
            }
        } catch (ex: IOException) {
            emit(Resource.Error("Please check Internet connection"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown Error"))
        }
    }


}