package com.example.myaura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myaura.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfileEntity)

    @Update
    suspend fun updateUserProfile(userProfile: UserProfileEntity)

    @Query("SELECT * FROM user_profiles WHERE uid = :uid")
    fun getUserProfile(uid: String): Flow<UserProfileEntity?>

    @Query("DELETE FROM user_profiles WHERE uid = :uid")
    suspend fun deleteUserProfile(uid: String)
}