package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.OsRepositoryImpl
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Service

@Service
class OsServiceImpl(
    private val osRepositoryImpl : OsRepositoryImpl
) : OsService {
    override fun findById(id : Long) : OperatingSystem?{
        return osRepositoryImpl.findById(id)
    }
    override fun findByName(name: String): OperatingSystem? {
        return osRepositoryImpl.findByName(name)
    }


}