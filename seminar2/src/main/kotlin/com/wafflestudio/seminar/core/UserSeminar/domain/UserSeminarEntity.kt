package com.wafflestudio.seminar.core.UserSeminar.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.wafflestudio.seminar.core.seminar.domain.SeminarEntity
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.domain.enums.RoleType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
@Table(name="UserSeminar")
@EntityListeners(AuditingEntityListener::class)
class UserSeminarEntity (
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    val user: UserEntity,
    
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seminarId")
    val seminar: SeminarEntity
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @CreatedDate
    var joinedAt: LocalDateTime? = LocalDateTime.parse(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )
    @LastModifiedDate
    var modifiedAt: LocalDateTime? = LocalDateTime.parse(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )
    var droppedAt: LocalDateTime? = null

    @Enumerated(EnumType.STRING)
    var role: RoleType = user.role
    var isActive: Boolean = true
}