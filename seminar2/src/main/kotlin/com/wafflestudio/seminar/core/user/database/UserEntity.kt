package com.wafflestudio.seminar.core.user.database
import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*

@Entity
class UserEntity(
    val username: String,
    val email: String,
    val password: String,
    
    @OneToOne(cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(name = "participant_id")
    var participant: ParticipantEntity? = null,
    
    @OneToOne(cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(name = "instructor_id")
    var instructor: InstructorEntity? = null,
): BaseTimeEntity()