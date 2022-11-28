package com.wafflestudio.seminar.core.seminar.repository

import com.wafflestudio.seminar.core.seminar.domain.SeminarEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SeminarRepository: JpaRepository<SeminarEntity, Long>, SeminarCustomRepository