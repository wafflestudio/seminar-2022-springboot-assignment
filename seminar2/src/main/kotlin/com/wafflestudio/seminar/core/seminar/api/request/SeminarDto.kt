package com.wafflestudio.seminar.core.seminar.api.request

import com.querydsl.core.annotations.QueryProjection
import com.wafflestudio.seminar.core.user.api.request.UserDto
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

class SeminarDto {
    

    data class SeminarResponse @QueryProjection constructor(
        val id: Long,
        val name: String,
        val joinedAt: LocalDateTime,
        val isActive: Boolean,
        val droppedAt: LocalDateTime?
    )

    data class InstructingSeminarResponse @QueryProjection constructor(
        val id: Long,
        val name: String,
        val joinedAt: LocalDateTime
    )
    
}

