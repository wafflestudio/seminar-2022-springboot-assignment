package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@RequestMapping("/os")
class OsController(
    private val osService: OsService
) {
    
    @GetMapping("")
    fun getOs(): List<OperatingSystem> {
        return osService.findAllOs()
    }
    
    @GetMapping("/{id}")
    fun getOne(@PathVariable id: String): OperatingSystem {
        return osService.findOsById(id.toLong())
    }
}