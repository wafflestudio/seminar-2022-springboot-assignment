package com.wafflestudio.seminar.core.seminar.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SeminarRepository: JpaRepository<Seminar, Long> {
    
    @Query("select s from Seminar s join fetch s.userSeminars us where s.name like %:name% order by s.createdAt desc")
    fun findByNameContainingOrderByCreatedAtDesc(name: String): List<Seminar>
    
    @Query("select s from Seminar s join fetch s.userSeminars us where s.name like %:name% order by s.createdAt asc")
    fun findByNameContainingOrderByCreatedAtAsc(name: String): List<Seminar>
}