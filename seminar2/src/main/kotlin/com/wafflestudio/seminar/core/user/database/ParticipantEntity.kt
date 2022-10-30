package com.wafflestudio.seminar.core.user.database

import javax.persistence.*

@Entity
class ParticipantEntity(
    val university: String,
    val isRegistered: Boolean,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
}