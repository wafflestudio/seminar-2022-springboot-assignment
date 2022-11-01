package com.wafflestudio.seminar.core.user.database

import org.springframework.lang.Nullable
import javax.persistence.*

@Entity
class InstructorEntity(
    val company: String,
    @Nullable
    val year: Int?,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
}