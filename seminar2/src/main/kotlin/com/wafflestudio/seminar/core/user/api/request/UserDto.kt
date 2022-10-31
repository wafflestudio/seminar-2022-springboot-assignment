package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.seminar.api.request.SeminarDto
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

class UserDto {

    data class SignUpRequest(
        @field:NotBlank(message = "You should write 'email'.")
        val email: String?,
        @field:NotBlank(message = "You should write 'username'.")
        val username: String?,
        @field:NotBlank(message = "You should write 'password'.")
        val password: String?,
        @field:NotBlank(message = "You should write 'role'.")
        val role: String,
        val university: String?,
        val isRegistered: Boolean?,
        val company: String?,
        @field:PositiveOrZero(message = "'year' should be positive or zero.")
        val year: Int?
    )

    data class UpdateRequest(
        val username: String?,
        val password: String?,
        val university: String?,
        val company: String?,
        @field:PositiveOrZero(message = "'year' should be positive or zero.")
        val year: Int?
    )
    


}

