package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.dto.auth.InstructorProfileDto
import com.wafflestudio.seminar.core.user.dto.auth.ParticipantProfileDto
import javax.validation.constraints.NotBlank

data class SignUpRequest(

        @field:NotBlank(message = "해당 정보가 없습니다")
        val email: String,

        @field:NotBlank(message = "해당 정보가 없습니다")
        val username: String,

        @field:NotBlank(message = "해당 정보가 없습니다")
        var password: String,
        
        val role: String,

        val participant: ParticipantProfileDto?,
        val instructor: InstructorProfileDto?
) {
        
}