package com.example.myaura.data.remote

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSource(private val firebaseAuth: FirebaseAuth){
    suspend fun signUp(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun signIn(email: String, password:String){
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }
}