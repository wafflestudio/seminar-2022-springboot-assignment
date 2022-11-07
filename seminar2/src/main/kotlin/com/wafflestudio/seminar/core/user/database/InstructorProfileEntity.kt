package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class InstructorProfileEntity(
        @Column(nullable=false)
        var company: String,
        @Column(nullable=true)
        var year: Long?,
) : BaseTimeEntity() {
}