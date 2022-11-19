package com.wafflestudio.seminar.common

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTimeEntity {

    @CreatedDate
    @Column(columnDefinition = "datetime(6) default '1999-01-01'")
    var createdAt: LocalDateTime? = LocalDateTime.now()

    @CreatedDate
    @Column(columnDefinition = "datetime(6) default '1999-01-01'")
    var modifiedAt: LocalDateTime? = LocalDateTime.now()

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}