package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList

@Entity
class SeminarEntity(

    var name : String,
    var capacity : Int,
    var count : Int,
    var time : String,
    var online : Boolean,
    val madeBy : Long
    
        )
    : BaseTimeEntity(){
    
    @Id
    @Column(name = "SEMINAR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id : Long = 0L
    
    @OneToMany(mappedBy = "seminar")
    private val users : List<UserSeminarEntity> = ArrayList()
}