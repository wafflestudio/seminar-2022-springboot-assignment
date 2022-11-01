package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.InstructorSeminarTableEntity
import com.wafflestudio.seminar.core.seminar.database.ParticipantSeminarTableEntity
import com.wafflestudio.seminar.core.user.domain.UserInfo
import javax.persistence.*

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class UserEntity(
    val email: String,
    var username: String,
    var password: String,
) : BaseTimeEntity() {
    
    @OneToOne
    @JoinColumn(name = "participant_profile_id")
    var participantProfile: ParticipantProfileEntity? = null;

    @OneToMany(mappedBy = "participant")
    val participatingSeminars: MutableSet<ParticipantSeminarTableEntity> = mutableSetOf();
    
    @OneToOne
    @JoinColumn(name = "instructor_profile_id")
    var instructorProfile: InstructorProfileEntity? = null;

    @OneToMany(mappedBy = "instructor")
    val instructingSeminars: MutableSet<InstructorSeminarTableEntity> = mutableSetOf();
    
    fun toUserInfo(): UserInfo = UserInfo(
        id,
        username,
        email,
        createdAt!!,
        modifiedAt!!,
        participantProfile?.toParticipantProfile(),
        instructorProfile?.toInstructorProfile(),
    )
    
}