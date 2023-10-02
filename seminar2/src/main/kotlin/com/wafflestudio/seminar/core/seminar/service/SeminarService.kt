package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.maptable.SeminarUser
import com.wafflestudio.seminar.core.maptable.SeminarUserRepository
import com.wafflestudio.seminar.core.seminar.api.request.RegisterParticipantRequest
import com.wafflestudio.seminar.core.seminar.api.request.createSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.response.CreateSeminarResponse
import com.wafflestudio.seminar.core.seminar.api.response.QuerySeminarResponse
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.user.api.request.RegisterSeminarRequest
import com.wafflestudio.seminar.core.user.api.request.Role
import com.wafflestudio.seminar.core.user.api.response.ProfileResponse
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.database.profile.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.profile.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.service.AuthException
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import javax.servlet.http.HttpServletRequest
import kotlin.streams.toList

@Service
class SeminarService(
    val seminarRepository: SeminarRepository,
    val userRepository: UserRepository,
    val seminarUserRepository: SeminarUserRepository
) {
    fun createSeminar(request: HttpServletRequest, createSeminarRequest: createSeminarRequest) : CreateSeminarResponse {
        val email: String = request.getAttribute("email").toString()
        val user: UserEntity? = userRepository.findByEmail(email)
        user ?: throw AuthException("유저 정보를 불러올 수 없습니다.")
        
        val instructor: InstructorProfileEntity =
            user.instructorProfileEntity ?: throw NoPermissionException("강연자가 아니면 강의를 개설할 수 없습니다.")
        val seminar: SeminarEntity = createSeminarRequest.toSeminar()
        val seminarInstructors = SeminarUser(seminar, user, Role.Instructors)
        user.seminarUser.add(seminarInstructors)
        seminar.seminarUser.add(seminarInstructors)
        val savedUser = userRepository.save(user)
        val savedSeminar = seminarRepository.save(seminar)
        return seminarResponse(seminar)
    }  
    
    fun getSeminar(request: HttpServletRequest, seminar_id: Long) : CreateSeminarResponse{
        val seminar = seminarRepository.findById(seminar_id).orElseThrow {WrongSeminarIdException("해당 id에 맞는 세미나가 없습니다.") }
        return seminarResponse(seminar)
       
    }
    
    fun getQuerySeminar(request: HttpServletRequest, order: String, name: String): List<QuerySeminarResponse>{
        return if(order == "earliest"){
            seminarRepository.findByNameContainingOrderByCreatedAtAsc(name)
                .stream()
                .map(QuerySeminarResponse.Companion::toQuerySeminarReseponse)
                .toList()
        }else{
            seminarRepository.findByNameContainingOrderByCreatedAtDesc(name)
                .stream()
                .map(QuerySeminarResponse.Companion::toQuerySeminarReseponse)
                .toList()
        }
    }
    
    fun registerToParticipant(request: HttpServletRequest, registerParticipantRequest: RegisterParticipantRequest) : ProfileResponse{
        val email : String = request.getAttribute("email").toString()
        val user : UserEntity? = userRepository.findByEmail(email)

        user ?: throw WrongEmailException("잘못된 토큰 정보입니다.")
        
        if(user.participantProfileEntity != null)
            throw AlreadyReigsteredException("이미 참가자로 등록되어 있습니다")
        
       
        val isRegistered = registerParticipantRequest.isRegistered ?: true
        val university = registerParticipantRequest.university ?: ""
        
            
        val participantProfileEntity = ParticipantProfileEntity(isRegistered, university)
        user.participantProfileEntity = participantProfileEntity
        userRepository.save(user)
        
        return ProfileResponse.toProfileResponse(user)
    }
    
    fun editSeminar(request: HttpServletRequest, createSeminarRequest: createSeminarRequest) : CreateSeminarResponse{
        val email : String = request.getAttribute("email").toString()
        val user : UserEntity? = userRepository.findByEmail(email)

        user ?: throw WrongEmailException("잘못된 토큰 정보입니다.")
        val seminar = seminarUserRepository
            .findByUser(user)
            .stream()
            .filter{ it.role == Role.Instructors }   
            .findAny()
            .orElseThrow { NoPermissionException("수정 권한이 없습니다")}
            .seminar
        
        seminar.name = createSeminarRequest.name
        seminar.capacity = createSeminarRequest.capacity
        seminar.count = createSeminarRequest.count
        seminar.online = createSeminarRequest.online ?: true
        seminarRepository.save(seminar)
        return seminarResponse(seminar)
            
    }
    
    fun registerSeminar(request: HttpServletRequest, seminar_id: Long, registerSeminarRequest: RegisterSeminarRequest): CreateSeminarResponse{
        val seminar = seminarRepository
            .findById(seminar_id)
            .orElseThrow {WrongSeminarIdException("해당 세미나가 존재하지 않습니다.")}
        
        
        val email : String = request.getAttribute("email").toString()
        val user : UserEntity? = userRepository.findByEmail(email)
        user ?: throw WrongEmailException("해당 이메일이 존재하지 않습니다.")
        val seminarUserInstructor = seminarUserRepository.findByUserAndSeminar(user, seminar)
            .stream()
            .filter{ it.role == Role.Instructors}
            .toList()
            
            
        val seminarUserParticipant = seminarUserRepository.findByUserAndSeminar(user, seminar)
            .stream()
            .filter{ it.role == Role.Participants}
            .toList()
               
        
        if(!seminarUserInstructor.isEmpty() || !seminarUserParticipant.isEmpty()){
            if(!seminarUserInstructor.isEmpty() && !seminarUserInstructor[0].isActive){
                throw AlreadyDroppedException("이미 드랍한 수업은 다시 신청할 수 없습니다.")
            }
            throw AlreadyReigsteredException("이미 참여중인 세미나입니다.")
        }
            
        
        
        if(registerSeminarRequest.role == Role.Instructors.label){
            val noHaveInstruct :Boolean = seminarUserRepository.findByUser(user)
                .filter { it.role == Role.Instructors }
                .toList()
                .isEmpty()
            if(!noHaveInstruct){
                throw AlreadyReigsteredException("이미 담당하는 수업이 있습니다.")
            }
            val seminarUser = SeminarUser(seminar, user, Role.Instructors)
            user.seminarUser.add(seminarUser)
            seminar.seminarUser.add(seminarUser)
            seminar.count += 1
            seminarUserRepository.save(seminarUser)
            userRepository.save(user)
            seminar.count += 1
            seminarRepository.save(seminar)
            
        }else if(registerSeminarRequest.role == Role.Participants.label){
            if(seminar.capacity == seminar.count)
                throw FullClassException("수강인원이 많아 신청할 수 없습니다")
            
            val seminarUser = SeminarUser(seminar, user, Role.Participants)
            user.seminarUser.add(seminarUser)
            seminar.seminarUser.add(seminarUser)
            seminarUserRepository.save(seminarUser)
            userRepository.save(user)
            seminarRepository.save(seminar)
        }else{
            throw WrongRoleException("올바른 역할을 입력해주세요.")
        }
        return seminarResponse(seminar)
    }
    fun dropSeminar(request: HttpServletRequest, seminar_id: Long): CreateSeminarResponse{
        val seminar = seminarRepository.findById(seminar_id)
            .orElseThrow{ WrongSeminarIdException("해당 세미나를 찾을 수 없습니다") }

        val email : String = request.getAttribute("email").toString()
        val user : UserEntity? = userRepository.findByEmail(email)
        user ?: throw WrongEmailException("인증 문제가 발생했습니다")
        val seminarUser = seminarUserRepository
            .findByUserAndSeminar(user, seminar)
            .stream()
        
        val isInstructed = seminarUser
            .filter{ it.role == Role.Instructors}
            .toList()
            .isEmpty()
        
        if(!isInstructed){
            throw NoPermissionException("선생님은 드랍할 수 없어요..ㅜㅜ")
        }
        
        val relation = seminarUser.findAny().orElseThrow{WrongSeminarIdException("해당 세미나를 찾을 수 없습니다")}
        relation.dropSeminar()
        seminarRepository.save(seminar)
        seminarUserRepository.save(relation)
        return seminarResponse(seminar)
        
        
    }
    private fun seminarResponse(entity: SeminarEntity) : CreateSeminarResponse{
        val instructorList :ArrayList<UserEntity> = ArrayList()
        entity.seminarUser
            .stream()
            .filter{it.role == Role.Instructors}
            .forEach { instructor ->  instructorList.add(instructor.user)}

        return CreateSeminarResponse(
            id = entity.id,
            name = entity.name,
            capacity = entity.capacity,
            count = entity.count,
            time = entity.time,
            online = entity.online,
            instructors = instructorList,
            participants = listOf()
        )
        
    }
}