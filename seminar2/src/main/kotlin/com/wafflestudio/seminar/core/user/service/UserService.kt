package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.profile.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.profile.database.InstructorProfileRepository
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.api.request.RegisterParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.request.UpdateUserRequest
import com.wafflestudio.seminar.core.user.api.response.*
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.type.UserRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional


@Service
class UserService(
    private val userRepository: UserRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authTokenService: AuthTokenService
) {
    
    @Transactional
    fun signUp(request: SignUpRequest) : AuthToken {
        validateSignUpRequest(request)
        val now = LocalDateTime.now()
        when (request.role) {
            UserRole.PARTICIPANT -> {
                val newUser = UserEntity(
                    loginedAt = now,
                    username = request.username,
                    email = request.email,
                    password = passwordEncoder.encode(request.password),
                    role = UserRole.PARTICIPANT,
                    university = request.university,
                    isRegistered = request.isRegistered
                )
                userRepository.save(newUser)
                participantProfileRepository.save(
                    ParticipantProfileEntity(
                        user = newUser
                    )
                )
            }
            UserRole.INSTRUCTOR -> {
                val newUser = UserEntity(
                    loginedAt = now,
                    username = request.username,
                    email = request.email,
                    password = passwordEncoder.encode(request.password),
                    role = UserRole.INSTRUCTOR,
                    isRegistered = request.isRegistered,
                    company = request.company,
                    year = request.year
                )
                userRepository.save(newUser)
                instructorProfileRepository.save(
                    InstructorProfileEntity(
                        user = newUser
                    )
                )
            }
            UserRole.BOTH -> {
                val newUser = UserEntity(
                    loginedAt = now,
                    username = request.username,
                    email = request.email,
                    password = passwordEncoder.encode(request.password),
                    role = UserRole.INSTRUCTOR,
                    university = request.university,
                    isRegistered = request.isRegistered,
                    company = request.company,
                    year = request.year
                )
                userRepository.save(newUser)
                instructorProfileRepository.save(
                    InstructorProfileEntity(
                        user = newUser
                    )
                )
                participantProfileRepository.save(
                    ParticipantProfileEntity(
                        user = newUser
                    )
                )
            }
        }
        
        return authTokenService.generateTokenByEmail(request.email)
    }
    
    @Transactional
    fun signIn(request: SignInRequest) : AuthToken {
        val user: UserEntity = validateSignInRequest(request)
        user.loginedAt = LocalDateTime.now()
        userRepository.save(user)
        return authTokenService.generateTokenByEmail(request.email)
    }

    fun getUser(id: Long) : UserProfile {
        val user = userRepository.findById(id)
        if (user.isPresent) {
            return createUserProfileFromUser(user.get())
        } else {
            throw Seminar404("해당 user id를 찾을 수 없습니다 - status 404")
        }
    }
    
    fun getAllUsers(page: Int, size: Int) : Page<UserProfile> {
        return userRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending())).map { 
            createUserProfileFromUser(it)
        }
    } 

    fun createUserProfileFromUser(user: UserEntity) : UserProfile {
        return when (user.role) {
            UserRole.PARTICIPANT -> {
                UserProfile.from(
                    user,
                    ParticipantProfile.from(
                        participantProfileRepository.findByUserId(user.id)!!,
                        getSeminarProfiles(user)
                    )
                )
            }
            UserRole.INSTRUCTOR -> {
                UserProfile.from(
                    user,
                    InstructorProfile.from(
                        instructorProfileRepository.findByUserId(user.id)!!,
                        getSeminarProfileForInstructor(user)
                    )
                )
            }
            UserRole.BOTH -> {
                UserProfile.from(
                    user,
                    ParticipantProfile.from(
                        participantProfileRepository.findByUserId(user.id)!!,
                        getSeminarProfiles(user)
                    ),
                    InstructorProfile.from(
                        instructorProfileRepository.findByUserId(user.id)!!,
                        getSeminarProfileForInstructor(user)
                    )
                    
                )
            }
        }
    }
    
    fun getSeminarProfiles(user: UserEntity) : MutableList<SeminarProfile> {
        val userSeminars = userSeminarRepository.findAllByUserId(user.id)
        val seminarProfiles = mutableListOf<SeminarProfile>()
        userSeminars.forEach {
            if (it.isParticipant) {
                seminarProfiles.add(
                    SeminarProfile(
                        id = it.seminar!!.id,
                        name = it.seminar!!.name,
                        joinedAt = it.createdAt,
                        isActive = it.isActive,
                        droppedAt = it.droppedAt
                    )
                )
            }
        }
        return seminarProfiles
    }
    
    fun getSeminarProfileForInstructor(user: UserEntity) : SeminarProfileForInstructor? {
        val userSeminars = userSeminarRepository.findAllByUserId(user.id)
        var instructingSeminar: SeminarProfileForInstructor? = null
        
        userSeminars.firstOrNull { !it.isParticipant }?.let {
            instructingSeminar = SeminarProfileForInstructor(
                id = it.seminar!!.id,
                name = it.seminar!!.name,
                joinedAt = it.createdAt
            )
        }
        return instructingSeminar
    }
    
    fun createUserProfileFromUserAndParticipationProfile(
        user: UserEntity, participantProfile: ParticipantProfile
    ) : UserProfile {
        return when (user.role) {
            UserRole.PARTICIPANT -> {
                UserProfile.from(
                    user, participantProfile
                )
            }
            UserRole.BOTH -> {
                UserProfile.from(
                    user,
                    participantProfile,
                    InstructorProfile.from(
                        instructorProfileRepository.findByUserId(user.id)!!,
                        getSeminarProfileForInstructor(user)
                    )
                )
            }
            else -> throw Seminar500("잘못된 함수 호출입니다")
        }
    }
    
    
    @Transactional
    fun updateUser(user: UserEntity, request: UpdateUserRequest) : UserProfile {
        // TODO user table 을 업데이트 하면 PROFILE table의 modified_at도 업데이트 해야되나?
        request.username?.let { 
            user.username = request.username
        }
        return when (user.role) {
            UserRole.PARTICIPANT -> {
                user.university = request.university
                userRepository.save(user)
                createUserProfileFromUser(user)
            }
            UserRole.INSTRUCTOR -> {
                validateYearPositive(request.year)
                user.company = request.company
                user.year = request.year
                userRepository.save(user)
                createUserProfileFromUser(user)
            }
            else -> {
                validateYearPositive(request.year)
                user.university = request.university
                user.company = request.company
                user.year = request.year
                userRepository.save(user)
                createUserProfileFromUser(user)
            }
        }
    }
    
    @Transactional
    fun registerParticipantForInstructor(user: UserEntity, request: RegisterParticipantRequest) : UserProfile {
        if (user.role == UserRole.PARTICIPANT || user.role == UserRole.BOTH) {
            throw Seminar409("이미 참여자입니다")
        }
        user.role = UserRole.BOTH
        user.university = request.university
        user.isRegistered = request.isRegistered
        userRepository.save(user)
        val participantProfileEntity = participantProfileRepository.save(
            ParticipantProfileEntity(
                user = user
            )
        )
        return createUserProfileFromUserAndParticipationProfile(
            user,
            ParticipantProfile.from(
                participantProfileEntity,
                getSeminarProfiles(user)
            )
        )
    }
    
    @Transactional
    fun deleteUser(user: UserEntity) {
        when (user.role) {
            UserRole.PARTICIPANT -> {
                participantProfileRepository.deleteByUserId(user.id)        
            }
            UserRole.INSTRUCTOR -> {
                instructorProfileRepository.deleteByUserId(user.id)
            }
            UserRole.BOTH -> {
                participantProfileRepository.deleteByUserId(user.id)
                instructorProfileRepository.deleteByUserId(user.id)
            }
        }
        userRepository.delete(user)
    }
    
    fun validateYearPositive(year: Int?) {
        if (year != null) {
            if (year < 0) {
                throw Seminar400("year은 0 혹은 양수여야 합니다")
            }
        }
    }
    
    fun validateSignUpRequest(request: SignUpRequest) {
        validateYearPositive(request.year)
        val userEntityByEmail: UserEntity? = userRepository.findByEmail(request.email)
        if (userEntityByEmail != null) {
            throw Seminar409("중복된 email 주소입니다. 다른 email을 시도해주세요")
        }
    }
    
    fun validateSignInRequest(request: SignInRequest) : UserEntity {
        val userEntity: UserEntity = userRepository.findByEmail(request.email) ?: throw Seminar404("해당 user email을 찾을 수 없습니다")

        if (!passwordEncoder.matches(request.password, userEntity.password)) {
            throw Seminar401("패스워드가 일치하지 않습니다")
        }
        return userEntity
    }
}