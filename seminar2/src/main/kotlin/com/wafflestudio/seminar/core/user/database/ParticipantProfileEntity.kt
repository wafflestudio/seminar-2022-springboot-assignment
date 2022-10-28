package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "ParticipantProfile")
class ParticipantProfileEntity(
    @Column(name = "university")
    var university: String? = "",

    @Column(name = "is_registered")
    var isRegistered: Boolean? = true,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
