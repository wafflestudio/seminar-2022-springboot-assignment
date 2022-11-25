package com.wafflestudio.seminar.core.seminar.api.request

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

data class SeminarMakeRequest(
    @field: NotNull(message = "세미나 이름을 입력해주세요.")
    @field: Size(min = 1, message = "세미나 이름은 한 글자 이상이어야 합니다.")
    val name: String,
    
    @field: NotNull(message = "세미나 정원을 입력해주세요.")
    @field: Positive(message = "정원은 양의 정수값이어야 합니다.")
    val capacity: Int,
    
    @field: NotNull(message = "세미나 횟수를 입력해주세요.")
    @field: Positive(message = "세미나 횟수는 양의 정수값이어야 합니다.")
    val count: Int,
    
    @field: NotNull(message = "정기 세미나 시간을 입력해주세요.")
    @field: Pattern(
        regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]\$",
        message = "정기 세미나 시간은 HH:MM형식으로 입력해주세요."
    )
    val time: String,
    val online: Boolean = true
)