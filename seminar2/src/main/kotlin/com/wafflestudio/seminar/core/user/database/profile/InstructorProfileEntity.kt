package com.wafflestudio.seminar.core.user.database.profile

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

import javax.persistence.*


@Entity(name = "instructorProfile")
class InstructorProfileEntity (
        company: String?,
        careerYear: Int?,
        ){
        
        @Id
        @Column(name = "instructorProfile_Id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id : Long = 0L
                
        @Column(nullable = true)
        var company : String? = company

        @Column
        var careerYear: Int? = careerYear
        
        @CreatedDate
        @Column(columnDefinition = "datetime(6) default '1999-01-01'")
        var createdAt: LocalDateTime = LocalDateTime.now()

        @CreatedDate
        @Column(columnDefinition = "datetime(6) default '1999-01-01'")
        var modifiedAt: LocalDateTime = LocalDateTime.now()
}