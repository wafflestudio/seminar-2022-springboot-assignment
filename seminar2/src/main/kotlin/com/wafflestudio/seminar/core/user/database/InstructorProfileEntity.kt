package com.wafflestudio.seminar.core.user.database

import javax.persistence.*

@Entity
@Table(name="InstructorProfile")
class InstructorProfileEntity(
    
    @Column
    var emailInstructor: String,
    
    @Column
    var company: String = "",
    
    @Column
    var year: Int? = null,
    
   
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}