package com.wafflestudio.seminar.core.user.database
import javax.persistence.*

@Entity
class UserEntity(
    val username: String,
    val email: String,
    val password: String,
    @OneToOne(cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(name = "participant_id")
    var participant: ParticipantEntity? = null,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
    
}