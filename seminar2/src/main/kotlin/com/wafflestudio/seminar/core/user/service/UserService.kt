package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.ErrorCode
import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.core.UserSeminar.repository.UserSeminarRepository
import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.domain.UserDTO
import com.wafflestudio.seminar.core.user.domain.enums.RoleType
import com.wafflestudio.seminar.core.user.domain.enums.RoleType.*
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.domain.profile.InstructorDTO
import com.wafflestudio.seminar.core.user.repository.UserRepository
import com.wafflestudio.seminar.core.user.domain.profile.InstructorProfile
import com.wafflestudio.seminar.core.user.domain.profile.ParticipantDTO
import com.wafflestudio.seminar.core.user.domain.profile.ParticipantProfile
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.transaction.annotation.Transactional


interface UserService {
    fun signUp(request: SignUpRequest): AuthToken
    fun logIn(email: String, password: String): AuthToken
    fun getMe(userId: Long): UserDTO
    
    fun getUserProfile(userId: Long, targetUserId: Long): UserDTO
    fun editProfile(userId: Long, request: EditProfileRequest): UserDTO
    
    fun addParticipantRole(userId: Long, request: ParticipantRoleRequest): UserDTO
}


@Service
class UserServiceImpl(
    private val authConfig: AuthConfig,
    private val authTokenService: AuthTokenService,
    private val userRepository: UserRepository,
    private val userSeminarRepository: UserSeminarRepository
): UserService {
    private val encoder = authConfig.passwordEncoder()
    
    override fun signUp(request: SignUpRequest): AuthToken {
        return with(request) {
            if(userRepository.existsByEmail(this.email!!)){
                throw SeminarException(ErrorCode.EMAIL_CONFLICT)
            }
            
            val role = RoleType.valueOf(this.role!!)
            val newUser = UserEntity(this.email, this.username!!, encoder.encode(this.password), role)
            when (role) {
                PARTICIPANT -> newUser.participantProfile =
                        ParticipantProfile(this.university?:"", this.isRegistered?:true, newUser)
                INSTRUCTOR -> newUser.instructorProfile =
                        InstructorProfile(this.company?:"", this.year, newUser)
            }
            userRepository.save(newUser)
            
            // return value
            authTokenService.generateTokenByEmail(this.email)
        }
    }
    
    override fun logIn(email: String, password: String): AuthToken {
        val userEntity = userRepository.findByEmail(email)?:
            throw SeminarException(ErrorCode.EMAIL_NOT_FOUND)

        if(!encoder.matches(password, userEntity.password)){
            throw SeminarException(ErrorCode.NOT_AUTHORIZED)
        } else {
            // 로그인 성공 - 마지막 로그인 시각 업데이트
            userEntity.lastLogin = LocalDateTime.parse(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
            userRepository.save(userEntity)
            return authTokenService.generateTokenByEmail(email)
        }
    }
    
    
    private fun UserEntity.toDTO(): UserDTO {
        val user = userRepository.getUserProfile(id)
        
        val participantProfile = user.participantProfile?.run {
            ParticipantDTO(
                this.id, this.university, this.isRegistered,
                userSeminarRepository.findParticipants(id)
            )
        }
        
        val instructorProfile = user.instructorProfile?.run {
            InstructorDTO(
                this.id, this.company, this.year,
                userSeminarRepository.findInstructors(id)
            )
        }
        
        return UserDTO.of(user, participantProfile, instructorProfile)
    }
    
    
    
    
    override fun getMe(userId: Long): UserDTO
        = userRepository.findByIdOrNull(userId)!!.toDTO()
    
    
    override fun getUserProfile(userId: Long, targetUserId: Long): UserDTO {
        val user = userRepository.findByIdOrNull(targetUserId)?:
            throw SeminarException(ErrorCode.USER_NOT_FOUND)
        return user.toDTO()
    }
    
    
    override fun editProfile(userId: Long, request: EditProfileRequest): UserDTO {
        var targetUser = userRepository.findById(userId).get()
        
        // (유형에 맞게) 수정을 요청한 필드에 대해 수정 작업
        with (request) {
            if(this.username != null) targetUser.username = this.username
            if(this.password != null) targetUser.password = this.password
            
            // user의 role field는 변경할 수 없는 거로 보는 게 맞을 듯
            // 유저가 세미나 진행자일 수도 있고 참여자일 수도 있다는 점에서 해당 profile이 존재하는지 여부로 살펴보기
            if(targetUser.participantProfile != null){
                if(this.isRegistered != null)
                    throw SeminarException(ErrorCode.EDIT_REGISTRATION_FORBIDDEN)
                if(this.university != null)
                    targetUser.participantProfile?.university = this.university.trim()
            }
            if(targetUser.instructorProfile != null){
                if(this.company != null)
                    targetUser.instructorProfile!!.company = this.company.trim()
                if(this.year != null)
                    targetUser.instructorProfile!!.year = this.year
            }
        }
        val user = userRepository.save(targetUser)
        return user.toDTO()
    }


    @Transactional
    override fun addParticipantRole(userId: Long, request: ParticipantRoleRequest): UserDTO {
        var targetUser = userRepository.findById(userId).get()
        
        // 참여자 등록을 했다면, 참여자 프로필은 더이상 null 상태일 수 없음
        if(targetUser.participantProfile != null) 
            throw SeminarException(ErrorCode.ALREADY_PARTICIPANT)
            
        targetUser.participantProfile =
            ParticipantProfile(request.university?:"", request.isRegistered?:true, targetUser)
        val user = userRepository.save(targetUser)
        return user.toDTO()
    }
}