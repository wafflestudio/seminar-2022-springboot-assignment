package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class OsRepositoryImpl(private val memoryDB: MemoryDB, private val seminarExceptionHandler: SeminarExceptionHandler) :
    OsRepository {
    override fun findAll(): List<OperatingSystem> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): OperatingSystem {
        TODO("Not yet implemented")
    }

}