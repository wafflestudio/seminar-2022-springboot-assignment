package com.wafflestudio.seminar.core.user.database

import javax.persistence.*

@Entity
@Table(name = "InstructorProfile")
class InstructorProfileEntity(
    @Column(name = "company")
    var company: String? = "",

    @Column(name = "`year`")
    var year: Int? = null,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}