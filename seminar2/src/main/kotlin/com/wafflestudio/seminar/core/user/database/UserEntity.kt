package com.wafflestudio.seminar.core.user.database
import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class UserEntity(
    val username: String,
    val email: String,
    val password: String,
    var lastLogin: LocalDateTime,
    val dateJoined: LocalDateTime,

    @OneToOne(cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(name = "participant_id")
    var participant: ParticipantEntity? = null,

    @OneToOne(cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(name = "instructor_id")
    var instructor: InstructorEntity? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val userSeminars: MutableList<UserSeminarEntity> = mutableListOf()
): BaseTimeEntity() {
    fun toUser(): User {
        val seminars = userSeminars.filter { it.role == "participant" }.map { it.toParticipant() }
        val instructingSeminars = userSeminars.filter { it.role == "instructor" }.map { it.toInstructor() }
        
        return User(
            id = id,
            username = username,
            email = email,
            lastLogin = lastLogin,
            dateJoined = dateJoined,
            participant = participant?.toParticipant(seminars),
            instructor = instructor?.toInstructor(instructingSeminars),
        )
    }
}