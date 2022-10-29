package com.wafflestudio.seminar.user.database

//import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.*
//import javax.persistence.EnumType
//import javax.persistence.Enumerated

@Entity
@Table(name = "users")
class UserEntity(
    @Column(name = "nickname")
    val nickname: String,
    @Column(name = "email")
    val email : String,
    @Column(name = "password")
    val password : String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val userId: Long = 0L
}