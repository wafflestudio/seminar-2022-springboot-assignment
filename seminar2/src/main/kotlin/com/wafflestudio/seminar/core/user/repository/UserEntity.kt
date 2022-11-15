package com.wafflestudio.seminar.core.user.repository

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.mappingTable.InstructorProfile
import com.wafflestudio.seminar.core.mappingTable.ParticipantProfile
import com.wafflestudio.seminar.core.mappingTable.UserSeminar
import com.wafflestudio.seminar.core.seminar.repository.Seminar
import com.wafflestudio.seminar.core.user.Role
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class UserEntity (
    username: String,
    email: String,
    password: String,
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    @JoinColumn(name = "participant_profile_id")
    var participant: ParticipantProfile? = null,
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.REMOVE]) 
    @JoinColumn(name = "instructor_profile_id")
    var instructor: InstructorProfile? = null,
): BaseTimeEntity() 
{

    @Column(nullable = false)
    var username = username;
    
    @Column(nullable = false, unique = true)
    var email = email;
    
    @Column(nullable = false)
    var password = password;

    @Column(columnDefinition = "datetime(6) default '1999-01-01'")
    var lastLogin: LocalDateTime? = null
    
    @OneToMany(
        mappedBy = "userEntity",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.REMOVE],
        orphanRemoval = true
    )
    var userSeminars: MutableList<UserSeminar> = mutableListOf()
 
    fun openSeminar(seminar: Seminar): Seminar {
        val userSeminar = UserSeminar(seminar, this, Role.Instructor)
        userSeminars.add(userSeminar)
        seminar.userSeminars.add(userSeminar)
        return seminar
    }
    
    fun participateSeminar(seminar: Seminar) {
        val userSeminar = UserSeminar(seminar, this, Role.Participant)
        userSeminar.createdAt = LocalDateTime.now()
        userSeminars.add(userSeminar)
        userSeminar.seminar.userSeminars.add(userSeminar)
        userSeminar.seminar.participantCount += 1
    }

    fun instructSeminar(seminar: Seminar) {
        val userSeminar = UserSeminar(seminar, this, Role.Instructor)
        userSeminar.createdAt = LocalDateTime.now()
        userSeminars.add(userSeminar)
        seminar.userSeminars.add(userSeminar)
    }
}