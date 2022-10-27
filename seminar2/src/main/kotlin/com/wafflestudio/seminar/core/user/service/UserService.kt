package com.wafflestudio.seminar.core.user.service

import com.querydsl.core.types.Path
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.user.database.*
//import com.wafflestudio.seminar.core.user.database.QNewParticipantProfileEntity.newParticipantProfileEntity
import com.wafflestudio.seminar.core.user.domain.UserProfile
import com.wafflestudio.seminar.core.user.dto.*
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val authTokenService: AuthTokenService,
    private val queryFactory: JPAQueryFactory,
    private val modelMapper: ModelMapper
) {
    fun getProfile(email : String, token: String): GetProfileDto {
        val findByEmailEntity = userRepository.findByEmail(email)

        val qUserEntity: QUserEntity = QUserEntity.userEntity
        val qParticipantProfileEntity: QParticipantProfileEntity? = QParticipantProfileEntity.participantProfileEntity
        val qInstructorProfileEntity: QInstructorProfileEntity? = QInstructorProfileEntity.instructorProfileEntity

        val userProfileDto =makeUserProfileDto(email, qUserEntity, qParticipantProfileEntity, qInstructorProfileEntity)

        val userEntity = userProfileDto[0].userEntity
        val participantProfileEntity = userProfileDto[0].participantProfileEntity
        val instructorProfileEntity = userProfileDto[0].instructorProfileEntity
        
        return if(findByEmailEntity.participant != null && findByEmailEntity.instructor == null) {
            GetProfileDto(
                userEntity?.id, 
                userEntity?.username, 
                userEntity?.email, 
                userEntity?.lastLogin,
                userEntity?.dateJoined,
                GetProfileParticipantDto(participantProfileEntity?.id, participantProfileEntity?.university, participantProfileEntity?.isRegistered),
               null
            )
            
        } else if(findByEmailEntity.participant == null && findByEmailEntity.instructor != null){
            GetProfileDto(
                userEntity?.id, 
                userEntity?.username, 
                userEntity?.email, 
                userEntity?.lastLogin, 
                userEntity?.dateJoined,
               null,
                GetProfileInstructorDto(instructorProfileEntity?.id, instructorProfileEntity?.company, instructorProfileEntity?.year)
            )
        } else if(findByEmailEntity.participant != null && findByEmailEntity.instructor != null){
            GetProfileDto(
                userEntity?.id, 
                userEntity?.username, 
                userEntity?.email, 
                userEntity?.lastLogin, 
                userEntity?.dateJoined,
                GetProfileParticipantDto(participantProfileEntity?.id,participantProfileEntity?.university, participantProfileEntity?.isRegistered),
                GetProfileInstructorDto(instructorProfileEntity?.id, instructorProfileEntity?.company, instructorProfileEntity?.year)
            )
            
        } else{
            throw Seminar400("오류")
        }
        
    }
    
    fun updateMe(user: UserProfile, token: String): UserProfile{
        //todo: email 못찾았으면 예외 제공
        //todo: year 음수이면 예외 제공
        
        val userEntity = userRepository.findByEmail(authTokenService.getCurrentEmail(token))
        println(userEntity)
        
       
        if(userEntity.participant != null){
            val participantProfileEntity = participantProfileRepository.findByEmailParticipant(authTokenService.getCurrentEmail(token))
            println(participantProfileEntity)
            userEntity.let {
                it.username = user.username
                it.password = user.password
                it.participant?.university = user.participant?.university.toString()

            }
            participantProfileEntity.let {
                it.university = user.participant?.university ?: ""
            }
            userRepository.save(userEntity)
            participantProfileRepository.save(participantProfileEntity)
        }
       
        if(userEntity.instructor != null){
            val instructorProfileEntity = instructorProfileRepository.findByEmailInstructor(authTokenService.getCurrentEmail(token))
            println(instructorProfileEntity)
            userEntity.let {
                it.username = user.username
                it.password = user.password
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
            password = user.password,
          
            participant = UpdateParticipantProfileDto(user.participant?.university),
            instructor = UpdateInstructorProfileDto(user.instructor?.company, user.instructor?.year)
            
            
        )
    }

    
    private fun makeUserProfileDto(email: String, qUserEntity: QUserEntity, qParticipantProfileEntity: QParticipantProfileEntity?, qInstructorProfileEntity: QInstructorProfileEntity?):List<UserProfileDto>{
        return queryFactory.select(Projections.constructor(
            UserProfileDto::class.java,
            qUserEntity,
            qParticipantProfileEntity,
            qInstructorProfileEntity
        ))
            .from(qUserEntity)
            .leftJoin(qParticipantProfileEntity).on(qUserEntity.participant.id.eq(qParticipantProfileEntity?.id))
            .leftJoin(qInstructorProfileEntity).on(qUserEntity.instructor.id.eq(qInstructorProfileEntity?.id))
            .where(qUserEntity.email.eq(email))
            .fetch()
    }
    
//    private fun makeUserProfileDto(email: String):List<UserProfileDto> {
//        val userEntity: QUserEntity = QUserEntity.userEntity
//        val newParticipantProfileEntity1: QNewParticipantProfileEntity1 = QNewParticipantProfileEntity1.newParticipantProfileEntity1
//      //  val instructorProfileEntity: QInstructorProfileEntity = QInstructorProfileEntity.instructorProfileEntity
//
//        return queryFactory.select(Projections.constructor(
//            UserProfileDto::class.java, userEntity.id, userEntity.username, 
//            userEntity.email, userEntity.dateJoined,newParticipantProfileEntity1
//        ))
//            .from(userEntity, newParticipantProfileEntity1)
//           .where(userEntity.participant.id.eq(newParticipantProfileEntity1.id))
//            //.leftJoin(userEntity.instructor, instructorProfileEntity).on(userEntity.email.eq(instructorProfileEntity.emailInstructor))
//          .where(userEntity.email.eq(email))
//            .fetch()
//    }
//    
//    fun makeParticipantProfileDto(seminarId: Long, email: String):List<ParticipantProfileDto> {
//            val participantProfileEntity: QParticipantProfileEntity = QParticipantProfileEntity.participantProfileEntity
//        val seminarEntity: QSeminarEntity = QSeminarEntity.seminarEntity
//        val userSeminarEntity: QUserSeminarEntity = QUserSeminarEntity.userSeminarEntity
//        return queryFactory.select(Projections.constructor(ParticipantProfileDto::class.java, participantProfileEntity.id,
//            participantProfileEntity.university, participantProfileEntity.isRegistered, seminarEntity))
//            .from(participantProfileEntity, userSeminarEntity)
//            .where(participantProfileEntity.id.eq(userSeminarEntity.user.id))
//            .where(userSeminarEntity.user.id.eq(seminarEntity.id))
//            .where(participantProfileEntity.emailParticipant.eq(email)).fetch()
//            
//    }
    
//    fun makeSeminarDto(seminarId: Long, email: String): QNewSeminarEntity{
//        val seminarEntity: QSeminarEntity = QSeminarEntity.seminarEntity
//        val participantSeminarEntity: QParticipantSeminarEntity = QParticipantSeminarEntity.participantSeminarEntity
//        val seminarDto =  queryFactory.select(Projections.constructor(SeminarDto::class.java, seminarEntity.id,seminarEntity.name
//        ,participantSeminarEntity.isActive))
//            .from(seminarEntity, participantSeminarEntity)
//            .where(seminarEntity.id.eq(participantSeminarEntity.seminar.id))
//            .where(seminarEntity.id.eq(seminarId))
//            .where().fetchOne()
//        return modelMapper.map(seminarDto, QNewSeminarEntity::class.java)    
//    }
}