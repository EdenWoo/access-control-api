package com.github.leon.aci.web.api

import com.github.leon.aci.domain.VisitRecord
import com.github.leon.aci.web.base.BaseController
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping(value = ["/v1/visit-record"])
class VisitRecordController : BaseController<VisitRecord, Long>() {

    @GetMapping
    override fun page(pageable: Pageable, request: HttpServletRequest): ResponseEntity<Page<VisitRecord>> {
        return super.page(pageable, request)
    }

    @GetMapping("{id}")
    override fun findOne(id: Long, request: HttpServletRequest): ResponseEntity<VisitRecord> {
        return super.findOne(id, request)
    }

    @PostMapping
    override fun saveOne(input: VisitRecord): ResponseEntity<*> {
        return super.saveOne(input)
    }

    @PutMapping
    override fun updateOne(id: Long, input: VisitRecord, request: HttpServletRequest): ResponseEntity<*> {
        return super.updateOne(id, input, request)
    }

    @DeleteMapping("{id}")
    override fun deleteOne(id: Long, request: HttpServletRequest): ResponseEntity<*> {
        return super.deleteOne(id, request)
    }
}