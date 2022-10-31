package com.wafflestudio.seminar.core.user.domain

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.wafflestudio.seminar.core.UserSeminar.domain.UserSeminarEntity
import com.wafflestudio.seminar.core.user.domain.enums.RoleType
import com.wafflestudio.seminar.core.user.domain.profile.InstructorProfile
import com.wafflestudio.seminar.core.user.domain.profile.ParticipantProfile
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
@Table(name="users")
@EntityListeners(AuditingEntityListener::class)
class UserEntity(
    @Column(nullable=false, unique=true)
    val email: String,
    @Column(nullable=false)
    var username: String,
    @Column(nullable=false)
    var password: String,
    @Enumerated(EnumType.STRING)
    var role: RoleType,

    @JsonManagedReference
    @OneToMany(
        mappedBy = "user",
        fetch = FetchType.LAZY,
        cascade = arrayOf(CascadeType.REMOVE, CascadeType.PERSIST)
    )
    var seminarList: MutableList<UserSeminarEntity>? = null,
    @OneToOne(
        mappedBy = "user",
        fetch = FetchType.LAZY,
        cascade = arrayOf(CascadeType.REMOVE, CascadeType.PERSIST)
    )
    var participantProfile: ParticipantProfile? = null,
    @OneToOne(
        mappedBy = "user",
        fetch = FetchType.LAZY,
        cascade = arrayOf(CascadeType.REMOVE, CascadeType.PERSIST)
    )
    var instructorProfile: InstructorProfile? = null,
) {
    
    @CreatedDate
    var dateJoined: LocalDate? = LocalDate.now()
    
    @CreatedDate
    var lastLogin: LocalDateTime? = LocalDateTime.parse(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}