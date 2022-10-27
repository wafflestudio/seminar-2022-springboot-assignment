package com.wafflestudio.seminar.user.database
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity (
    @Column(nullable = false)
    val nickname: String,
    @Column(unique = true, nullable = false)
    val email: String,
    var password: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}