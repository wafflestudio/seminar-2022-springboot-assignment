package com.wafflestudio.seminar.core.seminar.database


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

interface SeminarRepository : JpaRepository<SeminarEntity, Long> {
    fun findByManagerId(managerId: Long): SeminarEntity?
}

interface SeminarRepositorySupport {

}

@Component
class SeminarRepositorySupportImpl(

) : SeminarRepositorySupport {


}