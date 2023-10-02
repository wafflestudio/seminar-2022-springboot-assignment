package com.wafflestudio.seminar.core.seminar.database


import org.springframework.data.jpa.repository.JpaRepository

interface SeminarRepository : JpaRepository<SeminarEntity, Long>{
    fun findByNameContainingOrderByCreatedAtDesc(name:String):List<SeminarEntity>
    fun findByNameContainingOrderByCreatedAtAsc(name: String):List<SeminarEntity>
}