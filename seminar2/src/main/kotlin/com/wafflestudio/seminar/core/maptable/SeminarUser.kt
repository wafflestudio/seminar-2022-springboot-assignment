package com.wafflestudio.seminar.core.maptable

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.api.request.Role
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.profile.InstructorProfileEntity
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name ="seminarUser")
class SeminarUser (
    seminarEntity: SeminarEntity,
    userEntity: UserEntity,
    role: Role,
        ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_Id")
    @JsonIgnore
    val seminar : SeminarEntity = seminarEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    val user: UserEntity = userEntity
    
    val role : Role = role
    
    var isActive: Boolean = true
    
    var droppedAt: LocalDateTime? = null
    @CreatedDate
    @Column(columnDefinition = "datetime(6) default '1999-01-01'")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @CreatedDate
    @Column(columnDefinition = "datetime(6) default '1999-01-01'")
    var modifiedAt: LocalDateTime = LocalDateTime.now()
    
    fun dropSeminar(){
        isActive = false
        droppedAt = LocalDateTime.now()
        seminar.count -= 1
    }
}