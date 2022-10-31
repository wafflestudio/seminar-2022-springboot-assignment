package com.wafflestudio.seminar.core.seminar.domain

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.UserSeminar.domain.UserSeminarEntity
import javax.persistence.*

@Entity
@Table(name="seminars")
class SeminarEntity (
    @Column(nullable=false)
    var name: String,
    var instructor: String,
    var capacity: Long,
    var count: Long,
    var time: String,
    var online: Boolean,
    @JsonManagedReference
    @OneToMany(
        mappedBy = "seminar",
        fetch = FetchType.LAZY,
        cascade = arrayOf(CascadeType.REMOVE, CascadeType.PERSIST)
    )
    var userSeminarList: MutableList<UserSeminarEntity>?= null
) : BaseTimeEntity()