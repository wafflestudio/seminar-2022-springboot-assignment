package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/os")
class OsController(
    val osService: OsService
) {
    // OS ID로 검색 API
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): OperatingSystem {
        return osService.findById(id)
    }
    
    // OS 이름으로 검색 API
    @GetMapping("")
    fun findByName(@RequestParam name: String): OperatingSystem {
        return osService.findByName(name)
    }
}