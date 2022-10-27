//package com.wafflestudio.seminar.core.user.database
//
//import javax.persistence.*
//
//@Entity
//@Table(name="ParticipantProfile")
//class NewParticipantProfileEntity1(
//
//
//    @Column
//    var university: String = "",
//
//    @Column
//    var isRegistered: Boolean = true,
//
//    @OneToOne
//   @JoinColumn
//   var seminars: SeminarEntity?
//
//){
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    val id: Long = 0L
//}