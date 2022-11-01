package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "instructor_profile")
class InstructorEntity(
    @OneToOne(fetch = FetchType.LAZY)
    val user: UserEntity,
    var company: String,
    @Column(name = "instructing_year")
    var year: Long?,
) : BaseTimeEntity() {
    fun update(company: String, year: Long?) {
        this.company = company
        this.year = year
    }
}