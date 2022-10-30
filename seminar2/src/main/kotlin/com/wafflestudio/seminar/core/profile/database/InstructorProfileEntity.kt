package com.wafflestudio.seminar.core.profile.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

@Entity
@Table(name = "instructor_profile")
class InstructorProfileEntity(
        @Column(nullable = false)
        var company: String,
        
        @Column(nullable = true)
        var year: Int? = null,
        
        @OneToOne
        @JoinColumn(name = "user_id")
        @Cascade(CascadeType.ALL)
        val user: UserEntity,
): BaseTimeEntity() {
}