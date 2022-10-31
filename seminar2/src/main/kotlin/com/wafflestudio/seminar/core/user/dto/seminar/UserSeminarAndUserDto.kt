package com.wafflestudio.seminar.core.user.dto.seminar

import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.domain.UserSeminarEntity

data class UserSeminarAndUserDto(
    val userSeminarEntity: UserSeminarEntity?,
    val userEntity: UserEntity?,
) {
}