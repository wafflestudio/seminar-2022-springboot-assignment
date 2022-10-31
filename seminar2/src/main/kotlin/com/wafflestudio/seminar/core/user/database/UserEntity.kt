package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user")
class UserEntity(
    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    var username: String,

    @Column(nullable = false)
    var encodedPassword: String,

    var lastLogin: LocalDateTime,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val userSeminars: MutableSet<UserSeminarEntity> = mutableSetOf(),

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "participant_profile_id", referencedColumnName = "id")
    var participantProfile: ParticipantProfileEntity?,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "instructor_profile_id", referencedColumnName = "id")
    var instructorProfile: InstructorProfileEntity?
) : BaseTimeEntity() 