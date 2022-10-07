package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Component

@Component
class OsRepositoryImpl(private val memoryDB : MemoryDB): OsRepository {
    override fun findAll(): List<OperatingSystem> {
        return memoryDB.getOperatingSystems()
    }
    override fun findByName(name:String):OperatingSystem? {
            val list : List<OperatingSystem> =memoryDB.getOperatingSystems()
            list.forEach{
                if(it.osName == name){
                    return it
                }
            }
            return null
        }

    override fun findById(id: Long): OperatingSystem? {
        val list : List<OperatingSystem> =memoryDB.getOperatingSystems()
        list.forEach{
            if(it.id == id){
                return it
            }
        }
        return null
    }
}