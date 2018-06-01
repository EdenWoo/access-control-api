package com.github.leon.aci.extenstions

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

fun <T> List<T>.pseudoPagination(pageable: Pageable): Page<T> {
    val pageNumber = pageable.pageNumber
    val pageSize = pageable.pageSize
    return PageImpl(
            when (count()) {
                0 -> listOf()
                else -> subList(Integer.min(pageNumber * pageSize, count() - 1),
                        Integer.min(count(), pageNumber * pageSize + pageSize))
            }, PageRequest(pageNumber, pageSize), count().toLong())
}
