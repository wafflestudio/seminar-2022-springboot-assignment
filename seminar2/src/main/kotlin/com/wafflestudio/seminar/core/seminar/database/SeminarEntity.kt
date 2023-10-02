package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.maptable.SeminarUser
import com.wafflestudio.seminar.core.seminar.api.request.createSeminarRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList


@Entity(name="seminar")
class SeminarEntity (
    name: String,
    capacity : Int,
    count : Int,
    time : String,
    online : Boolean
        ){
    constructor(request : createSeminarRequest) :this(
        request.name,
        request.capacity,
        request.count,
        request.time,
        request.online
    )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seminar_Id")
    val id: Long = 0L
    
    @Column(nullable = false)
    var name: String = name

    @Column(nullable = false)
    var capacity: Int = capacity

    @Column(nullable = false)
    var count : Int = count

    @Column(nullable = false)
    val time : LocalTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))

    @Column(nullable = false)
    var online: Boolean = online
    
    @OneToMany(mappedBy = "seminar", cascade = arrayOf(CascadeType.ALL))
    var seminarUser: MutableList<SeminarUser> = ArrayList()

    @CreatedDate
    @Column(columnDefinition = "datetime(6) default '1999-01-01'")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @CreatedDate
    @Column(columnDefinition = "datetime(6) default '1999-01-01'")
    var modifiedAt: LocalDateTime = LocalDateTime.now()

}