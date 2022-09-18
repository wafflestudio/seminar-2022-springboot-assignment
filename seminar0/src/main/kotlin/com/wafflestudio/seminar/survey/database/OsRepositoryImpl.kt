package com.wafflestudio.seminar.survey.databaseimport com.wafflestudio.seminar.survey.domain.OperatingSystemimport com.wafflestudio.seminar.survey.domain.SurveyResponseimport org.springframework.stereotype.Component@Componentclass OsRepositoryImpl(val memoryDB: MemoryDB): OsRepository {    override fun findAll(): List<OperatingSystem>{        return memoryDB.getOperatingSystems()    }    override fun findById(id: Long): OperatingSystem?{        val os: List<OperatingSystem> = memoryDB.getOperatingSystems()        for(i in os){            if (i.id == id){                return i            }        }                return null    }    override fun findByName(name: String?): OperatingSystem? {        val os: List<OperatingSystem> = memoryDB.getOperatingSystems()        for(i in os){            if (i.osName == name){                return i            }        }                return null    }}