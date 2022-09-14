package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.ExceptionHandler

@Repository
class OsRepositoryImpl(
    private val db: MemoryDB
): OsRepository {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity(e.message ?: "Illegal Argument", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<String> {
        return ResponseEntity(e.message ?: "Exception", HttpStatus.INTERNAL_SERVER_ERROR)
    }
    
    @Override
    override fun findAll(): List<OperatingSystem> {
        return db.getOperatingSystems()
    }
    
    @Override
    override fun findById(id: Long): OperatingSystem? {
        return db.getOperatingSystems().associateBy { it.id }[id]
    }
}