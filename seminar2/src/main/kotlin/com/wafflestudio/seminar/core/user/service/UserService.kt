package com.wafflestudio.seminar.core.user.service

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.Seminar409
//import com.wafflestudio.seminar.core.seminar.database.QSeminarEntity
//import com.wafflestudio.seminar.core.seminar.database.QUserSeminarEntity
import com.wafflestudio.seminar.core.user.api.request.BeParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.UpdateProfileRequest
import com.wafflestudio.seminar.core.user.api.response.GetProfile
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.*
import com.wafflestudio.seminar.core.user.dto.seminar.SeminarInfoDto
import com.wafflestudio.seminar.core.user.dto.seminar.StudentDto
import com.wafflestudio.seminar.core.user.dto.seminar.UserProfileDto
import com.wafflestudio.seminar.core.user.dto.user.*
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val authTokenService: AuthTokenService,
    private val queryFactory: JPAQueryFactory,
) {
    fun getProfile(id : Long, token: String): GetProfile {

        val findByEmailEntity = userRepository.findByEmail(authTokenService.getCurrentEmail(token))
        
        
        if(authTokenService.getCurrentUserId(token) != id){
            throw Seminar401("정보에 접근할 수 없습니다")
        }

        val qUserEntity: QUserEntity = QUserEntity.userEntity
        val qParticipantProfileEntity: QParticipantProfileEntity? = QParticipantProfileEntity.participantProfileEntity
        val qInstructorProfileEntity: QInstructorProfileEntity? = QInstructorProfileEntity.instructorProfileEntity
        val qSeminarEntity: QSeminarEntity = QSeminarEntity.seminarEntity
        val qUserSeminarEntity: QUserSeminarEntity = QUserSeminarEntity.userSeminarEntity
        
        val userProfileDto =makeUserProfileDto(id, qUserEntity, qParticipantProfileEntity, qInstructorProfileEntity)

        val userEntity = userProfileDto[0].userEntity
        val participantProfileEntity = userProfileDto[0].participantProfileEntity
        val instructorProfileEntity = userProfileDto[0].instructorProfileEntity

        val seminarsList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
            .where(qUserEntity.id.eq(id))
            .where(qUserSeminarEntity.role.eq("participant")).fetch()

        val instructingSeminarsList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
            .where(qUserEntity.id.eq(id))
            .where(qUserSeminarEntity.role.eq("instructor")).fetch()

        val newListParticipant = mutableListOf<SeminarsDto>()
        val newListInstructor = mutableListOf<InstructingSeminarsDto>()
        
        for(i in 0 until seminarsList.size){
            val seminarEntity = seminarsList[i].seminarEntity
            val studentSeminarEntity = seminarsList[i].userSeminarEntity
            newListParticipant.add(
                SeminarsDto(
                    seminarEntity?.id,
                    seminarEntity?.name,
                    studentSeminarEntity?.joinedAt,
                    studentSeminarEntity?.isActive,
                    studentSeminarEntity?.droppedAt

                )
            )
        }

        for(i in 0 until instructingSeminarsList.size){
            val seminarEntity = instructingSeminarsList[i].seminarEntity
            val teacherSeminarEntity = instructingSeminarsList[i].userSeminarEntity
            newListInstructor.add(
                InstructingSeminarsDto(
                    seminarEntity?.id,
                    seminarEntity?.name,
                    teacherSeminarEntity?.joinedAt,

                )
            )
        }
        
        
        return if(findByEmailEntity?.participant != null && findByEmailEntity?.instructor == null) {
            GetProfile(
                userEntity?.id, 
                userEntity?.username, 
                userEntity?.email, 
                userEntity?.lastLogin,
                userEntity?.dateJoined,
                GetProfileParticipantDto(
                    participantProfileEntity?.id, participantProfileEntity?.university, participantProfileEntity?.isRegistered, newListParticipant
                ),
               null
            )
            
        } else if(findByEmailEntity?.participant == null && findByEmailEntity?.instructor != null){
            GetProfile(
                userEntity?.id, 
                userEntity?.username, 
                userEntity?.email, 
                userEntity?.lastLogin, 
                userEntity?.dateJoined,
               null,
                GetProfileInstructorDto(
                    instructorProfileEntity?.id, instructorProfileEntity?.company, instructorProfileEntity?.year, newListInstructor
                )
            )
        } else if(findByEmailEntity?.participant != null && findByEmailEntity?.instructor != null){
            GetProfile(
                userEntity?.id, 
                userEntity?.username, 
                userEntity?.email, 
                userEntity?.lastLogin, 
                userEntity?.dateJoined,
                GetProfileParticipantDto(participantProfileEntity?.id,participantProfileEntity?.university, participantProfileEntity?.isRegistered, newListParticipant),
                GetProfileInstructorDto(instructorProfileEntity?.id, instructorProfileEntity?.company, instructorProfileEntity?.year, newListInstructor)
            )
            
        } else{
            throw Seminar400("오류")
        }
        
    }
    
    fun updateProfile(user: UpdateProfileRequest, token: String): GetProfile{
        
        val userEntity = userRepository.findByEmail(authTokenService.getCurrentEmail(token))

        val qUserEntity: QUserEntity = QUserEntity.userEntity
        val qSeminarEntity: QSeminarEntity = QSeminarEntity.seminarEntity
        val qUserSeminarEntity: QUserSeminarEntity = QUserSeminarEntity.userSeminarEntity

        val seminarsList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
            .where(qUserEntity.email.eq(authTokenService.getCurrentEmail(token)))
            .where(qUserSeminarEntity.role.eq("participant")).fetch()

        val instructingSeminarsList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
            .where(qUserEntity.email.eq(authTokenService.getCurrentEmail(token)))
            .where(qUserSeminarEntity.role.eq("instructor")).fetch()

        val newListParticipant = mutableListOf<SeminarsDto>()
        val newListInstructor = mutableListOf<InstructingSeminarsDto>()

        for(i in 0 until seminarsList.size){
            val seminarEntity = seminarsList[i].seminarEntity
            val studentSeminarEntity = seminarsList[i].userSeminarEntity
            newListParticipant.add(
                SeminarsDto(
                    seminarEntity?.id,
                    seminarEntity?.name,
                    studentSeminarEntity?.joinedAt,
                    studentSeminarEntity?.isActive,
                    studentSeminarEntity?.droppedAt

                )
            )
        }

        for(i in 0 until instructingSeminarsList.size){
            val seminarEntity = instructingSeminarsList[i].seminarEntity
            val teacherSeminarEntity = instructingSeminarsList[i].userSeminarEntity
            newListInstructor.add(
                InstructingSeminarsDto(
                    seminarEntity?.id,
                    seminarEntity?.name,
                    teacherSeminarEntity?.joinedAt,

                    )
            )
        }

        if(userEntity?.participant != null && userEntity?.instructor == null){
            val participantProfileEntity = participantProfileRepository.findById(authTokenService.getCurrentParticipantId(token)).get()

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
            
            return GetProfile(
                userEntity.id,
                userEntity.username,
                userEntity.email,
                userEntity.lastLogin,
                userEntity.dateJoined,
                GetProfileParticipantDto(participantProfileEntity.id,participantProfileEntity.university, participantProfileEntity.isRegistered, newListParticipant),
                null)
        } else if(userEntity?.participant == null && userEntity?.instructor != null){
            val instructorProfileEntity = instructorProfileRepository.findById(authTokenService.getCurrentInstructorId(token)).get()

           val year = user.instructor?.year
            if (year != null) {
                if(year<0) {
                    throw Seminar400("0 또는 양의 정수만 입력할 수 있습니다")
                }
            }
            userEntity.let {
                it?.username = user.username
                it?.password = user.password
                it?.instructor?.company = user.instructor?.company.toString()
                it?.instructor?.year = user.instructor?.year
            }
            instructorProfileEntity.let { 
                it.company = user.instructor?.company ?: ""
                it.year = user.instructor?.year 
                
            }
            if (userEntity != null) {
                userRepository.save(userEntity)
            }
            instructorProfileRepository.save(instructorProfileEntity)

            return GetProfile(
                userEntity?.id,
                userEntity?.username,
                userEntity?.email,
                userEntity?.lastLogin,
                userEntity?.dateJoined,
               null,
                GetProfileInstructorDto(instructorProfileEntity.id, instructorProfileEntity.company, instructorProfileEntity.year, newListInstructor)
            )
        } else if(userEntity?.participant != null && userEntity?.instructor != null){
            val participantProfileEntity = participantProfileRepository.findById(authTokenService.getCurrentParticipantId(token)).get()
            val instructorProfileEntity = instructorProfileRepository.findById(authTokenService.getCurrentInstructorId(token)).get()
            userEntity.let {
                it.username = user.username
                it.password = user.password
                it.participant?.university = user.participant?.university.toString()
                it.instructor?.company = user.instructor?.company.toString()
                it.instructor?.year = user.instructor?.year
            }
            participantProfileEntity.let {
                it.university = user.participant?.university ?: ""
            }
            instructorProfileEntity.let {
                it.company = user.instructor?.company ?: ""
                it.year = user.instructor?.year
            }
            userRepository.save(userEntity)
            participantProfileRepository.save(participantProfileEntity)
            instructorProfileRepository.save(instructorProfileEntity)
            
            return GetProfile(
                userEntity.id,
                userEntity.username,
                userEntity.email,
                userEntity.lastLogin,
                userEntity.dateJoined,
                GetProfileParticipantDto(participantProfileEntity.id,participantProfileEntity.university, participantProfileEntity.isRegistered,newListParticipant),
                GetProfileInstructorDto(instructorProfileEntity.id, instructorProfileEntity.company, instructorProfileEntity.year,newListInstructor)
            )
        } else{
            throw Seminar400("오류입니다")
        }
    }
    
    
    fun beParticipant(participant: BeParticipantRequest, token: String):GetProfile {
        val userEntity = userRepository.findByEmail(authTokenService.getCurrentEmail(token))
        
        if(userEntity?.participant != null) {
            throw Seminar409("이미 참가자로 등록되어 있습니다")
        }

        val qUserEntity: QUserEntity = QUserEntity.userEntity
       
        val qSeminarEntity: QSeminarEntity = QSeminarEntity.seminarEntity
        val qUserSeminarEntity: QUserSeminarEntity = QUserSeminarEntity.userSeminarEntity

        val seminarsList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
            .where(qUserEntity.email.eq(authTokenService.getCurrentEmail(token)))
            .where(qUserSeminarEntity.role.eq("participant")).fetch()

        val instructingSeminarsList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
            .where(qUserEntity.email.eq(authTokenService.getCurrentEmail(token)))
            .where(qUserSeminarEntity.role.eq("instructor")).fetch()

        val newListParticipant = mutableListOf<SeminarsDto>()
        val newListInstructor = mutableListOf<InstructingSeminarsDto>()

        for(i in 0 until seminarsList.size){
            val seminarEntity = seminarsList[i].seminarEntity
            val studentSeminarEntity = seminarsList[i].userSeminarEntity
            newListParticipant.add(
                SeminarsDto(
                    seminarEntity?.id,
                    seminarEntity?.name,
                    studentSeminarEntity?.joinedAt,
                    studentSeminarEntity?.isActive,
                    studentSeminarEntity?.droppedAt

                )
            )
        }

        for(i in 0 until instructingSeminarsList.size){
            val seminarEntity = instructingSeminarsList[i].seminarEntity
            val teacherSeminarEntity = instructingSeminarsList[i].userSeminarEntity
            newListInstructor.add(
                InstructingSeminarsDto(
                    seminarEntity?.id,
                    seminarEntity?.name,
                    teacherSeminarEntity?.joinedAt,

                    )
            )
        }
        val participantEntity = participantProfileRepository.save(ParticipantProfileEntity(participant.university, participant.isRegistered))
        
        
        val newEntity = UserEntity(
            userEntity?.username,
            userEntity?.email,
            userEntity?.password,
            userEntity?.dateJoined,
            userEntity?.lastLogin,
            participantEntity,
            userEntity?.instructor
        )
        userRepository.save(newEntity)

        if (userEntity != null) {
            userRepository.delete(userEntity)
        }

        return GetProfile(
            userEntity?.id,
            userEntity?.username,
            userEntity?.email,
            userEntity?.lastLogin,
            userEntity?.dateJoined,
            GetProfileParticipantDto(participantEntity.id,participantEntity.university, participantEntity.isRegistered,newListParticipant),
            GetProfileInstructorDto(userEntity?.instructor?.id, userEntity?.instructor?.company, userEntity?.instructor?.year,newListInstructor)
        )
    }
    
    private fun UserProfile(user: UserEntity, token: String) = user.run {

        UpdateProfileRequest(
            username = user.username,
            password = user.password,
          
            participant = UpdateParticipantProfileDto(user.participant?.university),
            instructor = UpdateInstructorProfileDto(user.instructor?.company, user.instructor?.year)
            
            
        )
    }

    
    private fun makeUserProfileDto(id: Long, qUserEntity: QUserEntity, qParticipantProfileEntity: QParticipantProfileEntity?, qInstructorProfileEntity: QInstructorProfileEntity?):List<UserProfileDto>{
        return queryFactory.select(Projections.constructor(
            UserProfileDto::class.java,
            qUserEntity,
            qParticipantProfileEntity,
            qInstructorProfileEntity
        ))
            .from(qUserEntity)
            .leftJoin(qParticipantProfileEntity).on(qUserEntity.participant.id.eq(qParticipantProfileEntity?.id))
            .leftJoin(qInstructorProfileEntity).on(qUserEntity.instructor.id.eq(qInstructorProfileEntity?.id))
            .where(qUserEntity.id.eq(id))
            .fetch()
    }
  
}