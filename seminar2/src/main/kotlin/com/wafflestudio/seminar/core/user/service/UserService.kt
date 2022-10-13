package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.database.JpaRepository
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.UserProfile
import org.springframework.stereotype.Service

@Service
class UserService(
    private val jpaRepository: JpaRepository,
    private val authTokenService: AuthTokenService
) {
    fun getProfile(email : String, token: String): UserProfile{
        val userEntity = jpaRepository.findByEmail(email)
        return userProfile(userEntity, token)
        
    }
    
    private fun userProfile(user: UserEntity, token: String) = user.run { 
        UserProfile(
            id = authTokenService.getCurrentUserId(token),
            username = user.username,
            email = user.email,
            lastLogin = authTokenService.getCurrentLastLogin(token),
            dateJoined = user.dateJoined,
            participant = user.participantProfileEntity,
            instructor = user.instructorProfileEntity
            
            
        )
    }
}