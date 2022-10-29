package com.wafflestudio.seminar.core.user.domain

import javax.persistence.*

@Entity
@Table(name="seminar")
class SeminarEntity(
    
 
    @Column
    var name: String?,
    
    @Column
    var capacity: Int,
    
    @Column
    var count: Int,

    @Column
    var time: String,

    @Column
    var online: Boolean = true,

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}