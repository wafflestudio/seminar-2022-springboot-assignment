package com.wafflestudio.seminar.user.database


import org.springframework.data.jpa.repository.JpaRepository


// https://spring.io/guides/gs/accessing-data-jpa/
// 참조


interface UserRepository: JpaRepository<UserEntity, Long> {
    //  fun getUser(id:Long):UserEntity? 
    // 위 코드는 에러
    fun findByEmail(email:String):UserEntity?
}