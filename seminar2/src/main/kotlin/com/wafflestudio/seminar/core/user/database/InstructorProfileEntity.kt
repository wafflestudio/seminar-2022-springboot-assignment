package com.wafflestudio.seminar.core.user.database

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator
import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.api.request.UserRequest
import com.wafflestudio.seminar.core.user.domain.Instructor
import com.wafflestudio.seminar.core.user.domain.User
import javax.persistence.*
import javax.transaction.Transactional

@Entity
@Table(name = "InstructorProfile")
@JsonIdentityInfo(generator = IntSequenceGenerator::class, property = "id")
data class InstructorProfileEntity(
    @OneToOne(fetch = FetchType.LAZY)
    val user: UserEntity,
    @Column(name = "company")
    var company: String = "",
    @Column(name = "year")
    var year: Int? = null,
) : BaseTimeEntity() {

    fun toInstructor(): Instructor {
        return Instructor(
            id = id,
            company = company,
            year = year,
            instructingSeminars = user.seminars.filter { it.role == User.Role.INSTRUCTOR }
                .map { it.toInstructingSeminar() }
                .toMutableSet(),
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

    override fun hashCode() = id.hashCode()
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}