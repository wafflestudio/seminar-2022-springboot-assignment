package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.api.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.api.exception.ErrorCode
import com.wafflestudio.seminar.survey.api.exception.SeminarException
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException

@Component
class OsRepositoryImpl(private val memoryDB: MemoryDB, private val seminarExceptionHandler: SeminarExceptionHandler) : OsRepository {
    override fun findAll(): List<OperatingSystem> {
        return memoryDB.getOperatingSystems()
    }

    override fun findById(id: Long): OperatingSystem {
        val osList = memoryDB.getOperatingSystems()
        var temp : OperatingSystem? = osList.find{it.id == id}
        val os = temp ?: throw SeminarException(ErrorCode.OS_ID_NOT_FOUND, id.toString())
        return os
    }

    override fun findByName(name: String): OperatingSystem {
        val osList = memoryDB.getOperatingSystems()
        var temp : OperatingSystem? = osList.find{it.osName == name}
        val os = temp ?: throw SeminarException(ErrorCode.OS_NAME_NOT_FOUND, name)
        return os
    }
}