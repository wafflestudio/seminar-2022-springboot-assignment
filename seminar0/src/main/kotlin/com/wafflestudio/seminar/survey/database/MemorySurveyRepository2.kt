package com.wafflestudio.seminar.survey.database

// import com.wafflestudio.seminar.survey.database.MemorySurveyRepository2
import org.springframework.stereotype.Repository
import org.springframework.util.ObjectUtils
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors
/*
@Repository
class MemorySurveyRepository2 {
    
    fun save(item: Item): Item {
        item.setId(++sequence)
        store[item.getId()] = item
        return item
    }

    fun update(itemId: Long, updateParam: ItemUpdateDto) {
        val findItem: Item = findById(itemId).orElseThrow()
        findItem.setItemName(updateParam.getItemName())
        findItem.setPrice(updateParam.getPrice())
        findItem.setQuantity(updateParam.getQuantity())
    }

    fun findById(id: Long): Optional<Item> {
        return Optional.ofNullable<Item>(store[id])
    }

    fun findAll(cond: ItemSearchCond): List<Item> {
        val itemName: String = cond.getItemName()
        val maxPrice: Int = cond.getMaxPrice()
        return store.values.stream()
            .filter(Predicate<Item> { item: Item ->
                if (ObjectUtils.isEmpty(itemName)) {
                    return@filter true
                }
                item.getItemName().contains(itemName)
            }).filter(Predicate<Item> { item: Item ->
                if (maxPrice == null) {
                    return@filter true
                }
                item.getPrice() <= maxPrice
            })
            .collect(Collectors.toList<Any>())
    }

    fun clearStore() {
        store.clear()
    }

    companion object {
        private val store: MutableMap<Long, Item> = HashMap<Long, Item>() //static
        private var sequence = 0L //static
    }
}*/