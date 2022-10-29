package com.wafflestudio.seminar.core.profile.database

import org.springframework.data.jpa.repository.JpaRepository

interface InstructorProfileRepository: JpaRepository<InstructorProfileEntity, Long> {
    
}