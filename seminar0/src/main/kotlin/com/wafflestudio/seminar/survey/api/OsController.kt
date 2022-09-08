package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class OsController(
    val osService: OsService
) {
    // OS ID로 검색 API
    @GetMapping("/os/{id}")
    fun getOsById(): OperatingSystem {
        TODO("Not yet implemented")
    }
    
    // OS 이름으로 검색 API
    @GetMapping("/os")
    fun getOsByName(@RequestParam("name") name: String): OperatingSystem {
        TODO("Not yet implemented")
    }
}