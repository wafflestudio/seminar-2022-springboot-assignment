package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
class ParticipantProfile(
    @Column(name = "university")
    val university : String = "",
    
    val isRegistered : Boolean = true
) :BaseTimeEntity(){
    
    @OneToOne(mappedBy = "participant")
    lateinit var user :UserEntity
}