//package com.wafflestudio.seminar.core.user.database
//
//import javax.persistence.*
//
//@Entity
//@Table(name="InstructorProfile")
//class NewInstructorProfileEntity1(
//    
//  
//    @Column
//    var company: String = "",
//    
//    @Column
//    var year: Int? = null,
//
//    @OneToOne
//    @JoinColumn
//    var seminarEntity: SeminarEntity?
//) {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    val id: Long = 0L
//}