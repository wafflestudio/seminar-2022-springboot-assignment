package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.LoginUser
import com.wafflestudio.seminar.common.SeminarRequestBodyException
import com.wafflestudio.seminar.core.seminar.dto.SeminarPostRequest
import com.wafflestudio.seminar.core.seminar.dto.SeminarPutRequest
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class SeminarController(
        private val seminarService: SeminarService,
) {
    @PostMapping("/api/v1/seminar")
    fun postSeminar(
            @Valid @RequestBody seminarPostRequest: SeminarPostRequest,
            bindingResult: BindingResult,
            @LoginUser meUser: UserEntity?,
    ) = if (bindingResult.hasErrors()) {
        throw SeminarRequestBodyException(bindingResult.fieldErrors)
    } else {
        meUser?.let {
            seminarService.createSeminarAndReturnSeminarDetail(seminarPostRequest, meUser)
        }
                ?: ResponseEntity<String>("Failed to get current user info.", HttpStatus.UNAUTHORIZED)
    }
    
    @PutMapping("/api/v1/seminar")
    fun putSeminar(
            @Valid @RequestBody seminarPutRequest: SeminarPutRequest,
            bindingResult: BindingResult,
            @LoginUser meUser: UserEntity?,
    ) = if (bindingResult.hasErrors()) {
        throw SeminarRequestBodyException(bindingResult.fieldErrors)
    } else {
        meUser?.let {
            seminarService.modifySeminarAndReturnSeminarDetail(seminarPutRequest, meUser)
        }
            ?: ResponseEntity<String>("Failed to get current user info.", HttpStatus.UNAUTHORIZED)
    }
}