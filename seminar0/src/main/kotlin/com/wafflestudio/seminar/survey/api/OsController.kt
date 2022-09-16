package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/os")
class OsController(private val osService: OsService) {
    @GetMapping("/{osId}")
    fun getByOsId(@PathVariable osId: Long): OperatingSystem {
        val res = osService.findById(osId)
        if (res.isEmpty()) {
            throw SeminarExceptionHandler().SeminarException("Page not found", HttpStatus.NOT_FOUND)
        }
        return res[0]
    }

    @GetMapping("/search")
    fun getByOsName(@RequestParam name: String): OperatingSystem {
        val res = osService.findByName(name)
        if (res.isEmpty()) {
            throw SeminarExceptionHandler().SeminarException("Page not found", HttpStatus.NOT_FOUND)
        }
        return res[0]
    }
}