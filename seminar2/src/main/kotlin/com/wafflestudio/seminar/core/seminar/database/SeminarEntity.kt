package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.api.response.SeminarResponse
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class SeminarEntity(
        @Column(nullable=false)
        var name: String,
        @Column(nullable=false)
        var capacity: Long,
        @Column(nullable=false)
        var count: Long,
        @Column(nullable=false)
        var time: String,
        @Column(nullable=false)
        var online: Boolean,
) : BaseTimeEntity() {
}