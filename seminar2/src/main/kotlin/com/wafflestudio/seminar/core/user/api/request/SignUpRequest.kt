package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.UserRole
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

data class SignUpRequest(
    @field: NotBlank
    @field: Email
    val email: String,

    @field: NotBlank
    val username: String,

    @field: NotBlank
    val password: String,

    @field: NotNull
    val role: UserRole,

    // case: participant
    val university: String? = null,
    val isRegistered: Boolean = true,

    // case: instructor
    val company: String? = null,
    @field: PositiveOrZero
    val year: Int? = null,
) {

    fun toUserEntity(passwordEncoder: PasswordEncoder): UserEntity = UserEntity(
        email,
        username,
        passwordEncoder.encode(password),
    )
    
    fun toParticipantProfileEntity(): ParticipantProfileEntity = ParticipantProfileEntity(
        university,
        isRegistered,
    )
    
    fun toInstructorProfileEntry(): InstructorProfileEntity = InstructorProfileEntity(
        company,
        year,
    )
    
}