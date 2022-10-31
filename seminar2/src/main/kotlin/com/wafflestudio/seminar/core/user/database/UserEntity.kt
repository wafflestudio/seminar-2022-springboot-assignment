package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class UserEntity(
    val email: String,
    val username: String,
    val password: String,
) : BaseTimeEntity() {
    
    @OneToOne
    var participantProfileEntity: ParticipantProfileEntity? = null;
    
    @OneToOne
    var instructorProfileEntity: InstructorProfileEntity? = null;
    
}