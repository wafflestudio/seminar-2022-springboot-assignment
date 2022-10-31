package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.domain.InstructorProfile
import javax.persistence.*

@Entity
@Table(name = "instructorProfile")
class InstructorProfileEntity(
    @OneToOne(mappedBy = "instructorProfile", cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: UserEntity? = null,
    var company: String,
    var year: Int?,
) : BaseTimeEntity() {
    fun toInstructorProfile(): InstructorProfile {
        return InstructorProfile(
            id = id,
            company = company,
            year = year
        )
    }
}