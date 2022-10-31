//package com.wafflestudio.seminar.common
//
//import org.hibernate.annotations.CreationTimestamp
//import org.springframework.data.annotation.CreatedDate
//import org.springframework.data.jpa.domain.support.AuditingEntityListener
//import java.time.LocalDateTime
//import javax.persistence.*
//
//@MappedSuperclass
//@EntityListeners(AuditingEntityListener::class)
//abstract class BaseTimeEntity {
//    
//    @CreatedDate
//    val createdDate : LocalDateTime? = null
//}