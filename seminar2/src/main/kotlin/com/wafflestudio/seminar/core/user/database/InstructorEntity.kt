package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.domain.SeminarInstructor
import com.wafflestudio.seminar.core.user.domain.Instructor
import javax.persistence.*

@Entity
class InstructorEntity(
    var company: String,
    var year: Int?,
): BaseTimeEntity() {
    fun toInstructor(instructingSeminars: List<SeminarInstructor>): Instructor {
        return Instructor(
            company = company,
            year = year,
            instructingSeminars = instructingSeminars,
        )
    }
}