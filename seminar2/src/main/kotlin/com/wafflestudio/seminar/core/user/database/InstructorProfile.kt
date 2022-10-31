package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminar
import com.wafflestudio.seminar.core.user.domain.userResponse.InstructorProfileResponse
import com.wafflestudio.seminar.core.user.domain.userResponse.InstructorProfileSeminarResponse
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name="instructors")
class InstructorProfile (
    val company : String,
    val year : Int?,
    val seminars : List<UserSeminar>
        ) : BaseTimeEntity() {
            
            fun toInstructorProfileResponse() : InstructorProfileResponse {
                val retSeminar : List<InstructorProfileSeminarResponse> = listOf()
                return InstructorProfileResponse(
                    id = id,
                    company = company,
                    year = year,
                    instructingSeminars = retSeminar
                )
            }
}