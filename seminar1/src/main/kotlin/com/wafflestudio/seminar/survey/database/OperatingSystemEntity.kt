package com.wafflestudio.seminar.survey.database

import javax.persistence.*

@Entity(name = "operating_system")
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["osName"])])
class OperatingSystemEntity(
    val osName: String,
    val price: Long,
    val des: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}