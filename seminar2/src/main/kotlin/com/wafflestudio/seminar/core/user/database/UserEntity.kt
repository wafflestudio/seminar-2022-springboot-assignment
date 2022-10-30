package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.InstructorEntity
import com.wafflestudio.seminar.core.seminar.database.ParticipantEntity
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
class UserEntity(
    @Column(unique = true)
    val email: String,
    val username: String,
    val password: String,
): BaseTimeEntity() {

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var participantProfile: ParticipantEntity? = null
    
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var instructorProfile: InstructorEntity? = null
    
    var lastLoginedAt: LocalDateTime = LocalDateTime.now()
    
    fun updateLastLogIn() {
        lastLoginedAt = LocalDateTime.now()
    }
        
    companion object {
        fun participant(
            email: String,
            username: String,
            password: String,
            university: String,
            isActive: Boolean,
        ): UserEntity {
            return UserEntity(email, username, password)
                .apply { participantProfile = ParticipantEntity(this, university, isActive) }
        }
        
        fun instructor(
            email: String,
            username: String,
            password: String,
            company: String,
            year: Long?,
        ): UserEntity {
            return UserEntity(email, username, password)
                .apply { instructorProfile = InstructorEntity(this, company, year) }
        }
    }
}