package com.wafflestudio.seminar.core.user.database

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wafflestudio.seminar.core.maptable.SeminarUser
import com.wafflestudio.seminar.core.user.database.profile.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.profile.ParticipantProfileEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name="users")
class UserEntity (
    userName : String,
    email: String,
    password: String,
    instructorProfileEntity: InstructorProfileEntity?,
    participantProfileEntity: ParticipantProfileEntity?,
    dateJoined : LocalDateTime,
        ){
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_id")
    val id: Long = 0L
    
    @Column(nullable = false)
    var userName = userName
    
    @Column(nullable = false)
    val email: String = email
    
    @Column(nullable = false)
    @JsonIgnore
    val password = password
    
   
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "instructorProfile_Id")
    var instructorProfileEntity: InstructorProfileEntity? = instructorProfileEntity
    
    
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "participantsProfile_Id")
    var participantProfileEntity: ParticipantProfileEntity? = participantProfileEntity
    
    @OneToMany(mappedBy = "user", cascade = arrayOf(CascadeType.ALL))
    var seminarUser : MutableList<SeminarUser> = mutableListOf()
    
    var lastLogin: LocalDateTime? = null
    var dateJoined : LocalDateTime = dateJoined
}