package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name="seminars")
class SeminarEntity (
    val name : String,
    val capacity : Int,
    val count : Int,
    val online : Boolean,
    val instructors : UserSeminar,
    val participants : List<UserSeminar>
        ) : BaseTimeEntity() {
}