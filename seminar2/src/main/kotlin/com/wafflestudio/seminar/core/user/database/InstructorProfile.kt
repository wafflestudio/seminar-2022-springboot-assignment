package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
class InstructorProfile (
    @Column(name = "company")
    val company : String = "",
    
    val year : Int? = null
    
        ): BaseTimeEntity(){
    
    @OneToOne(mappedBy = "instructor")
    lateinit var user :UserEntity
    
    
    
}