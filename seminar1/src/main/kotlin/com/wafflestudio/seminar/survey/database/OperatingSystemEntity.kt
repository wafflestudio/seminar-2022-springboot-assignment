package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["osName"])])
class OperatingSystemEntity(
    val osName: String,
    val price: Long,
    val desc: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    fun toOperatingSystem(): OperatingSystem {
        return OperatingSystem(
            this.id,
            this.osName,
            this.price,
            this.desc
        )
    }
}