package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.seminar.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.user.domain.User
import javax.persistence.*

@Entity
@Table(name = "user")
class UserEntity(
    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    val encodedPassword: String,

    @OneToMany(mappedBy = "user")
    val userSeminars: Set<UserSeminarEntity>? = HashSet(),

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "participant_profile_id", referencedColumnName = "id")
    val participantProfile: ParticipantProfileEntity?,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "instructor_profile_id", referencedColumnName = "id")
    val instructorProfile: InstructorProfileEntity?
) : BaseTimeEntity() {
    fun toUser(): User {
        return User(
            email = email,
            username = username,
            encodedPassword = encodedPassword
        )
    }
}