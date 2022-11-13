package com.wafflestudio.seminar.core.profile.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

@Entity
@Table(name = "participant_profile")
class ParticipantProfileEntity(
        @Column(nullable = false)
        var university: String,

        @Column(nullable = false)
        var isRegistered: Boolean,

        @OneToOne
        @JoinColumn(name = "user_id")
        val user: UserEntity,
): BaseTimeEntity() {
}