package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminar
import com.wafflestudio.seminar.core.user.api.response.InstructorProfileResponse
import com.wafflestudio.seminar.core.user.api.response.InstructorProfileSeminarResponse
import com.wafflestudio.seminar.core.user.api.response.ParticipantProfileSeminarResponse
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name="instructor_profile")
class InstructorProfile (
    var company : String,
    var year : Int?,
        ) : BaseTimeEntity() {
            
            fun toInstructorProfileResponse(userSeminar: UserSeminar) : InstructorProfileResponse {
                return InstructorProfileResponse(
                    id = id,
                    company = company,
                    year = year,
                    instructingSeminar = userSeminar.toInstructorProfileSeminarResponse()
                )
            }
}