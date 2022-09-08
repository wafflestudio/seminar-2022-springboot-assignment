package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.os.OsRepository
import org.springframework.stereotype.Service

@Service
class OsService(
    val osRepository: OsRepository
) {
}