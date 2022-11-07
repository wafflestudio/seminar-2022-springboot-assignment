package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.MakeSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.response.*
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.userseminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.userseminar.database.UserSeminarRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Arrays
import java.util.Collections

interface SeminarService {
    fun makeSeminar(id: Long, seminar: MakeSeminarRequest): MakeSeminarResponse
    fun putSeminar(id: Long, seminar: MakeSeminarRequest): MakeSeminarResponse
    fun getSeminar(seminar_id: Long): MakeSeminarResponse
    fun querySeminar(name: String?, order: String?) : QuerySeminarResponses
    fun joinSeminar(id: Long, request: JoinSeminarRequest, seminar_id: Long) : MakeSeminarResponse
    fun deleteSeminar(id: Long, seminar_id: Long)
}

@Service
class SeminarServiceImpl(
        val userRepository: UserRepository,
        val authTokenService: AuthTokenService,
        val authConfig: AuthConfig,
        val userSeminarRepository: UserSeminarRepository,
        val seminarRepository: SeminarRepository
) : SeminarService {
    override fun makeSeminar(id: Long, seminar: MakeSeminarRequest): MakeSeminarResponse {
        if(seminar.name==null) throw Seminar400("세미나 이름을 입력해주세요.")
        if(seminar.capacity==null) throw Seminar400("세미나 정원을 입력해주세요.")
        if(seminar.count==null) throw Seminar400("세미나 횟수를 입력해주세요.")
        if(seminar.time==null) throw Seminar400("세미나 시간을 입력해주세요.")
        
        if(seminar.name=="") throw Seminar400("세미나 이름이 0글자입니다.")
        if(seminar.capacity<=0) throw Seminar400("세미나 정원은 양의 정수여야 합니다.")
        if(seminar.count<=0) throw Seminar400("세미나 횟수는 양의 정수여야 합니다.")
        
        val userEntity = userRepository.findByIdOrNull(id) ?: throw Seminar404("User id를 찾을 수 없습니다.")
        if(userEntity.instructorProfileEntity==null) throw Seminar403("진행자 권한이 없습니다.")
        if(userSeminarRepository.findByUserAndRole(userEntity, "instructor").isNotEmpty()) throw Seminar400("이미 진행하고 있는 세미나가 있습니다.")

        val seminarEntity = SeminarEntity(seminar.name, seminar.capacity, seminar.count, seminar.time, seminar.online)
        seminarRepository.save(seminarEntity)
        val userSeminarEntity = UserSeminarEntity(seminarEntity, userEntity, "instructor", true, null)
        userSeminarRepository.save(userSeminarEntity)

        return getSeminar(seminarEntity.id)
    }

    override fun putSeminar(id: Long, seminar: MakeSeminarRequest): MakeSeminarResponse {
        if(seminar.name==null) throw Seminar400("세미나 이름을 입력해주세요.")
        if(seminar.capacity==null) throw Seminar400("세미나 정원을 입력해주세요.")
        if(seminar.count==null) throw Seminar400("세미나 횟수를 입력해주세요.")
        if(seminar.time==null) throw Seminar400("세미나 시간을 입력해주세요.")

        if(seminar.name=="") throw Seminar400("세미나 이름이 0글자입니다.")
        if(seminar.capacity<=0) throw Seminar400("세미나 정원은 양의 정수여야 합니다.")
        if(seminar.count<=0) throw Seminar400("세미나 횟수는 양의 정수여야 합니다.")

        val userEntity = userRepository.findByIdOrNull(id) ?: throw Seminar404("User id를 찾을 수 없습니다.")
        if(userEntity.instructorProfileEntity==null) throw Seminar403("진행자 권한이 없습니다.")
        if(userSeminarRepository.findByUserAndRole(userEntity, "instructor").isEmpty()) throw Seminar400("진행하고 있는 세미나가 없습니다.")
        val seminarEntity = userSeminarRepository.findByUserAndRole(userEntity, "instructor")[0].seminar
        
        seminarEntity.name = seminar.name
        seminarEntity.capacity = seminar.capacity
        seminarEntity.count = seminar.count
        seminarEntity.time = seminar.time
        seminarEntity.online = seminar.online
        
        seminarRepository.save(seminarEntity)
        return getSeminar(seminarEntity.id)
    }

    override fun getSeminar(seminar_id: Long): MakeSeminarResponse {
        var seminar = seminarRepository.findByIdOrNull(seminar_id) ?: throw Seminar404("세미나 id를 찾을 수 없습니다.")
        var seminarList = userSeminarRepository.findBySeminar(seminar)
        var participantResponse: ArrayList<ParticipantResponse> = arrayListOf()
        var instructorResponse: ArrayList<InstructorResponse> = arrayListOf()
        for(userseminar in seminarList) {
            if(userseminar.role.equals("instructor")) instructorResponse.add(InstructorResponse(userseminar.user.id, userseminar.user.username, userseminar.user.email, userseminar.user.createdAt!!))
            if(userseminar.role.equals("participant")) participantResponse.add(ParticipantResponse(userseminar.user.id, userseminar.user.username, userseminar.user.email, userseminar.createdAt!!, userseminar.isActive!!, userseminar.droppedAt))
        }
        val MakeSeminarResponse = MakeSeminarResponse(seminar.id, seminar.name, seminar.capacity, seminar.count, seminar.time, seminar.online, instructorResponse, participantResponse)
        return MakeSeminarResponse
    }

    override fun querySeminar(name: String?, order: String?): QuerySeminarResponses {
        var querySeminarResponses : ArrayList<QuerySeminarResponse> = arrayListOf()
        if(name==null) {
            val seminars = seminarRepository.findAllByOrderByCreatedAt()
            seminars.reverse()
            for(seminar in seminars) {
                var seminarList = userSeminarRepository.findBySeminar(seminar)
                var instructorResponse: ArrayList<InstructorResponse> = arrayListOf()
                var cnt: Long = 0
                for(userseminar in seminarList) {
                    if(userseminar.role.equals("instructor")) instructorResponse.add(InstructorResponse(userseminar.user.id, userseminar.user.username, userseminar.user.email, userseminar.user.createdAt!!))
                    if(userseminar.role.equals("participant") && userseminar.isActive!!) cnt++
                }
                querySeminarResponses.add(QuerySeminarResponse(seminar.id, seminar.name, instructorResponse, cnt))
            }
        }
        else {
            val seminars = seminarRepository.findAllByNameContainingOrderByCreatedAt(name)
            seminars.reverse()
            for(seminar in seminars) {
                var seminarList = userSeminarRepository.findBySeminar(seminar)
                var instructorResponse: ArrayList<InstructorResponse> = arrayListOf()
                var cnt: Long = 0
                for(userseminar in seminarList) {
                    if(userseminar.role.equals("instructor")) instructorResponse.add(InstructorResponse(userseminar.user.id, userseminar.user.username, userseminar.user.email, userseminar.user.createdAt!!))
                    if(userseminar.role.equals("participant") && userseminar.isActive!!) cnt++
                }
                querySeminarResponses.add(QuerySeminarResponse(seminar.id, seminar.name, instructorResponse, cnt))
            }
        }
        if(order!=null && order.equals("earliest")) {
            querySeminarResponses.reverse()
        }
        return QuerySeminarResponses(querySeminarResponses)
    }

    override fun joinSeminar(id: Long, request: JoinSeminarRequest, seminar_id: Long): MakeSeminarResponse {
        var seminar = seminarRepository.findByIdOrNull(seminar_id) ?: throw Seminar404("세미나 id를 찾을 수 없습니다.")
        val userEntity = userRepository.findByIdOrNull(id) ?: throw Seminar404("User id를 찾을 수 없습니다.")
        if(!request.role.equals("participant") && !request.role.equals("instructor")) throw Seminar400("role이 instructor이나 participant가 아닙니다.")
        if(userSeminarRepository.findByUserAndSeminarAndRole(userEntity, seminar, "participant").isNotEmpty() && !userSeminarRepository.findByUserAndSeminarAndRole(userEntity, seminar, "participant")[0].isActive) throw Seminar400("세미나를 드랍한 회원은 다시 세미나에 참여할 수 없습니다.")
        if(userSeminarRepository.findByUserAndSeminar(userEntity, seminar).isNotEmpty()) throw Seminar400("이미 세미나에 참가하고 있습니다.")
        if(request.role.equals("participant")) {
            if(userEntity.participantProfileEntity==null) throw Seminar403("수강생 자격이 없습니다.")
            if(!userEntity.participantProfileEntity!!.isRegistered) throw Seminar403("활성회원이 아닙니다.")
            if(userSeminarRepository.findBySeminarAndRole(seminar, "participant").size.equals(seminar.capacity)) throw Seminar400("세미나가 다 찼습니다.")
        }   
        else {
            if(userSeminarRepository.findByUserAndRole(userEntity, "instructor").isNotEmpty()) throw Seminar400("진행하고 있는 세미나가 있습니다.")
        }
        val userSeminarEntity = UserSeminarEntity(seminar, userEntity, request.role, true, null)
        userSeminarRepository.save(userSeminarEntity)
        return getSeminar(seminar_id)
    }

    override fun deleteSeminar(id: Long, seminar_id: Long) {
        var seminar = seminarRepository.findByIdOrNull(seminar_id) ?: throw Seminar404("세미나 id를 찾을 수 없습니다.")
        val userEntity = userRepository.findByIdOrNull(id) ?: throw Seminar404("User id를 찾을 수 없습니다.")
        if(userSeminarRepository.findByUserAndSeminarAndRole(userEntity, seminar, "instructor").isNotEmpty()) throw Seminar403("세미나 진행자는 드랍할 수 없습니다.")
        val a = userSeminarRepository.findByUserAndSeminarAndRole(userEntity, seminar, "participant")
        if(a.isEmpty()) return
        if(!a[0].isActive) throw Seminar400("이미 드랍한 수강생입니다.")
        a[0].isActive=false
        a[0].droppedAt=LocalDateTime.now()
        userSeminarRepository.save(a[0])
    }
}