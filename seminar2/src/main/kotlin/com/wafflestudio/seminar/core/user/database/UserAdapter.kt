package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.domain.UserPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserAdapter(
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val passwordEncoder: PasswordEncoder
) : UserPort {
    override fun createUser(signUpRequest: SignUpRequest) = signUpRequest.run {
        val encodedPassword = passwordEncoder.encode(signUpRequest.password)
        userRepository.findByEmail(email!!)?.let { throw Seminar409("${email}: 중복된 이메일입니다.") }
        try {
            User.Role.valueOf(role!!)
        } catch (e: IllegalArgumentException) {
            throw Seminar400("${role}: 잘못된 수강생/진행자 형식입니다.")
        }
        val userEntity = UserEntity(
            email = email,
            username = username!!,
            encodedPassword = encodedPassword,
            userSeminars = null,
            participantProfile = null,
            instructorProfile = null
        )

        if (User.Role.valueOf(role) == User.Role.PARTICIPANT) {
            participantProfileRepository.save(
                ParticipantProfileEntity(
                    user = userEntity,
                    university = university!!,
                    isRegistered = isRegistered!!,
                )
            )
        } else if (User.Role.valueOf(role) == User.Role.INSTRUCTOR) {
            instructorProfileRepository.save(
                InstructorProfileEntity(
                    user = userEntity,
                    company = company!!,
                    year = year
                )
            )
        }

        userRepository.save(
            userEntity
        ).toUser()
    }


    override fun getUser(signInRequest: SignInRequest): User {
        val email = signInRequest.email
        val password = signInRequest.password
        val userEntity = userRepository.findByEmail(email) ?: throw Seminar404("해당 이메일(${email})로 등록된 사용자가 없어요.")

        return if (passwordEncoder.matches(
                password,
                userEntity.encodedPassword
            )
        ) userEntity.toUser() else throw Seminar401("비밀번호가 잘못되었습니다.")
    }

    override fun getUserIdByEmail(email: String): Long {
        val userEntity = userRepository.findByEmail(email) ?: throw Seminar404("해당 이메일(${email})로 등록된 사용자가 없어요.")
        return userEntity.id
    }
}