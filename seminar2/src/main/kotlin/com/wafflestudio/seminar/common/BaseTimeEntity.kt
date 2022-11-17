package com.wafflestudio.seminar.common

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTimeEntity {

    @CreatedDate
    @Column(columnDefinition = "datetime(6) default '1999-01-01'")
    var createdAt: LocalDateTime? = LocalDateTime.parse(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )

    @LastModifiedDate
    @Column(columnDefinition = "datetime(6) default '1999-01-01'")
    var modifiedAt: LocalDateTime? = LocalDateTime.parse(
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}