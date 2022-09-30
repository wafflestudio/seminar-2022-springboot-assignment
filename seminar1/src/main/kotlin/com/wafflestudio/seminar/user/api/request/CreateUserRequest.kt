package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class CreateUserRequest(
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    val nickname: String,
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    val email: String,
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 3, max = 15, message = "비밀번호는 3자 이상, 15자 이하로 입력해주세요.")
    val password : String
)