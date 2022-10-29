package com.wafflestudio.seminar.survey.database

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity(name = "operating_system")
class OperatingSystemEntity(
    val osName: String,
    val price: Long,
    val desc: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}