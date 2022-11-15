package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.mappingTable.InstructorProfile
import com.wafflestudio.seminar.core.mappingTable.ParticipantProfile
import com.wafflestudio.seminar.core.user.repository.UserEntity
import com.wafflestudio.seminar.exception.Seminar400
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class SignUpRequest(
    @field: NotBlank(message = "email은 필수값입니다.")
    val email: String,
    @field: NotBlank(message = "username은 필수값입니다.")
    val username: String,
    @field: NotBlank(message = "password는 필수값입니다.")
    val password: String,
    @field: Pattern(regexp = "Instructor|Participant", message = "올바른 role값을 입력해주세요.")
    val role: String,
    val university: String = "",
    val isRegistered: Boolean = true,
    val company: String = "",
    val year: Int? = null,
){

    fun toParticipantUserEntity(request: SignUpRequest, passwordEncoder: PasswordEncoder): UserEntity {
        return UserEntity(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            participant = ParticipantProfile(
                request.university,
                isRegistered = request.isRegistered
            )
        )
    }

    fun toInstructorUserEntity(request: SignUpRequest, passwordEncoder: PasswordEncoder): UserEntity {
        validateYear(request.year)
        return UserEntity(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            instructor = InstructorProfile(
                company = request.company,
                years = request.year
            )
        )
    }

    private fun validateYear(year: Int?) {
        if (year != null && year <= 0)
            throw Seminar400("year 값은 0보다 큰 수여야 합니다.")
    }

}