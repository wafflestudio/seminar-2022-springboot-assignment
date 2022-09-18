package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import org.springframework.stereotype.Service

@Service
class OsService(private val osRepo: OsRepository) {
    fun searchTotal() = osRepo.findAll()
    fun searchByName(name: String) = osRepo.findByName(name)
    fun searchById(id: Long) = osRepo.findById(id)
}