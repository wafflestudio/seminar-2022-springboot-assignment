package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarDTO
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarDTO
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarDTO
import com.wafflestudio.seminar.core.seminar.database.*
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SeminarService(
    private val seminarRepository: SeminarRepository,
    private val seminarDslRepository: SeminarDslRepository,
    private val userSeminarRepository: UserSeminarRepository
) {
    fun getSeminar(id: Long): Seminar {
        val seminarEntity = seminarRepository.findByIdOrNull(id) ?: throw Seminar400("$id 의 세미나는 존재하지 않습니다.")
        return seminarEntity.toSeminar()
    }

    @Transactional(readOnly = true)
    fun getSeminarList(name: String, order: String): List<Seminar> {
        val isAscending = order == "earliest"
        val seminars = seminarDslRepository.getList(name, isAscending)
        
        return seminars.map { it.toSeminar() }
    }
    
    @Transactional
    fun createSeminar(user: UserEntity, createSeminarDTO: CreateSeminarDTO): Seminar {
        if (user.instructor == null) {
            throw Seminar400("강사가 아닌 사용자는 세미나를 생성할 수 없습니다.")
        }
        
        val seminarEntity = seminarRepository.save(createSeminarDTO.toEntity())
        val userSeminarEntity = userSeminarRepository.save(UserSeminarEntity(
            user = user,
            seminar = seminarEntity,
            role = "instructor",
        ))
        user.userSeminars.add(userSeminarEntity)
        seminarEntity.userSeminars.add(userSeminarEntity)
        
        return seminarEntity.toSeminar()
    }
    
    @Transactional
    fun updateSeminar(userEntity: UserEntity, seminarId: Long, updateSeminarDTO: UpdateSeminarDTO): Seminar {
        val seminarEntity: SeminarEntity = seminarRepository.findByIdOrNull(seminarId)
            ?: throw Seminar400("$seminarId 의 세미나는 존재하지 않습니다.")
        
        if (updateSeminarDTO.name != null) seminarEntity.name = updateSeminarDTO.name
        if (updateSeminarDTO.time != null) seminarEntity.time = updateSeminarDTO.time
        if (updateSeminarDTO.count != null) seminarEntity.count = updateSeminarDTO.count
        if (updateSeminarDTO.online != null) seminarEntity.online = updateSeminarDTO.online
        
        if (updateSeminarDTO.capacity != null) {
            val curCount = seminarEntity.userSeminars.count { it.role == "participant" }
            if (updateSeminarDTO.capacity < curCount) throw Seminar400("현재 인원보다 작게 capacity 를 설정할 수 없습니다")
            seminarEntity.capacity = updateSeminarDTO.capacity
        }
        
        return seminarEntity.toSeminar()
    }
    
    @Transactional
    fun joinSeminar(userEntity: UserEntity, seminarId: Long, joinSeminarDTO: JoinSeminarDTO): Seminar {
        val role = joinSeminarDTO.role
        if (role != "instructor" && role != "participant")
            throw Seminar400("role 은 instructor 또는 participant 중 하나여야 합니다.")

        if (role == "instructor" && userEntity.instructor == null) {
            throw Seminar400("instructor 가 아닌 유저는 instructor 로 세미나에 참여할 수 없습니다.")
        }
        if (role == "participant" && userEntity.participant == null) {
            throw Seminar400("participant 가 아닌 유저는 participant 로 세미나에 참여할 수 없습니다.")
        }
        
        val seminarEntity = seminarRepository.findByIdOrNull(seminarId)
            ?: throw Seminar400("$seminarId 의 세미나는 존재하지 않습니다.")

        // TODO: queryDSL 사용하기
        val userSeminarEntity = userSeminarRepository.findAll()
                .firstOrNull { it.user.id == userEntity.id && it.seminar.id == seminarId }

        if (userSeminarEntity != null) {
            if (userSeminarEntity.isActive) {
                throw Seminar400("이미 세미나에 참여하고 있습니다.")
            } else {
                throw Seminar400("이전에 드랍한 세미나입니다.")
            }
        }
        
        if (seminarEntity.userSeminars.size >= seminarEntity.capacity)
            throw Seminar400("세미나의 수용 인원이 초과되었습니다.")


        val userSeminar = UserSeminarEntity(
            user = userEntity,
            seminar = seminarEntity,
            role = role,
        )
        seminarEntity.userSeminars.add(userSeminar)
        userEntity.userSeminars.add(userSeminar)
        userSeminarRepository.save(userSeminar)
        
        return seminarEntity.toSeminar()
    }
    
    @Transactional
    fun dropSeminar(userEntity: UserEntity, seminarId: Long): Seminar? {
        val seminarEntity = seminarRepository.findByIdOrNull(seminarId)
            ?: throw Seminar400("$seminarId 의 세미나는 존재하지 않습니다.")

        val userSeminarEntityList = userSeminarRepository.findAll()
        val userSeminarEntity = userSeminarEntityList
            .firstOrNull { it.user.id == userEntity.id && it.seminar.id == seminarId }
            ?: throw Seminar400("세미나에 참여하고 있지 않습니다.")

        val instructorUserSeminarEntityList = userSeminarEntityList
            .filter { it.seminar.id == seminarId && it.role == "instructor" && it.isActive }
        if (instructorUserSeminarEntityList.size == 1 && instructorUserSeminarEntityList[0].user.id == userEntity.id) {
            throw Seminar400("세미나의 강사는 세미나를 드랍할 수 없습니다.")
        }
        
        if (!userSeminarEntity.isActive)
            throw Seminar400("이전에 이미 세미나를 드랍한 적이 있습니다.")

        userSeminarEntity.isActive = false
        userSeminarEntity.droppedAt = LocalDateTime.now()
        userSeminarRepository.save(userSeminarEntity)
        
        return seminarEntity.toSeminar()
    }
}