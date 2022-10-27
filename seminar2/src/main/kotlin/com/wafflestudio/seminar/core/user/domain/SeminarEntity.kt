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

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="instructor_id")
    var instructors: UserEntity

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}