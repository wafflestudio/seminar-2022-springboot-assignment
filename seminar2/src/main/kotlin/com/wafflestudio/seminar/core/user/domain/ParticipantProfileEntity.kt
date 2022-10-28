package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.common.BaseTimeEntity
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name="ParticipantProfile")
class ParticipantProfileEntity (
    
    
    @Column
    var university: String? = "",
    
    @Column
    var isRegistered: Boolean = true,


    


): BaseTimeEntity(){
   

   
}