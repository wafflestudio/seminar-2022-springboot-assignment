package com.wafflestudio.seminar.core.user.database


import org.springframework.data.jpa.repository.JpaRepository

interface InstructorProfileRepository : JpaRepository<InstructorProfileEntity, Long> {
}