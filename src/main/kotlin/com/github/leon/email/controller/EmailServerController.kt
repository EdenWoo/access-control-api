package com.github.leon.email.controller

import com.github.leon.aci.web.base.BaseController
import com.github.leon.email.domain.EmailServer
import com.github.leon.email.service.EmailLogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/v1/email-server")
class EmailServerController(

) : BaseController<EmailServer, Long>() {


    @GetMapping
    override fun page(pageable: Pageable, request: HttpServletRequest): ResponseEntity<Page<EmailServer>> {
        return super.page(pageable, request)
    }

    @GetMapping("{id}")
    override fun findOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<EmailServer> {
        return super.findOne(id, request)
    }

    @PostMapping
    override fun saveOne(@RequestBody input: EmailServer): ResponseEntity<*> {
        return super.saveOne(input)
    }

    @PutMapping("{id}")
    override fun updateOne(@PathVariable  id: Long, @RequestBody input: EmailServer, request: HttpServletRequest): ResponseEntity<*> {
        return super.updateOne(id, input, request)
    }

    @DeleteMapping
    override fun deleteOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        return super.deleteOne(id, request)
    }
}