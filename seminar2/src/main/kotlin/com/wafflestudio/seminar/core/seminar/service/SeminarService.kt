package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.join.UserSeminarEntity
import com.wafflestudio.seminar.core.join.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.changeTimeStringToMinutes
import com.wafflestudio.seminar.core.seminar.database.changeTotalMinutesToTimeString
import com.wafflestudio.seminar.core.seminar.dto.*
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.dto.InstructorUserResponse
import com.wafflestudio.seminar.core.user.dto.ParticipantUserResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

interface SeminarService {
    fun createSeminarAndReturnSeminarDetail(
        seminarPostRequest: SeminarPostRequest,
        meUser: UserEntity
    ): SeminarDetailResponse

    fun modifySeminarAndReturnSeminarDetail(
        seminarPutRequest: SeminarPutRequest,
        meUser: UserEntity
    ): SeminarDetailResponse

    fun getSeminarDetailById(seminarId: Long): SeminarDetailResponse
    fun getSeminarListQueriedByNameAndOrder(name: String, order: String): List<SeminarQueryElementResponse>
    fun attendUserToSeminarAndReturnSeminarDetail
    (seminarId: Long, seminarRegisterRequest: SeminarRegisterRequest, meUser: UserEntity):
        SeminarDetailResponse

    fun dropUserFromSeminar(seminarId: Long, meUser: UserEntity): Any
}

