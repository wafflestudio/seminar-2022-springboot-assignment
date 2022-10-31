package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class UserEntity(
    val email: String,
    val username: String,
    val password: String,
) : BaseTimeEntity() {
    
    @OneToOne
    @JoinColumn(name = "participant_profile_id")
    var participantProfile: ParticipantProfileEntity? = null;
    
    @OneToOne
    @JoinColumn(name = "instructor_profile_id")
    var instructorProfile: InstructorProfileEntity? = null;
    
}