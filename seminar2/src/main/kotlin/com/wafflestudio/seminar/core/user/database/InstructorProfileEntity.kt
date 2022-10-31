package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.api.request.UserRequest
import com.wafflestudio.seminar.core.user.domain.Instructor
import javax.persistence.*
import javax.transaction.Transactional

@Entity
@Table(name = "InstructorProfile")
data class InstructorProfileEntity(
    @OneToOne(fetch = FetchType.LAZY)
    val user: UserEntity,
    @Column(name = "company")
    var company: String = "",
    @Column(name = "year")
    var year: Int? = null,
): BaseTimeEntity() {
    
    fun toInstructor(): Instructor {
        return Instructor(
            id = id,
            company = company,
            year = year,
            instructingSeminars = emptyList(),
        )
    }

    @Transactional
    fun updateProfile(userRequest: UserRequest) {
        userRequest.company?.let {
            company = userRequest.company
        }
        userRequest.year?.let {
            year = userRequest.year
        }
    }
}