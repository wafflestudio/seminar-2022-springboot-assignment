package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import java.time.LocalTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "seminar")
class SeminarEntity(
    @OneToMany(mappedBy = "seminar", cascade = [CascadeType.ALL])
    val userSeminars: MutableSet<UserSeminarEntity> = mutableSetOf(),

    var name: String,
    var capacity: Int,
    var count: Int,
    var time: LocalTime,
    var online: Boolean,
    val creatorId: Long
) : BaseTimeEntity() 