@Service
class SeminarServiceImpl(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
) : SeminarService {

    @Transactional
    override fun createSeminarAndReturnSeminarDetail(
        seminarPostRequest: SeminarPostRequest,
        meUser: UserEntity
    ): SeminarDetailResponse {
        val instructorProfile = meUser.instructorProfile
            ?: throw Seminar403("Not an instructor.")

        if (userSeminarRepository.existsByUserAndRole(meUser, INSTRUCTOR)) {
            throw Seminar400("Already instructing seminar")
        }

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

        return getSeminarDetailById(seminar.id)
    }

    @Transactional
    override fun modifySeminarAndReturnSeminarDetail(
        seminarPutRequest: SeminarPutRequest,
        meUser: UserEntity
    ): SeminarDetailResponse {
        // Exception checks
        val seminar = seminarRepository.findByIdOrNull(seminarPutRequest.id!!)
            ?: throw Seminar403("Seminar doesn't exists.")

        if (seminar.created_user != meUser) {
            throw Seminar403("Not a creator of seminar.")
        }

        // Modify if given
        seminarPutRequest.run {
            name?.let { seminar.name = it }
            capacity?.let { seminar.capacity = it }
            count?.let { seminar.count = it }
            time?.let { seminar.time = changeTimeStringToMinutes(it) }
            online?.let { seminar.online = it }
        }
        seminarRepository.save(seminar)

        return getSeminarDetailById(seminar.id)
    }

    override fun getSeminarDetailById(seminarId: Long): SeminarDetailResponse {
        val queryResult = seminarRepository.querySeminarDetail(seminarId)
            ?: throw Seminar404("Seminar $seminarId does not exists.")
        val seminar = queryResult.first
        val userSeminarList = queryResult.second
        return makeSeminarDetailFromQuery(seminar, userSeminarList)
    }

    override fun getSeminarListQueriedByNameAndOrder(
        name: String,
        order: String
    ): List<SeminarQueryElementResponse> {
        val seminarList = seminarRepository.queryWithNameByOrder(name, order)
        val seminarQueryResponseList = seminarList.map {
            SeminarQueryElementResponse(
                id = it.id,
                name = it.name,
                instructors = userSeminarRepository
                    .findAllBySeminarAndRole(it, INSTRUCTOR)
                    .map { us ->
                        InstructorUserResponse(
                            id = us.user.id,
                            username = us.user.username,
                            email = us.user.email,
                            joinedAt = us.createdAt!!,
                        )
                    },
                participantCount = userSeminarRepository.countAllBySeminarAndRole(it, PARTICIPANT),
            )
        }
        return seminarQueryResponseList
    }

    override fun attendUserToSeminarAndReturnSeminarDetail(
        seminarId: Long,
        seminarRegisterRequest: SeminarRegisterRequest,
        meUser: UserEntity
    ): SeminarDetailResponse {
        val seminar = seminarRepository.findByIdOrNull(seminarId)
            ?: throw Seminar404("Seminar $seminarId not found")

        // if already in seminar, throw error
        if (userSeminarRepository.existsByUserAndSeminar(meUser, seminar)) {
            throw Seminar400("Already in Seminar")
        }

        // if already instructing some seminar, throw error
        if (userSeminarRepository.existsByUserAndRole(meUser, INSTRUCTOR)) {
            throw Seminar400("Already instructor in other seminar")
        }

        @Transactional
        fun attendSeminar(r: String) {
            userSeminarRepository.save(
                UserSeminarEntity(
                    user = meUser,
                    seminar = seminar,
                    isActive = true,
                    role = r
                )
            )
        }

        when (seminarRegisterRequest.role) {
            PARTICIPANT -> {
                if (meUser.participantProfile == null) {
                    throw Seminar403("Wrong role given.")
                } else if (!meUser.participantProfile!!.isRegistered) {
                    throw Seminar403("Not registered")
                } else {
                    // if seminar is full
                    if (userSeminarRepository.countAllBySeminarAndRole(seminar, PARTICIPANT) >= seminar.count) {
                        throw Seminar400("Seminar already full.")
                    } else {
                        attendSeminar(PARTICIPANT)
                    }
                }
            }
            INSTRUCTOR -> {
                attendSeminar(INSTRUCTOR)
            }
            else -> throw Seminar400("Invalid role given.")
        }

        // If reach to here, it means there was no problem.
        return getSeminarDetailById(seminarId)
    }

    override fun dropUserFromSeminar(seminarId: Long, meUser: UserEntity): Any {
        val seminar = seminarRepository.findByIdOrNull(seminarId)
            ?: throw Seminar404("No seminar $seminarId exists.")

        if (userSeminarRepository.existsByUserAndSeminarAndRole(meUser, seminar, INSTRUCTOR)) {
            throw Seminar403("Seminar instructor cannot drop seminar.")
        }

        val userSeminar = userSeminarRepository.findByUserAndSeminarAndRole(meUser, seminar, PARTICIPANT)
            ?.run {
                isActive = false
                droppedAt = LocalDateTime.now()
                userSeminarRepository.save(this)
                return getSeminarDetailById(seminarId)
            }

        return ResponseEntity<Any>(HttpStatus.OK)
    }

    fun makeSeminarDetailFromQuery(
        seminar: SeminarEntity,
        userSeminarList: List<UserSeminarEntity>
    ): SeminarDetailResponse {
        val instructorUserResponseList: MutableList<InstructorUserResponse> = mutableListOf()
        val participantUserResponseList: MutableList<ParticipantUserResponse> = mutableListOf()

        userSeminarList.forEach {
            when (it.role) {
                INSTRUCTOR -> instructorUserResponseList.add(
                    InstructorUserResponse(
                        id = it.user.id,
                        username = it.user.username,
                        email = it.user.email,
                        joinedAt = it.createdAt!!,
                    )
                )
                PARTICIPANT -> participantUserResponseList.add(
                    ParticipantUserResponse(
                        id = it.user.id,
                        username = it.user.username,
                        email = it.user.email,
                        joinedAt = it.createdAt!!,
                        isActive = it.isActive,
                        droppedAt = it.droppedAt,
                    )
                )
            }
        }

        val seminarDetailResponse = SeminarDetailResponse(
            id = seminar.id,
            name = seminar.name,
            capacity = seminar.capacity,
            count = seminar.count,
            time = changeTotalMinutesToTimeString(seminar.time),
            online = seminar.online,
            instructors = instructorUserResponseList,
            participants = participantUserResponseList,
        )
        return seminarDetailResponse
    }
}