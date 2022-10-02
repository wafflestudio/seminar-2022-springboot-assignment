package com.wafflestudio.seminar.survey.database

import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["osName"])], name = "os")
class OperatingSystemEntity(
    val osName: String,
    val price: Long,
    @Column(name = "os_desc")
    val desc: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}