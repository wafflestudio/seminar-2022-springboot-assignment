package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar409
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
    var username: String,
    var password: String,
): BaseTimeEntity() {

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var participantProfile: ParticipantEntity? = null
    
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var instructorProfile: InstructorEntity? = null
    
    var lastLoginedAt: LocalDateTime = LocalDateTime.now()
    
    val isInstructor: Boolean
        get() = this.instructorProfile != null
    
    fun updateLastLogIn() {
        lastLoginedAt = LocalDateTime.now()
    }
    
    fun update(
        username: String?, 
        encodedPwd: String?,
        company: String,
        university: String,
        year: Long?,
    ) {
        this.username = username ?: this.username
        this.password = encodedPwd ?: this.password
        participantProfile?.update(university)
        instructorProfile?.update(company, year)
    }
    
    fun createProfile(
        university: String,
        isRegistered: Boolean,
    ) {
        if (participantProfile != null) {
            throw Seminar409("이미 참여자로 등록되어 있습니다.")
        }
        
        this.participantProfile = ParticipantEntity(this, university, isRegistered)
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