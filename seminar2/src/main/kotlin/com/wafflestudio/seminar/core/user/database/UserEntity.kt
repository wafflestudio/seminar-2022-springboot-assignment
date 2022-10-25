package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.core.user.domain.ParticipantProfile
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="user")
class UserEntity(


        
    @Column
    var username: String,
    
    @Column
    var email: String,
    
    @Column
    var password: String,
    
    @Column
    var dateJoined: LocalDateTime,
    
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="participant_id")
    var participant: ParticipantProfileEntity? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="instructor_id")
    var instructor: InstructorProfileEntity? = null

    
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    
    
}