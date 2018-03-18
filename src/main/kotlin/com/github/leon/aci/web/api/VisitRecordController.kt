package com.github.leon.aci.web.api

import com.github.leon.aci.domain.VisitRecord
import com.github.leon.aci.service.VisitRecordService
import com.github.leon.aci.web.base.BaseController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(value = "/v1/visit-record")
class VisitRecordController : BaseController() {

    @Autowired
    private val visitRecordService: VisitRecordService? = null

    @GetMapping
    fun index(pageable: Pageable): ResponseEntity<Page<VisitRecord>> {
        return ResponseEntity.ok(visitRecordService!!.findAll(pageable))
    }
}