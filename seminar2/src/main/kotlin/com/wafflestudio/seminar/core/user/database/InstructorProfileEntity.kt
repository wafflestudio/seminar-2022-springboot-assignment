package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.*
import com.wafflestudio.seminar.core.userSeminar.domain.UserInstructorSeminar
import javax.persistence.*

@Entity
@Table(name = "instructor_profile")
class InstructorProfileEntity(
    var company: String = "",
    var year: Int? = null,
) : BaseTimeEntity() {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserEntity? = null

    //==연관관계 메서드==//
    fun addUser(user: UserEntity) {
        this.user = user
        user.instructorProfile = this
    }

    //==Mapping DTO==//
    fun toDTO(): InstructorProfile {
        val seminars = ArrayList<UserInstructorSeminar>()
        for (seminar in user?.userSeminars!!) {
            if (seminar.role == Role.INSTRUCTOR && seminar.user == user)
                seminars.add(seminar.toUserInstructorSeminar())
        }
        return InstructorProfile(
            id = id,
            company = company,
            year = year,
            seminars = seminars
        )
    }
}