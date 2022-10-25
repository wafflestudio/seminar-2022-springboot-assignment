package com.wafflestudio.seminar.core.user.service

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.UserProfile
import com.wafflestudio.seminar.core.user.dto.UserProfileDto
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val authTokenService: AuthTokenService,
    private val queryFactory: JPAQueryFactory
) {
    fun getProfile(email : String, token: String): List<UserProfileDto> {
   
        
        val userEntity: QUserEntity = QUserEntity.userEntity
     val participantProfileEntity1 = QParticipantProfileEntity("participantProfileEntity1")
       val instructorProfileEntity1 = QInstructorProfileEntity("instructorProfileEntity1")
        val participantProfileEntity: QParticipantProfileEntity = QParticipantProfileEntity.participantProfileEntity
        val instructorProfileEntity: QInstructorProfileEntity = QInstructorProfileEntity.instructorProfileEntity
        return queryFactory.select(Projections.constructor(UserProfileDto::class.java, userEntity.id, userEntity.username,
        userEntity.email, userEntity.dateJoined, participantProfileEntity,instructorProfileEntity))
            .from(userEntity)
            .leftJoin(userEntity.participant, participantProfileEntity).on(userEntity.email.eq(participantProfileEntity.emailParticipant))
            .leftJoin(userEntity.instructor, instructorProfileEntity).on(userEntity.email.eq(instructorProfileEntity.emailInstructor))
            .where(userEntity.email.eq(email))
            .fetch()
        
    }
    
    fun updateMe(user: UserProfile, token: String): UserProfile{
        //todo: email 못찾았으면 예외 제공
        //todo: year 음수이면 예외 제공
        val userEntity = userRepository.findByEmail(user.email)
        println(userEntity)
        
       
        if(userEntity.participant != null){
            val participantProfileEntity = participantProfileRepository.findByEmailParticipant(user.email)
            println(participantProfileEntity)
            userEntity.let {
                it.username = user.username
                it.participant?.university = user.participant?.university.toString()

            }
            participantProfileEntity.let {
                it.university = user.participant?.university ?: ""
            }
            userRepository.save(userEntity)
            participantProfileRepository.save(participantProfileEntity)
        }
       
        if(userEntity.instructor != null){
            val instructorProfileEntity = instructorProfileRepository.findByEmailInstructor(user.email)
            println(instructorProfileEntity)
            userEntity.let {
                it.username = user.username

                it.instructor?.company = user.instructor?.company.toString()
                it.instructor?.year = user.instructor?.year
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
            participant = user.participant,
            instructor = user.instructor
            
            
        )
    }
}