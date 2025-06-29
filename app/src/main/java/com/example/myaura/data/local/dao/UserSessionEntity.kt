package com.example.myaura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myaura.data.local.entity.UserSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: UserSessionEntity)

    @Query("SELECT * FROM user_session WHERE id = 1")
    fun getSession(): Flow<UserSessionEntity?>

    @Query("DELETE FROM user_session")
    suspend fun clearSession()
}