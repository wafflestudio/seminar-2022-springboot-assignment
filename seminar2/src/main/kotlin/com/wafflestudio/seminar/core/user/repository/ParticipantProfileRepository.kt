package com.wafflestudio.seminar.core.user.repository

import com.wafflestudio.seminar.core.user.domain.profile.ParticipantProfile
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantProfileRepository: JpaRepository<ParticipantProfile, Long>