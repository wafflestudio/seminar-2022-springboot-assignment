package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "seminars")
class SeminarEntity(
    @OneToMany(mappedBy = "seminar")
    val userSeminars: Set<UserSeminarEntity>,

    val seminarName: String
) : BaseTimeEntity() 