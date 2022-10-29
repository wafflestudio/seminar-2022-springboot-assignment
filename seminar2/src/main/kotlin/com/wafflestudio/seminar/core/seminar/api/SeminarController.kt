package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.core.seminar.service.SeminarService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class SeminarController(
    private val seminarService: SeminarService
) 