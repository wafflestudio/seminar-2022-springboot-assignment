package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class SeminarEntity : BaseTimeEntity() {
    @OneToMany(mappedBy = "seminar", cascade = [CascadeType.REMOVE])
    private val userSeminars: List<UserSeminarEntity> = ArrayList()
}