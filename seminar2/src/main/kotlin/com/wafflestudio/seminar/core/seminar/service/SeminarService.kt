package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.INSTRUCTOR
import com.wafflestudio.seminar.common.PARTICIPANT
import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.core.join.UserSeminarEntity
import com.wafflestudio.seminar.core.join.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.changeTimeStringToMinutes
import com.wafflestudio.seminar.core.seminar.database.changeTotalMinutesToTimeString
import com.wafflestudio.seminar.core.seminar.dto.SeminarDetailResponse
import com.wafflestudio.seminar.core.seminar.dto.SeminarPostRequest
import com.wafflestudio.seminar.core.seminar.dto.SeminarPutRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.dto.InstructorUserResponse
import com.wafflestudio.seminar.core.user.dto.ParticipantUserResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

interface SeminarService {
    fun createSeminarAndReturnSeminarDetail(
            seminarPostRequest: SeminarPostRequest, meUser: UserEntity
    ): SeminarDetailResponse

    fun modifySeminarAndReturnSeminarDetail(
            seminarPutRequest: SeminarPutRequest, meUser: UserEntity
    ): SeminarDetailResponse
}

@Service
class SeminarServiceImpl(
        private val userRepository: UserRepository,
        private val seminarRepository: SeminarRepository,
        private val userSeminarRepository: UserSeminarRepository,
): SeminarService {
    
    @Transactional
    override fun createSeminarAndReturnSeminarDetail(
            seminarPostRequest: SeminarPostRequest, meUser: UserEntity
    ): SeminarDetailResponse {
        val instructorProfile = meUser.instructorProfile
                ?: throw Seminar403("Not an instructor.")
        
        val seminar = SeminarEntity(
                name = seminarPostRequest.name,
                capacity = seminarPostRequest.capacity!!,
                count = seminarPostRequest.count!!,
                time = changeTimeStringToMinutes(seminarPostRequest.time!!),
                online = seminarPostRequest.online,
                created_user = meUser
        )
        
        val userSeminar = UserSeminarEntity(
                user = meUser,
                seminar = seminar,
                role = INSTRUCTOR,
        )
        
        seminarRepository.save(seminar)
        userSeminarRepository.save(userSeminar)
        
        return makeSeminarDetail(seminar, userSeminar)
    }

    @Transactional
    override fun modifySeminarAndReturnSeminarDetail(
            seminarPutRequest: SeminarPutRequest, meUser: UserEntity
    ): SeminarDetailResponse {
        // Exception checks
        val seminar = seminarRepository.findByIdOrNull(seminarPutRequest.id!!)
                ?: throw Seminar403("Seminar doesn't exists.")
        
        if (seminar.created_user != meUser) {
            throw Seminar403("Not a creator of seminar.")
        }
        
        // Modify if given
        seminarPutRequest.run{
            name?.let{ seminar.name = it }
            capacity?.let{ seminar.capacity = it }
            count?.let{ seminar.count = it }
            time?.let{ seminar.time = changeTimeStringToMinutes(it) }
            online?.let{ seminar.online = it }
        }
        seminarRepository.save(seminar)
        
        // Since we already checked that meUser is creator of this seminar, it must not null.
        val userSeminar = userSeminarRepository.findByUserAndSeminar(meUser, seminar)!!
        return makeSeminarDetail(seminar, userSeminar)
    }
    
    fun makeSeminarDetail(
            seminar: SeminarEntity, userSeminar: UserSeminarEntity
    ): SeminarDetailResponse {
        
        val instructorUserSeminar = userSeminarRepository.findAllBySeminarAndRole(
                seminar, INSTRUCTOR
        )
        val instructorUserResponseList = instructorUserSeminar.map {
            InstructorUserResponse(
                    id = it.user.id,
                    username = it.user.username,
                    email = it.user.email,
                    joinedAt = it.createdAt!!,
            )
        }
        
        val participantUserSeminar = userSeminarRepository.findAllBySeminarAndRole(
                seminar, PARTICIPANT
        )
        val participantUserResponseList = participantUserSeminar.map {
            ParticipantUserResponse(
                    id = it.user.id,
                    username = it.user.username,
                    email = it.user.email,
                    joinedAt = it.createdAt!!,
                    isActive = it.isActive,
                    droppedAt = it.droppedAt,
            )
        }
        
        val seminarDetailResponse = SeminarDetailResponse(
                id = seminar.id,
                name = seminar.name,
                capacity = seminar.capacity,
                count = seminar.count,
                time = changeTotalMinutesToTimeString(seminar.time),
                online = seminar.online,
                instructors = instructorUserResponseList,
                participants =  participantUserResponseList,
        )
        
        return seminarDetailResponse
    }
}