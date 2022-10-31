package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.seminar.domain.InstructingSeminar
import com.wafflestudio.seminar.core.seminar.domain.ParticipatingSeminar
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserAdapter(
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val passwordEncoder: PasswordEncoder
) : UserPort {
    override fun createUser(signUpRequest: SignUpRequest) = signUpRequest.run {
        checkDuplicatedEmail(email!!)
        val encodedPassword = passwordEncoder.encode(signUpRequest.password)
        val userEntity = UserEntity(
            email = email,
            username = username!!,
            encodedPassword = encodedPassword,
            lastLogin = LocalDateTime.now(),
            participantProfile = null,
            instructorProfile = null,
        )

        if (User.Role.valueOf(role!!) == User.Role.PARTICIPANT) {
            val participantProfileEntity = ParticipantProfileEntity(
                user = userEntity,
                university = university!!,
                isRegistered = isRegistered!!,
            )
            userEntity.participantProfile = participantProfileEntity
            participantProfileRepository.save(participantProfileEntity)
        } else if (User.Role.valueOf(role) == User.Role.INSTRUCTOR) {
            val instructorProfileEntity = InstructorProfileEntity(
                user = userEntity,
                company = company!!,
                year = year
            )
            userEntity.instructorProfile = instructorProfileEntity
            instructorProfileRepository.save(instructorProfileEntity)
        }
        userRepository.save(userEntity)
    }


    override fun getUser(signInRequest: SignInRequest) = signInRequest.run {
        val userEntity = userRepository.findByEmail(email) ?: throw Seminar404("해당 이메일(${email})로 등록된 사용자가 없어요.")
        if (!passwordEncoder.matches(password, userEntity.encodedPassword)) {
            throw Seminar401("비밀번호가 잘못되었습니다.")
        }
        userEntity.lastLogin = LocalDateTime.now()
        userRepository.save(userEntity)
    }

    override fun getUserIdByEmail(email: String): Long {
        val userEntity = userRepository.findByEmail(email) ?: throw Seminar404("해당 이메일(${email})로 등록된 사용자가 없어요.")
        return userEntity.id
    }

    override fun getProfile(userId: Long): ProfileResponse {
        val userEntity = userRepository.findByIdOrNull(userId) ?: throw Seminar404("해당 아이디(${userId})로 등록된 사용자가 없어요.")
        val participatingSeminars: MutableList<ParticipatingSeminar> = mutableListOf()
        var instructingSeminar: InstructingSeminar? = null
        userEntity.userSeminars.forEach {
            it.seminar.run {
                if (it.role == User.Role.PARTICIPANT) participatingSeminars.add(
                    ParticipatingSeminar(
                        id = id,
                        name = name,
                        joinedAt = it.joinedAt,
                        isActive = it.isActive,
                        droppedAt = it.droppedAt
                    )
                ) else instructingSeminar = InstructingSeminar(
                    id = id,
                    name = name,
                    joinedAt = it.joinedAt
                )
            }
        }
        return userEntity.run {
            ProfileResponse(
                id = id,
                username = username,
                email = email,
                lastLogin = lastLogin,
                dateJoined = createdAt!!,
                participant = if (participantProfile != null) ParticipantProfile(
                    id = participantProfile!!.id,
                    university = participantProfile!!.university,
                    isRegistered = participantProfile!!.isRegistered,
                    seminars = participatingSeminars
                ) else null,
                instructor = if (instructorProfile != null) InstructorProfile(
                    id = instructorProfile!!.id,
                    company = instructorProfile!!.company,
                    year = instructorProfile!!.year,
                    instructingSeminars = instructingSeminar
                ) else null
            )
        }
    }

    //    override fun editProfile(userId: Long, editProfileRequest: EditProfileRequest) = editProfileRequest.run {
//        val userEntity = userRepository.findByIdOrNull(userId) ?: throw Seminar404("해당 아이디(${userId})로 등록된 사용자가 없어요.")
//        username?.run { userEntity.username = this }
//        if (university != null && userEntity.participantProfile != null) {
//            userEntity.participantProfile?.university = university
//        }
//        if (userEntity.instructorProfile != null) {
//            userEntity.instructorProfile?.company = company
//            userEntity.instructorProfile?.year = year
//        }
//        userRepository.save(userEntity).toUser()
//    }
//
//    override fun registerParticipant(userId: Long, registerParticipantRequest: RegisterParticipantRequest) =
//        registerParticipantRequest.run {
//            val userEntity =
//                userRepository.findByIdOrNull(userId) ?: throw Seminar404("해당 아이디(${userId})로 등록된 사용자가 없어요.")
//            if (userEntity.participantProfile != null) {
//                throw Seminar409("이미 수강생 신분입니다.")
//            }
//            userEntity.participantProfile = ParticipantProfileEntity(userEntity, university, isRegistered)
//            userRepository.save(userEntity).toUser()
//        }
    fun checkDuplicatedEmail(email: String) {
        userRepository.findByEmail(email)?.let { throw Seminar409("${email}: 중복된 이메일입니다.") }
    }
}