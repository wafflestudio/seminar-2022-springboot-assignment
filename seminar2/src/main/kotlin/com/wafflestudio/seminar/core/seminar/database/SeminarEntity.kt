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

    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean,
    val creatorId: Long
) : BaseTimeEntity() 