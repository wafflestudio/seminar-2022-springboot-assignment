package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.database.InstructorProfileRepository
import com.wafflestudio.seminar.core.user.database.ParticipantProfileRepository
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.UserProfile
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val authTokenService: AuthTokenService
) {
    fun getProfile(email : String, token: String): UserProfile{
        val userEntity = userRepository.findByEmail(email)
        return UserProfile(userEntity, token)
        
    }
    
    fun updateMe(user: UserProfile, token: String): UserProfile{
        //todo: email 못찾았으면 예외 제공
        //todo: year 음수이면 예외 제공
        val userEntity = userRepository.findByEmail(user.email)
        println(userEntity)
        
       
        if(userEntity.participantProfileEntity != null){
            val participantProfileEntity = participantProfileRepository.findByEmailParticipant(user.email)
            println(participantProfileEntity)
            userEntity.let {
                it.username = user.username
                it.participantProfileEntity?.university = user.participant?.university.toString()

            }
            participantProfileEntity.let {
                it.university = user.participant?.university ?: ""
            }
            userRepository.save(userEntity)
            participantProfileRepository.save(participantProfileEntity)
        }
       
        if(userEntity.instructorProfileEntity != null){
            val instructorProfileEntity = instructorProfileRepository.findByEmailInstructor(user.email)
            println(instructorProfileEntity)
            userEntity.let {
                it.username = user.username

                it.instructorProfileEntity?.company = user.instructor?.company.toString()
                it.instructorProfileEntity?.year = user.instructor?.year
            }
            instructorProfileEntity.let { 
                it.company = user.instructor?.company ?: ""
                it.year = user.instructor?.year 
            }
            userRepository.save(userEntity)
            instructorProfileRepository.save(instructorProfileEntity)
        }
       
        
        
        return UserProfile(userEntity,token)
       
    }
    private fun UserProfile(user: UserEntity, token: String) = user.run { 

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