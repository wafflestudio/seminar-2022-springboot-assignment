package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name="UserSeminar")
class UserSeminar (
    @ManyToOne(cascade = [CascadeType.REMOVE])
    val user : UserEntity,
    val seminar : SeminarEntity
    ) : BaseTimeEntity() {
}