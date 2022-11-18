package com.wafflestudio.seminar.core.user.api.response

import com.wafflestudio.seminar.core.user.domain.SeminarEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.domain.UserSeminarEntity
import com.wafflestudio.seminar.core.user.dto.user.GetProfileInstructorDto
import com.wafflestudio.seminar.core.user.dto.user.GetProfileParticipantDto
import com.wafflestudio.seminar.core.user.dto.user.InstructingSeminarsDto
import com.wafflestudio.seminar.core.user.dto.user.SeminarsDto
import java.time.LocalDate

data class GetProfile(
        val id: Long?,
        val username: String?,
        val email: String?,
        val lastLogin: LocalDate?,
        val dateJoined: LocalDate?,
        val participant: GetProfileParticipantDto? = null,
        val instructor: GetProfileInstructorDto? = null
) {
   companion object {
       fun of(
               userEntity: UserEntity,
               seminars: List<SeminarsDto>?,
               instructingSeminars: List<InstructingSeminarsDto>?
       ): GetProfile{
            return userEntity.run { 
               GetProfile(
                       id = id,
                       username = username,
                       email = email,
                       lastLogin = lastLogin,
                       dateJoined = dateJoined,
                       participant = if(participant?.id != null) GetProfileParticipantDto.of(participant!!, seminars) else null,
                       instructor = if(instructor?.id != null) GetProfileInstructorDto.of(instructor!!,instructingSeminars) else null
               )
           }
       }
   }
}