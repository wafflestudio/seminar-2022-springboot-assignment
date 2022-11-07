package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.config.WebConfig
import com.wafflestudio.seminar.core.user.api.request.LogInRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.PutUserRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.response.*
import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.userseminar.database.UserSeminarRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface AuthService {
    fun signUp(user: SignUpRequest) : AuthToken
    fun logIn(request: LogInRequest) : AuthToken
    fun getUser(id: Long) : GetUserResponse
    fun putUser(id: Long, request: PutUserRequest) : GetUserResponse
    fun getUserId(userEmail: String) : Long
    fun postParticipant(id: Long, request: ParticipantRequest) : GetUserResponse
}

@Service
class AuthServiceImpl(
    val userRepository: UserRepository,
    val authTokenService: AuthTokenService,
    val authConfig: AuthConfig,
    val userSeminarRepository: UserSeminarRepository
) : AuthService {
    
    override fun getUserId(userEmail: String) : Long {
        val userEntity = userRepository.findByEmail(userEmail) ?: throw Seminar404("Email을 찾지 못했습니다.")
        return userEntity.id
    }
    override fun signUp(user: SignUpRequest) : AuthToken {
        if(user.username==null) throw Seminar400("Username을 입력하세요.")
        if(user.email==null) throw Seminar400("Email을 입력하세요.")
        if(user.password==null) throw Seminar400("Password를 입력하세요.")
        if(user.role==null) throw Seminar400("Role을 입력하세요.")
        if(!user.role.equals("participant") and !user.role.equals("instructor") and !user.role.equals("both")) throw Seminar400("올바른 Role을 입력하세요.")
        if(userRepository.findByEmail(user.email) != null) throw Seminar409("Email이 이미 존재합니다.")
        
        val encpassword = authConfig.passwordEncoder().encode(user.password)
        if(user.role.equals("participant")) {
            val userEntity = UserEntity(user.username, user.email, encpassword, ParticipantProfileEntity(user.university, user.isRegistered), null)
            userRepository.save(userEntity)
        }
        if(user.role.equals("instructor")) {
            if(user.year!=null && user.year <0) throw Seminar400("Year은 0 또는 양수여야 합니다.")
            val userEntity = UserEntity(user.username, user.email, encpassword, null, InstructorProfileEntity(user.company, user.year))
            userEntity.createdAt= LocalDateTime.now()
            userEntity.modifiedAt= LocalDateTime.now()
            userRepository.save(userEntity)
        }
        if(user.role.equals("both")) {
            if(user.year!=null && user.year <0) throw Seminar400("Year은 0 또는 양수여야 합니다.")
            val userEntity = UserEntity(user.username, user.email, encpassword, ParticipantProfileEntity(user.university, user.isRegistered), InstructorProfileEntity(user.company, user.year))
            userRepository.save(userEntity)
        }
        return authTokenService.generateTokenByEmail(user.email)
    }
    
    override fun logIn(request: LogInRequest) : AuthToken {
        if(request.email==null) throw Seminar400("Email을 입력하세요.");
        if(request.password==null) throw Seminar400("Password를 입력하세요.");
        val userEntity = userRepository.findByEmail(request.email) ?: throw Seminar404("Email을 찾지 못했습니다.")
        userRepository.save(userEntity)
        if(authConfig.passwordEncoder().matches(request.password, userEntity.password)) return authTokenService.generateTokenByEmail(request.email)
        throw Seminar401("Password가 잘못되었습니다.")
    }

    override fun getUser(id: Long): GetUserResponse {
        val userEntity = userRepository.findByIdOrNull(id) ?: throw Seminar404("User id를 찾을 수 없습니다.")
        var participantResponse: ParticipantResponse? = null
        if(userEntity.participantProfileEntity != null) {
            var seminarlist: MutableList<SeminarResponse> = arrayListOf()
            var arr = userSeminarRepository.findByUserAndRole(userEntity, "participant")
            for (userSeminarEntity in arr) {
                seminarlist.add(SeminarResponse(userSeminarEntity.seminar.id, userSeminarEntity.seminar.name, userSeminarEntity.createdAt!!, userSeminarEntity.isActive!!, userSeminarEntity.droppedAt))
            }
            arr = userSeminarRepository.findByUserAndRole(userEntity, "both")
            for (userSeminarEntity in arr) {
                seminarlist.add(SeminarResponse(userSeminarEntity.seminar.id, userSeminarEntity.seminar.name, userSeminarEntity.createdAt!!, userSeminarEntity.isActive!!, userSeminarEntity.droppedAt))
            }
            participantResponse = ParticipantResponse(
                    userEntity.participantProfileEntity!!.id, 
                    userEntity.participantProfileEntity!!.university,
                    userEntity.participantProfileEntity!!.isRegistered,
                    seminarlist       
            )
        }
        var instructorResponse: InstructorResponse? = null
        if(userEntity.instructorProfileEntity != null) {
            var seminarlist: MutableList<InstructingSeminarResponse> = arrayListOf()
            var instructingSeminarResponse: InstructingSeminarResponse? = null
            var arr = userSeminarRepository.findByUserAndRole(userEntity, "instructor")
            for (userSeminarEntity in arr) {
                seminarlist.add(InstructingSeminarResponse(userSeminarEntity.seminar.id, userSeminarEntity.seminar.name, userSeminarEntity.createdAt!!))
            }
            arr = userSeminarRepository.findByUserAndRole(userEntity, "both")
            for (userSeminarEntity in arr) {
                seminarlist.add(InstructingSeminarResponse(userSeminarEntity.seminar.id, userSeminarEntity.seminar.name, userSeminarEntity.createdAt!!))
            }
            if(!seminarlist.isEmpty()) instructingSeminarResponse = seminarlist[0]
            instructorResponse = InstructorResponse(
                    userEntity.instructorProfileEntity!!.id,
                    userEntity.instructorProfileEntity!!.company,
                    userEntity.instructorProfileEntity!!.year,
                    instructingSeminarResponse
            )
        }
        val getUserResponse = GetUserResponse(
                userEntity.id,
                userEntity.username,
                userEntity.email,
                userEntity.modifiedAt!!,
                userEntity.createdAt!!,
                participantResponse,
                instructorResponse
        )
        return getUserResponse
    }

    override fun putUser(id: Long, request: PutUserRequest) : GetUserResponse{
        val userEntity = userRepository.findByIdOrNull(id) ?: throw Seminar404("User id를 찾을 수 없습니다.")
        if(request.username==null) throw Seminar400("Username을 입력하세요.")
        userEntity.username=request.username
        if(userEntity.participantProfileEntity!=null) {
            if(request.university==null) userEntity.participantProfileEntity!!.university=""
            else userEntity.participantProfileEntity!!.university=request.university
        }
        if(userEntity.instructorProfileEntity!=null) {
            if(request.company==null) userEntity.instructorProfileEntity!!.company=""
            else userEntity.instructorProfileEntity!!.company=request.company
            userEntity.instructorProfileEntity!!.year=request.year
            if(request.year!=null) if(request.year!!>0) throw Seminar400("Year은 0 또는 양수여야 합니다.")
        }
        userRepository.save(userEntity)
        return getUser(id)
    }

    override fun postParticipant(id: Long, request: ParticipantRequest) : GetUserResponse {
        val userEntity = userRepository.findByIdOrNull(id) ?: throw Seminar404("User id를 찾을 수 없습니다.")
        if(userEntity.participantProfileEntity!=null) throw Seminar409("이미 수강자입니다.")
        userEntity.participantProfileEntity=ParticipantProfileEntity(request.university, request.isRegistered)
        userRepository.save(userEntity)
        return getUser(id)
    }
}