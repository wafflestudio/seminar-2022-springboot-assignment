package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name="userSeminar")
class UserSeminarEntity(
    
    
    @Column
    var seminarName: String,

    @Column    
    var professor: String,
    
    @Column
    var grade: Int,   //상대평가, 절대평가
    
    @ManyToOne(fetch = FetchType.LAZY)
    var participant : ParticipantProfileEntity ?= null
    
    
)  : BaseTimeEntity(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0L

    
}