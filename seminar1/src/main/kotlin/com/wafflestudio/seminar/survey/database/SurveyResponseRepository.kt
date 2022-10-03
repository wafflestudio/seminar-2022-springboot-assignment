package com.wafflestudio.seminar.survey.database

import org.springframework.data.jpa.repository.JpaRepository

interface SurveyResponseRepository : JpaRepository<SurveyResponseEntity, Long>