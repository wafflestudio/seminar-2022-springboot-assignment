package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.Role
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.user.domain.Profile
import com.wafflestudio.seminar.core.user.domain.User
import javax.persistence.*

@Entity
@Table(name = "USER")
class UserEntity(
    val name : String,
    
    @Column(unique = true, nullable = false)
    val email : String,
    
    val encodedPassword : String,
    val role : Role
) {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L
    
    @OneToMany(mappedBy = "user")
    private val seminars : List<UserSeminarEntity> = ArrayList()
    
    @OneToOne
    private val instructor : InstructorProfile? = null
    
    @OneToOne
    private val participant : ParticipantProfile? = null
    
    
    fun toUser(): User {
        return User(
            name = name,
            email = email
        )
    }
    fun toProfile(): Profile{
        return Profile(
            id = id,
            username = name,
            email = email,
            
        )
    } 
}