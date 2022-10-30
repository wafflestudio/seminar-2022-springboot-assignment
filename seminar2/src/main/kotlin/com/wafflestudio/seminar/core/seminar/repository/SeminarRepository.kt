package com.wafflestudio.seminar.core.seminar.repository

import org.springframework.data.jpa.repository.JpaRepository

interface SeminarRepository: JpaRepository<Seminar, Long> {
    fun findByNameContainingOrderByCreatedAtDesc(name: String): List<Seminar>
    fun findByNameContainingOrderByCreatedAtAsc(name: String): List<Seminar>
}