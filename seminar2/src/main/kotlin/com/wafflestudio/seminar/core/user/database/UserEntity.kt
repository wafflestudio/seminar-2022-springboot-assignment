package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.core.join.UserSeminarEntity
import com.wafflestudio.seminar.core.profile.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileEntity
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "user")
class UserEntity (
        @Size(min = 1) @Column(nullable = false)
        var username: String,
        
        @Size(min = 1) @Column(nullable = false, unique = true)
        val email: String,

        @Size(min = 1) @Column(nullable = false)
        var password: String,
        
        @CreationTimestamp @Column(nullable = false)
        var lastLogin: LocalDateTime? = null,

        @OneToMany(mappedBy = "user")
        @Cascade(CascadeType.ALL)
        val seminars: MutableSet<UserSeminarEntity> = mutableSetOf(),

        @OneToOne(optional = true, mappedBy = "user")
        @Cascade(CascadeType.ALL)
        var participantProfile: ParticipantProfileEntity? = null,
        
        @OneToOne(optional = true, mappedBy = "user")
        @Cascade(CascadeType.ALL)
        var instructorProfile: InstructorProfileEntity? = null,
) {
    @CreationTimestamp @Column(nullable = false, updatable = false)
    val dataJoined: LocalDateTime? = null

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}