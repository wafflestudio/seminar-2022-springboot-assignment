package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
class InstructorProfileEntity(
    private val company: String = "",
    private val year: Int? = null,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private val user: UserEntity
) : BaseTimeEntity() {

}