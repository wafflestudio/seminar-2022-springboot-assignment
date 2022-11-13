package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.LogExecutionTime
import com.wafflestudio.seminar.common.LoginUser
import com.wafflestudio.seminar.common.SeminarRequestBodyException
import com.wafflestudio.seminar.core.seminar.dto.SeminarPostRequest
import com.wafflestudio.seminar.core.seminar.dto.SeminarPutRequest
import com.wafflestudio.seminar.core.seminar.dto.SeminarRegisterRequest
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.hibernate.annotations.common.util.impl.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class SeminarController(
        private val seminarService: SeminarService,
) {
    private val log = org.slf4j.LoggerFactory.getLogger(javaClass)
    
    @LogExecutionTime
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

    @LogExecutionTime
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

    @LogExecutionTime
    @GetMapping("/api/v1/seminar/{seminarId}")
    fun getSeminar(@PathVariable seminarId: Long) = seminarService.getSeminarDetailById(seminarId)

    @LogExecutionTime
    @GetMapping("/api/v1/seminar")
    fun getSeminarsWithQuery(
            @RequestParam(name = "name") name: String?,
            @RequestParam(name = "order") order: String?,
    ) = seminarService.getSeminarListQueriedByNameAndOrder(name?:"", order?:"")

    @LogExecutionTime
    @PostMapping("/api/v1/seminar/{seminar_id}/user/")
    fun postUserToSeminar(
            @PathVariable seminar_id: Long,
            @Valid @RequestBody seminarRegisterRequest: SeminarRegisterRequest,
            bindingResult: BindingResult,
            @LoginUser meUser: UserEntity?,
    ): Any {
        meUser?.let{
            if (bindingResult.hasErrors()) {
                throw SeminarRequestBodyException(bindingResult.fieldErrors)
            }
            return seminarService.attendUserToSeminarAndReturnSeminarDetail(seminar_id, seminarRegisterRequest, meUser)
        } ?: return ResponseEntity<String>("Cannot found current user", HttpStatus.UNAUTHORIZED)
    }

    @LogExecutionTime
    @DeleteMapping("/api/v1/seminar/{seminar_id}/user")
    fun deleteUserFromSeminar(
            @PathVariable seminar_id: Long,
            @LoginUser meUser: UserEntity?,
    ): Any {
        meUser?.let{
            return seminarService.dropUserFromSeminar(seminar_id, meUser)
        } ?: return ResponseEntity<String>("Cannot found current user", HttpStatus.UNAUTHORIZED)
    }
}