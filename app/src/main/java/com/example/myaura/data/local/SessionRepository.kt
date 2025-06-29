package com.example.myaura.data.local

import com.example.myaura.data.local.dao.UserSessionDao
import com.example.myaura.data.local.entity.UserSessionEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepository @Inject constructor(
    private val userSessionDao: UserSessionDao
) {

    val isLoggedIn: Flow<Boolean> = userSessionDao.getSession()
        .map { it?.isLoggedIn ?: false }

    suspend fun saveLoginSession(isLoggedIn: Boolean) {
        val session = userSessionDao.getSession().first() ?: UserSessionEntity(isLoggedIn = false, isDarkMode = false)
        userSessionDao.insertSession(session.copy(isLoggedIn = isLoggedIn))
    }

    val isDarkMode: Flow<Boolean> = userSessionDao.getSession()
        .map { it?.isDarkMode ?: false }

    suspend fun saveDarkModePreference(isDarkMode: Boolean) {
        val session = userSessionDao.getSession().first() ?: UserSessionEntity(isLoggedIn = false, isDarkMode = false)
        userSessionDao.insertSession(session.copy(isDarkMode = isDarkMode))
    }

    suspend fun clearLoginSession(){
        val currentSession = userSessionDao.getSession().first()
        if (currentSession != null) {
            userSessionDao.insertSession(currentSession.copy(isLoggedIn = false))
        }
    }
}