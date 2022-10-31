package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.Instructor
import javax.persistence.*

@Entity
@Table(name = "InstructorProfile")
data class InstructorProfileEntity(
    @OneToOne(fetch = FetchType.LAZY)
    val user: UserEntity,
    @Column(name = "company")
    val company: String = "",
    @Column(name = "year")
    val year: Int? = null,
): BaseTimeEntity() {
    
    fun toInstructor(): Instructor {
        return Instructor(
            id = id,
            company = company,
            year = year,
            instructingSeminars = emptyList(),
        )
    }
}