package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.seminar.api.request.*
import com.wafflestudio.seminar.core.seminar.domain.SeminarForList
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class SeminarController(
    private val service: SeminarService
) {
    @Authenticated
    @PostMapping("/api/v1/seminar")
    fun seminarCreate(
        @RequestHeader("Authorization") authToken: String,
        @RequestBody createSeminarRequest: CreateSeminarRequest
    ) = service.createSeminar(authToken, createSeminarRequest)

    @Authenticated
    @PutMapping("/api/v1/seminar")
    fun seminarModify(
        @RequestHeader("Authorization") authToken: String,
        @RequestBody modifySeminarRequest: ModifySeminarRequest
    ) = service.modifySeminar(authToken, modifySeminarRequest)

    @Authenticated
    @GetMapping("/api/v1/seminar")
    fun allSeminarRead(
        @RequestHeader("Authorization") authToken: String,
        @RequestParam("name") seminarName: String?,
        @RequestParam("order") order: String?,
        @RequestParam("page", required = false, defaultValue = "1") page: Int?
    ): List<SeminarForList> {
        return if (order == "earliest") {
            val pageable = PageRequest.of(page!! - 1, 50, Sort.by("createdAt").descending())
            service.getAllSeminar(seminarName, order, pageable)
        } else {
            val pageable = PageRequest.of(page!! -1, 50)
            service.getAllSeminar(seminarName, order, pageable)
        }
    }

    @Authenticated
    @GetMapping("/api/v1/seminar/{seminar_id}")
    fun seminarRead(
        @RequestHeader("Authorization") authToken: String,
        @PathVariable("seminar_id") seminarId: Long
    ) = ResponseEntity.ok().body(service.readSeminar(seminarId))

    @Authenticated
    @PostMapping("/api/v1/seminar/{seminar_id}/user")
    fun seminarApply(
        @RequestHeader("Authorization") authToken: String,
        @PathVariable("seminar_id") seminarId: Long,
        @RequestBody applySeminarRequest: ApplySeminarRequest
    ) = service.applySeminar(authToken, seminarId, applySeminarRequest)

    @Authenticated
    @DeleteMapping("/api/v1/seminar/{seminar_id}/user")
    fun seminarGiveUp(
        @RequestHeader("Authorization") authToken: String,
        @PathVariable("seminar_id") seminarId: Long
    ) = service.deleteParticipantFromSeminar(authToken, seminarId)
}