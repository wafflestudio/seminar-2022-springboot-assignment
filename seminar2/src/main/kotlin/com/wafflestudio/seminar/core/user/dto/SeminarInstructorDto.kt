package com.wafflestudio.seminar.core.user.dto


import com.wafflestudio.seminar.core.user.domain.SeminarEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.domain.UserSeminarEntity

data class SeminarInstructorDto(

    val seminarEntity: SeminarEntity?,

    val userEntity: UserEntity?,
    val userSeminarEntity: UserSeminarEntity?,

    ) {
    
    
}