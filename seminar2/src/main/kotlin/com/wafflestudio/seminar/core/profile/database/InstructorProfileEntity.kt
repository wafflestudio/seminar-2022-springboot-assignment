package com.wafflestudio.seminar.core.profile.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.api.response.InstructorProfile
import com.wafflestudio.seminar.core.user.api.response.ParticipantProfile
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "instructor_profile")
class InstructorProfileEntity(
    override var createdAt: LocalDateTime? = null,
    override var modifiedAt: LocalDateTime? = null,
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_user_id")
    val user: UserEntity? = null
) : BaseTimeEntity() {}

