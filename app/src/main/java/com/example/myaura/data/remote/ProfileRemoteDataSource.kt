package com.example.myaura.data.remote

import com.example.myaura.domain.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRemoteDataSource (private val firestore: FirebaseFirestore) {
    suspend fun saveUserProfile(uid: String, profile: UserProfile) {
        firestore.collection("users").document(uid).set(profile).await()
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        return firestore.collection("users").document(uid).get().await()
            .toObject(UserProfile::class.java)
    }

}