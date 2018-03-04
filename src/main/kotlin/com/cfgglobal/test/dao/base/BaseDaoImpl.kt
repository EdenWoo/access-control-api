package com.cfgglobal.test.dao.base


import arrow.core.Option
import arrow.data.Try
import arrow.syntax.collections.firstOption
import com.cfgglobal.test.vo.Condition
import com.cfgglobal.test.vo.Filter
import com.github.leon.security.ApplicationProperties
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils
import org.joor.Reflect
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.criteria.*

class BaseDaoImpl<T, ID : Serializable>(
        val myDomainClass: Class<T>,
        val entityManager: EntityManager
) : SimpleJpaRepository<T, ID>(myDomainClass, entityManager), BaseDao<T, ID> {

    val log = LoggerFactory.getLogger(BaseDaoImpl::class.java)!!
    private fun str2Enum(str: String): Option<Enum<*>> {
        return ApplicationProperties.enums
                .map { e -> getaClass(e) }
                .map { e -> Try { Reflect.on(e).call("valueOf", str).get<Any>() } }
                .firstOption({ it.isSuccess() })
                .map { t -> t.get() as Enum<*> }
    }

    private fun getaClass(str: String): Class<out Enum<*>> {
        return ApplicationProperties.enumPackages
                .map { p -> Try { Reflect.on(p + "." + str).get<Any>() as Class<out Enum<*>> } }
                .firstOption { it.isSuccess() }.get().get()

    }


    override fun findByFilter(filters: List<Filter>, pageable: Pageable): Page<T> {
        val spec = Specification { root: Root<T>, query: CriteriaQuery<*>, cb: CriteriaBuilder ->
            val predicates = getPredicates(filters, root, cb)
            query.where(*predicates.toTypedArray())
            query.restriction
        }
        return findAll(spec, pageable)
    }

    override fun findByFilter(filters: List<Filter>): List<T> {
        log.debug(filters.toString())
        val spec = Specification { root: Root<T>, query: CriteriaQuery<*>, cb: CriteriaBuilder ->
            val predicates = getPredicates(filters, root, cb)
            query.where(*predicates.toTypedArray())
            query.restriction
        }
        return findAll(spec)
    }

    override fun findByFilter(filter: Filter): List<T> {
        return findByFilter(listOf(filter))
    }


    override fun findAll(spec: Specification<T>, pageable: Pageable, entityGraphName: String): Page<T> {
        val query = getQuery(spec, pageable.sort)
        val entityGraph = entityManager.getEntityGraph(entityGraphName)
        query.setHint(entityGraph.name, entityGraph)
        return readPage(query, myDomainClass, pageable, spec)
    }


    override fun findByRequestParameters(info: Map<String, Array<String>>, pageable: Pageable): Page<T> {
        return findByFilter(Filter.createFilters(info), pageable)
    }

    override fun findByRequestParameters(info: Map<String, Array<String>>): List<T> {
        return findByFilter(Filter.createFilters(info))
    }


    private fun getPredicates(filters: List<Filter>, root: Root<T>, cb: CriteriaBuilder): List<Predicate?> {
        return filters
                .filter { it -> it !== Filter.EMPTY }
                .map { filter ->
                    val predicates = filter.conditions
                            .filter { condition ->
                                val `val` = condition.value.toString()
                                if (condition.operator == Filter.OPERATOR_BETWEEN) {
                                    val arr = condition.value as Array<String>
                                    val o1 = arr[0]
                                    val o2 = arr[1]
                                    return@filter StringUtils.isNoneBlank(o1) && StringUtils.isNoneBlank(o2.toString())
                                } else if (StringUtils.isBlank(`val`)) {
                                    return@filter false
                                }
                                true
                            }
                            .map { condition ->
                                val searchPath: Path<Any>
                                val fieldName = condition.fieldName
                                val fields = fieldName!!.split(".")
                                if (fields.size > 1) {
                                    var join: Join<Any, Any> = root.join<Any, Any>(fields.get(0))
                                    for (i in 1 until fields.size - 1) {
                                        join = join.join(fields.get(i))
                                    }
                                    searchPath = join.get(fields.get(fields.size - 1))
                                } else {
                                    searchPath = root.get<Any>(fieldName)
                                }
                                val predicate = getPredicate(cb, condition, searchPath)
                                if (predicate == null) {
                                    null
                                }

                                if (condition.relation == Filter.RELATION_AND) {

                                    cb.and(predicate)
                                } else {
                                    cb.or(predicate)
                                }
                            }
                    if (predicates.isEmpty())
                        null
                    else
                        predicates
                                .reduce({ l, r ->
                                    if (r.operator == Predicate.BooleanOperator.AND) {
                                        cb.and(l, r)
                                    } else {
                                        cb.or(l, r)
                                    }
                                })
                }.filter { it != null }
    }

    private fun getPredicate(cb: CriteriaBuilder, condition: Condition, searchPath: Path<*>): Predicate? {
        var predicate: Predicate?
        val value = condition.value
        val s = value.toString()
        if (isEnum(s) && condition.operator.toUpperCase() != "NULL" && condition.operator.toUpperCase() != "NOTNULL") {
            condition.operator = Filter.OPERATOR_EQ
        }
        when (condition.operator.toUpperCase()) {
            Filter.OPERATOR_NULL -> predicate = cb.isNull(searchPath)
            Filter.OPERATOR_LIKE -> predicate = cb.like(searchPath as Path<String>, "%$s%")
            Filter.OPERATOR_LESS_EQ -> predicate = cb.lessThan<String>(searchPath as Path<String>, s)
            Filter.OPERATOR_BETWEEN -> {
                val arr = condition.value as Array<String>
                val o1 = arr[0]
                val o2 = arr[1]


                var from = o1
                var to = o2

                if (condition.fieldName.equals("verificationDate") || condition.fieldName.equals("incorporatedDate")) { //
                    predicate = cb.between<String>(searchPath as Path<String>, from, to)

                } else if (NumberUtils.isCreatable(from)) {
                    predicate = cb.between<Int>(searchPath as Path<Int>, NumberUtils.toInt(from), NumberUtils.toInt(to))
                } else {
                    if (from.length == "1970-01-01".length) {
                        from += " 00:00:00"
                    }
                    if (to.length == "1970-01-01".length) {
                        to += " 23:59:59"
                    }
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val fromDateTime = LocalDateTime.parse(from, formatter).atZone(ZoneId.systemDefault())
                    val toDateTime = LocalDateTime.parse(to, formatter).atZone(ZoneId.systemDefault())
                    predicate = cb.between(searchPath as Path<ZonedDateTime>, fromDateTime, toDateTime)


                }
            }
            Filter.OPERATOR_IN -> {
                val list: Array<Any> = when (value) {
                    is String -> arrayOf(value)
                    else -> value as Array<Any>
                }

                if (isEnum(list[0].toString())) {
                    predicate = searchPath.`in`(list.map { en -> str2Enum(en.toString()).get() })
                } else {
                    predicate = searchPath.`in`(list)
                }
            }
            else -> predicate = when {
                s == "true" || s == "false" -> cb.equal(searchPath, java.lang.Boolean.valueOf(s))
                isEnum(s) -> cb.equal(searchPath, str2Enum(s).get())
                NumberUtils.isCreatable(s) -> cb.equal(searchPath, NumberUtils.toLong(s))
                s.contains("-") -> cb.equal(searchPath, LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                else -> cb.equal(searchPath, s)
            }
        }
        return predicate
    }

    private fun isEnum(cs: String): Boolean {
        return str2Enum(cs).isDefined()
    }

    override fun support(modelType: String): Boolean {
        return myDomainClass.name == modelType
    }



}