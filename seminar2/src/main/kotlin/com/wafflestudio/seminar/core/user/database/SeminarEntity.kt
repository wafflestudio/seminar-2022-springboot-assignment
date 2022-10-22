package com.wafflestudio.seminar.core.user.database

import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name="Seminar")
class SeminarEntity(
    
    @Column
    var name: String,
    
    @Column
    var capacity: Int,
    
    @Column
    var count: Int,
    
    @Column
    var time: LocalTime,
    
    @Column
    var online: Boolean = true,
    
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="instructor_id")
    var instructorProfileEntity: InstructorProfileEntity
    

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}