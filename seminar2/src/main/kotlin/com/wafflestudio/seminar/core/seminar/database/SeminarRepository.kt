package com.wafflestudio.seminar.core.seminar.database

import org.springframework.data.jpa.repository.JpaRepository

interface SeminarRepository : JpaRepository<SeminarEntity, Long> {
    fun findAllByNameContainingOrderByCreatedAt(name : String) : ArrayList<SeminarEntity>
    fun findAllByOrderByCreatedAt() : ArrayList<SeminarEntity>
}