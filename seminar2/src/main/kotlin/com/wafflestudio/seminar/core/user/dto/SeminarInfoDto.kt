package com.wafflestudio.seminar.core.user.dto


import com.wafflestudio.seminar.core.user.domain.SeminarEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.domain.UserSeminarEntity

data class SeminarInfoDto(

    val seminarEntity: SeminarEntity?,

    val userSeminarEntity: UserSeminarEntity?,
    val userEntity: UserEntity?,

    ) {
    
    
}