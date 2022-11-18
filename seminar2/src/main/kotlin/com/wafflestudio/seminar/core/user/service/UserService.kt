package com.wafflestudio.seminar.core.user.service

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.user.domain.QUserEntity.userEntity
import com.wafflestudio.seminar.core.user.api.request.BeParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.UpdateProfileRequest
import com.wafflestudio.seminar.core.user.api.response.GetProfile
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.*
import com.wafflestudio.seminar.core.user.domain.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.user.domain.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.user.dto.seminar.SeminarInfoDto
import com.wafflestudio.seminar.core.user.dto.seminar.UserProfileDto
import com.wafflestudio.seminar.core.user.dto.user.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class UserService(
        private val userRepository: UserRepository,
        private val participantProfileRepository: ParticipantProfileRepository,
        private val instructorProfileRepository: InstructorProfileRepository,
        private val authTokenService: AuthTokenService,
        private val queryFactory: JPAQueryFactory,
) {
    fun getProfile(id: Long, userId: Long?): GetProfile {

        if(userId == null) { throw Seminar404("해당하는 유저가 없습니다") }
        val user = userRepository.findByIdOrNull(userId) ?: throw Seminar404("해당하는 유저가 없습니다")

        if (userId != id) {
            throw Seminar401("정보에 접근할 수 없습니다")
        }


        val profileEntity: UserEntity = queryFactory.select(userEntity).from(userEntity).where(userEntity.id.eq(userId)).fetchOne()
                ?: throw Seminar404("해당하는 유저가 없습니다")

        val seminarsDto: List<SeminarsDto>? = queryFactory.select(Projections.constructor(
                SeminarsDto::class.java, seminarEntity.id, seminarEntity.name,
                userSeminarEntity.joinedAt, userSeminarEntity.isActive, userSeminarEntity.droppedAt))
                .from(seminarEntity)
                .leftJoin(userSeminarEntity).on(seminarEntity.id.eq(userSeminarEntity.seminar.id)).fetchJoin()
                .where(userSeminarEntity.user.id.eq(userId), userSeminarEntity.role.eq("PARTICIPANT")).fetch()

        val instructingSeminarsDto: List<InstructingSeminarsDto>? = queryFactory.select(Projections.constructor(
                InstructingSeminarsDto::class.java, seminarEntity.id, seminarEntity.name, userSeminarEntity.joinedAt))
                .from(seminarEntity)
                .leftJoin(userSeminarEntity).on(seminarEntity.id.eq(userSeminarEntity.seminar.id)).fetchJoin()
                .where(userSeminarEntity.user.id.eq(userId), userSeminarEntity.role.eq("INSTRUCTOR")).fetch()

        return GetProfile.of(profileEntity, seminarsDto,instructingSeminarsDto)
    }

    @Transactional
    fun updateProfile(user: UpdateProfileRequest, userId: Long?): GetProfile {

        // todo: 예외 제대로 찾기
        val userEntity1 = userRepository.findByIdOrNull(userId) ?: throw Seminar404("해당하는 유저가 없습니다")
        
        
        userEntity1.let {
            it.username = user.username
            it.password = user.password
            it.participant?.university = user.participant?.university
            it.instructor?.company = user.instructor?.company
            it.instructor?.year = user.instructor?.year
        }
        userEntity1.participant?.let {
            it.university = user.participant?.university
        }
        userEntity1.instructor?.let {
            it.company = user.instructor?.company
            it.year = user.instructor?.year
        }

//        if(participantProfileEntity != null) participantProfileRepository.save(participantProfileEntity)
//        if(instructorProfileEntity != null) instructorProfileRepository.save(instructorProfileEntity)
        
        val profileEntity: UserEntity = queryFactory.select(userEntity).from(userEntity).where(userEntity.id.eq(userId)).fetchOne()
                ?: throw Seminar404("해당하는 유저가 없습니다")

        val seminarsDto: List<SeminarsDto>? = queryFactory.select(Projections.constructor(
                SeminarsDto::class.java, seminarEntity.id, seminarEntity.name,
                userSeminarEntity.joinedAt, userSeminarEntity.isActive, userSeminarEntity.droppedAt))
                .from(seminarEntity)
                .leftJoin(userSeminarEntity).on(seminarEntity.id.eq(userSeminarEntity.seminar.id)).fetchJoin()
                .where(userSeminarEntity.user.id.eq(userId), userSeminarEntity.role.eq("PARTICIPANT")).fetch()

        val instructingSeminarsDto: List<InstructingSeminarsDto>? = queryFactory.select(Projections.constructor(
                InstructingSeminarsDto::class.java, seminarEntity.id, seminarEntity.name, userSeminarEntity.joinedAt))
                .from(seminarEntity)
                .leftJoin(userSeminarEntity).on(seminarEntity.id.eq(userSeminarEntity.seminar.id)).fetchJoin()
                .where(userSeminarEntity.user.id.eq(userId), userSeminarEntity.role.eq("INSTRUCTOR")).fetch()

        return GetProfile.of(profileEntity, seminarsDto,instructingSeminarsDto)
       
    }


    @Transactional
    fun beParticipant(participant: BeParticipantRequest, userId: Long?): GetProfile {
        if(userId == null) { throw Seminar404("해당하는 유저가 없습니다")}
        val user = userRepository.findByIdOrNull(userId) ?: throw Seminar404("해당하는 유저가 없습니다")

        if (user.participant != null) {
            throw Seminar409("이미 참가자로 등록되어 있습니다")
        } else{
            user.participant = ParticipantProfileEntity(participant.university, participant.isRegistered)
        }

        val profileEntity: UserEntity = queryFactory.select(userEntity).from(userEntity).where(userEntity.id.eq(userId)).fetchOne()
                ?: throw Seminar404("해당하는 유저가 없습니다")

        val seminarsDto: List<SeminarsDto>? = queryFactory.select(Projections.constructor(
                SeminarsDto::class.java, seminarEntity.id, seminarEntity.name,
                userSeminarEntity.joinedAt, userSeminarEntity.isActive, userSeminarEntity.droppedAt))
                .from(seminarEntity)
                .leftJoin(userSeminarEntity).on(seminarEntity.id.eq(userSeminarEntity.seminar.id)).fetchJoin()
                .where(userSeminarEntity.user.id.eq(userId), userSeminarEntity.role.eq("PARTICIPANT")).fetch()

        val instructingSeminarsDto: List<InstructingSeminarsDto>? = queryFactory.select(Projections.constructor(
                InstructingSeminarsDto::class.java, seminarEntity.id, seminarEntity.name, userSeminarEntity.joinedAt))
                .from(seminarEntity)
                .leftJoin(userSeminarEntity).on(seminarEntity.id.eq(userSeminarEntity.seminar.id)).fetchJoin()
                .where(userSeminarEntity.user.id.eq(userId), userSeminarEntity.role.eq("INSTRUCTOR")).fetch()

        return GetProfile.of(profileEntity, seminarsDto,instructingSeminarsDto)

      
     
    }


    

}