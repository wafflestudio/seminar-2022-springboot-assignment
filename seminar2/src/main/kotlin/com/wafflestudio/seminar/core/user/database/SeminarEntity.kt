package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class SeminarEntity(
    val name: String
) : BaseTimeEntity() {
    @OneToMany(mappedBy = "seminar", cascade = [CascadeType.REMOVE])
    val userSeminars: List<UserSeminarEntity> = ArrayList()
}