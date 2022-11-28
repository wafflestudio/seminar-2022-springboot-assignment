package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.domain.InstructingSeminarInfo
import com.wafflestudio.seminar.core.seminar.domain.ParticipatingSeminarInfo
import com.wafflestudio.seminar.core.user.domain.InstructorProfile
import com.wafflestudio.seminar.core.user.domain.ParticipantProfile
import javax.persistence.*

@Entity
@Table(name = "participant_profiles")
class ParticipantProfileEntity(
    var university: String?,
    var isRegistered: Boolean,
) : BaseTimeEntity() {

    fun toParticipantProfile(seminars: List<ParticipatingSeminarInfo>): ParticipantProfile = ParticipantProfile(
        id,
        university,
        isRegistered,
        seminars,
    )
}

@Entity
@Table(name = "instructor_profiles")
class InstructorProfileEntity(
    var company: String?,
    var year: Int?,
) : BaseTimeEntity() {

    fun toInstructorProfile(seminars: List<InstructingSeminarInfo>): InstructorProfile = InstructorProfile(
        id,
        company,
        year,
        seminars,
    )
}