package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.data.jpa.repository.JpaRepository

interface SurveyResponseRepository : JpaRepository<SurveyResponse, Long>