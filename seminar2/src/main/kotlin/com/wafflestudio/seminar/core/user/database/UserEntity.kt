package com.wafflestudio.seminar.core.user.database
import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.user.domain.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class UserEntity(
    var username: String,
    var email: String,
    var password: String,

    @OneToOne(cascade = [CascadeType.ALL], optional = true, orphanRemoval = true)
    @JoinColumn(name = "participant_id")
    var participant: ParticipantEntity? = null,

    @OneToOne(cascade = [CascadeType.ALL], optional = true, orphanRemoval = true)
    @JoinColumn(name = "instructor_id")
    var instructor: InstructorEntity? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val userSeminars: MutableList<UserSeminarEntity> = mutableListOf()
): BaseTimeEntity() {
    var lastLogin: LocalDateTime = LocalDateTime.now()
    val dateJoined: LocalDateTime = LocalDateTime.now()
    
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
    
    fun updateLastLogin() {
        lastLogin = LocalDateTime.now()
    }
    
    fun updateInstructor(company: String, year: Int?) {
        if (instructor == null) return
        if (company != "") instructor!!.company = company
        if (year != null) instructor!!.year = year
    }
    
    fun updateParticipant(university: String) {
        if (participant == null) return
        if (university != "") participant!!.university = university
    }
}