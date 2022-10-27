package com.wafflestudio.seminar.core.user.dto


import com.wafflestudio.seminar.core.user.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserSeminarEntity

data class SeminarInstructorDto(

    val seminarEntity: SeminarEntity?,

    val userEntity: UserEntity?,
    val userSeminarEntity: UserSeminarEntity?,

    ) {
    
    
}