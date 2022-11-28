package com.wafflestudio.seminar.core.user.repository

import com.wafflestudio.seminar.core.user.domain.profile.InstructorProfile
import org.springframework.data.jpa.repository.JpaRepository

interface InstructorProfileRepository : JpaRepository<InstructorProfile, Long>