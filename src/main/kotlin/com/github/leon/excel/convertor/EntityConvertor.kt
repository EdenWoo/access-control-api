package com.github.leon.excel.convertor

import com.github.leon.files.convert.CellConverter
import javax.persistence.EntityManager

class EntityConvertor : CellConverter {
    lateinit var em: EntityManager
    lateinit var name: String
    override fun convert(value: String, obj: Any): Any {
        //xx-id
        val (id, _) = value.split("-")
        println("id $id")

        val hql = "SELECT e from $name e where e.id = :id"
        val entity = em.createQuery(hql).setParameter("id", id.toLong()).singleResult
        return entity
    }
}