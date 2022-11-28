package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.InstructorSeminarTableEntity
import com.wafflestudio.seminar.core.seminar.database.ParticipantSeminarTableEntity
import com.wafflestudio.seminar.core.user.api.request.UpdateRequest
import com.wafflestudio.seminar.core.user.domain.UserInfo
import org.springframework.security.crypto.password.PasswordEncoder
import javax.persistence.*
import javax.persistence.CascadeType

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class UserEntity(
    val email: String,
    var username: String,
    var password: String,
) : BaseTimeEntity() {

    @OneToOne(cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "participant_profile_id")
    var participantProfile: ParticipantProfileEntity? = null

    @OneToMany(mappedBy = "participant", cascade = [CascadeType.REMOVE])
    val participatingSeminars: MutableSet<ParticipantSeminarTableEntity> = mutableSetOf()

    @OneToOne(cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "instructor_profile_id")
    var instructorProfile: InstructorProfileEntity? = null

    @OneToMany(mappedBy = "instructor", cascade = [CascadeType.REMOVE])
    val instructingSeminars: MutableSet<InstructorSeminarTableEntity> = mutableSetOf()

    fun updateUser(updateRequest: UpdateRequest, passwordEncoder: PasswordEncoder): UserInfo {
        username = updateRequest.username ?: username
        password = updateRequest.password ?: passwordEncoder.encode(password)
        participantProfile?.let {
            it.university = updateRequest.university ?: it.university
        }
        instructorProfile?.let {
            it.company = updateRequest.company ?: it.company
            it.year = updateRequest.year ?: it.year
        }

        return this.toUserInfo()
    }

    fun toUserInfo(): UserInfo = UserInfo(
        id,
        username,
        email,
        createdAt!!,
        modifiedAt!!,
        participantProfile?.toParticipantProfile(
            participatingSeminars.map { it.toParticipatingSeminarInfo() }
        ),
        instructorProfile?.toInstructorProfile(
            instructingSeminars.map { it.toInstructingSeminarInfo() }
        ),
    )

}