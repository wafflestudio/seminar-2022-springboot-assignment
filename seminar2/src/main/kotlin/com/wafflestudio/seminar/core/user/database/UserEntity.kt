package com.wafflestudio.seminar.core.user.database

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator
import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.user.domain.User
import javax.persistence.*

@Entity
@Table(name = "`user`")
@JsonIdentityInfo(generator = IntSequenceGenerator::class, property = "id")
data class UserEntity(
    @Column(name = "email", unique = true, nullable = false)
    val email: String,
    @Column(name = "name", nullable = false)
    val username: String,
    @Column(name = "password", nullable = false)
    val password: String,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    var participant: ParticipantProfileEntity? = null,
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    var instructor: InstructorProfileEntity? = null,
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val seminars: MutableList<UserSeminarEntity> = mutableListOf()
) : BaseTimeEntity() {
    
    fun toUser(): User {
        return User(
            id = id,
            email = email,
            username = username,
            password = password,
            lastLogin = modifiedAt,
            dateJoined = createdAt,
            participant = participant?.toParticipant(),
            instructor = instructor?.toInstructor(),
        )
    }

    override fun hashCode() = id.hashCode()
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}