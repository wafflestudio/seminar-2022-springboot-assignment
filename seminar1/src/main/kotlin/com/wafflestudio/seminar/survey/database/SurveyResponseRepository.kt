package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.user.database.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SurveyResponseRepository : JpaRepository<SurveyResponseEntity, Long>