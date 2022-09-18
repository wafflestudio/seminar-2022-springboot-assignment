package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import org.springframework.stereotype.Service

@Service
class OsService(private val osRepository : OsRepository) {
    fun getAllOS() = osRepository.findAll()
    fun searchOSByName(name: String) = osRepository.findByName(name)
    fun searchOSById(id: Long) = osRepository.findById(id)
}