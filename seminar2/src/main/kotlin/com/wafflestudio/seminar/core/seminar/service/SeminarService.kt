package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.api.response.SeminarResponse
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.UserSeminar
import com.wafflestudio.seminar.core.user.database.Role
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface SeminarService {
    fun makeSeminar(req: SeminarRequest, user_id: Long) : SeminarResponse
    fun editSeminar(seminar_id: Long, req: EditSeminarRequest, user_id: Long) : SeminarResponse
    fun getSeminar(seminar_id: Long) : SeminarResponse
    fun getAllSeminar() : List<SeminarResponse>
    fun joinSeminar(seminar_id: Long, user_id: Long, req: JoinSeminarRequest) : SeminarResponse
    fun dropSeminar(seminar_id: Long, user_id: Long) : SeminarResponse
}


@Service
class SeminarServiceImpl(
    val userRepository: UserRepository,
    val seminarRepository: SeminarRepository,
    val userSeminarRepository: UserSeminarRepository
) : SeminarService {
    
    override fun makeSeminar(req: SeminarRequest, user_id: Long) : SeminarResponse {
        val user = userRepository.findByIdOrNull(user_id) ?: throw Seminar404("해당하는 유저가 없습니다.")
        if(user.instructorProfile == null) throw Seminar403("instructor 권한이 없습니다.")
        if(userSeminarRepository.findByUserAndRole(user, Role.Instructor) != null) throw Seminar400("이미 세미나를 진행하고 있습니다.")
        val seminar = req.toSeminarEntity()
        val userSeminar = UserSeminar(
            seminar = seminar,
            user = user,
            role = Role.Instructor
        )
        userSeminarRepository.save(userSeminar)
        seminarRepository.save(seminar)
        return seminar.toSeminarResponse(userSeminarRepository)
    }
    
    override fun editSeminar(seminar_id: Long, req: EditSeminarRequest, user_id: Long) : SeminarResponse {
        val seminar = seminarRepository.findByIdOrNull(seminar_id) ?: throw Seminar404("세미나가 존재하지 않습니다.")
        val user = userRepository.findByIdOrNull(user_id) ?: throw Seminar404("유저가 존재하지 않습니다.")
        userSeminarRepository.findByUserAndSeminar(user, seminar) ?: throw Seminar403("수정 권한이 없습니다.")
        seminar.editSeminar(req)
        seminarRepository.save(seminar)
        return seminar.toSeminarResponse(userSeminarRepository)
    }

    override fun getSeminar(seminar_id: Long): SeminarResponse {
        return seminarRepository.findByIdOrNull(seminar_id)?.toSeminarResponse(userSeminarRepository) ?: throw Seminar404("세미나가 존재하지 않습니다.")
    }

    override fun getAllSeminar(): List<SeminarResponse> {
        val entityList = seminarRepository.findAllByOrderByCreatedAtDesc()
        val seminarList = mutableListOf<SeminarResponse>()
        for(entity in entityList) seminarList.add(entity.toSeminarResponse(userSeminarRepository))
        return seminarList
    }

    override fun joinSeminar(seminar_id: Long, user_id: Long, req: JoinSeminarRequest): SeminarResponse {
        val user = userRepository.findByIdOrNull(user_id) ?: throw Seminar404("유저가 존재하지 않습니다.")
        val seminar = seminarRepository.findByIdOrNull(seminar_id) ?: throw Seminar404("세미나가 존재하지 않습니다.")
        val userSeminar : UserSeminar
        val now = userSeminarRepository.findByUserAndSeminar(user, seminar)
        if(now != null) {
            if(now.isActive) throw Seminar400("이미 세미나에 참여중입니다.")
            else throw Seminar400("이미 드랍한 세미나입니다.")
        }
        if(req.role == Role.Participant) {
            if(user.participantProfile == null) throw Seminar400("participant 자격이 없습니다.")
            if(!user.participantProfile!!.isRegistered) throw Seminar400("활성회원이 아닙니다.")
            if(seminar.capacity <= seminar.participantCount) throw Seminar400("세미나의 정원이 다 찼습니다.")
            userSeminar = UserSeminar(seminar = seminar, user = user, role = Role.Participant)
            userSeminarRepository.save(userSeminar)
            seminar.participantCount++
        }
        else {
            if(user.instructorProfile == null) throw Seminar400("instructor 자격이 없습니다.")
            if(userSeminarRepository.findByUserAndRole(user, Role.Instructor) != null) throw Seminar400("이미 세미나를 담당하고 있습니다.")
            userSeminar = UserSeminar(seminar = seminar, user = user, role = Role.Instructor)
            userSeminarRepository.save(userSeminar)
        }
        seminarRepository.save(seminar)
        return seminar.toSeminarResponse(userSeminarRepository)
    }

    override fun dropSeminar(seminar_id: Long, user_id: Long): SeminarResponse {
        val user = userRepository.findByIdOrNull(user_id) ?: throw Seminar404("해당하는 유저가 없습니다.")
        val seminar = seminarRepository.findByIdOrNull(seminar_id) ?: throw Seminar404("세미나를 찾을 수 없습니다.")
        val userSeminar = userSeminarRepository.findByUserAndSeminar(user, seminar) ?: return seminar.toSeminarResponse(userSeminarRepository)
        if(userSeminar.role == Role.Instructor) throw Seminar400("진행자는 세미나를 드랍할 수 없습니다.")
        if(!userSeminar.isActive) throw Seminar400("이미 드랍한 세미나입니다.")
        userSeminar.dropSeminar(seminarRepository)
        userRepository.save(user)
        seminarRepository.save(seminar)
        return seminar.toSeminarResponse(userSeminarRepository)
    }


}