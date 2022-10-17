package com.wafflestudio.seminar.core.user.database

import javax.persistence.*

@Entity
@Table(name="ParticipantProfile")
class ParticipantProfileEntity(
    
    @Column
    var emailParticipant: String,
    
    @Column
    var university: String = "",
    
    @Column
    var isRegistered: Boolean = true,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}