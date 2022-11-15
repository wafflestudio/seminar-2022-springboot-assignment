package com.wafflestudio.seminar.core.seminar.repository

import com.wafflestudio.seminar.core.mappingTable.UserSeminar
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface UserSeminarRepository: JpaRepository<UserSeminar, Long> {
    
    @Query("select us from UserSeminar us join fetch us.userEntity u join fetch us.seminar s where u.id = :userId")
    fun findByUserEntityId(userId: Long): List<UserSeminar>

    @Query("select us from UserSeminar us join fetch us.userEntity u join fetch us.seminar s where u.id = :userId and s.id = :seminarId")
    fun findByUserEntityIdAndSeminarId(userId: Long, seminarId: Long): Optional<UserSeminar>
    
}