package com.wafflestudio.seminar.core.user.database.profile

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "participantsProfile")
class ParticipantProfileEntity(
    isRegistered : Boolean?,
    university : String?,
) {

    @Id
    @Column(name = "participantsProfile_Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L

    @Column(nullable = false)
    var university : String? = university
 
    @Column(nullable = false)
    var isRegistered : Boolean? = isRegistered

    @CreatedDate
    @Column(columnDefinition = "datetime(6) default '1999-01-01'")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @CreatedDate
    @Column(columnDefinition = "datetime(6) default '1999-01-01'")
    var modifiedAt: LocalDateTime = LocalDateTime.now()
}