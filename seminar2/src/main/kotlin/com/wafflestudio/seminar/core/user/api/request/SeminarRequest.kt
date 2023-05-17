package com.wafflestudio.seminar.core.user.api.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class SeminarRequest(

        @field:NotBlank(message = "이름을 입력하지 않았습니다")
        val name: String?,

        @field:NotNull(message = "수량을 입력하지 않았습니다")
        @field:Positive(message = "수량은 양수를 입력해야 합니다")
        val capacity: Int?,

        @field:NotNull(message = "참가자 수를 입력하지 않았습니다")
        @field:Positive(message = "참가자 수는 양수를 입력해야 합니다")
        val count: Int?,
        
        @field:NotNull(message = "강좌 수업 시간을 입력해야 합니다")
        val time: String?,
        
        val online: Boolean? = true
)