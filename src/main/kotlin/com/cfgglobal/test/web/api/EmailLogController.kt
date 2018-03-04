package com.cfgglobal.test.web.api

import com.cfgglobal.test.enums.TaskStatus
import com.cfgglobal.test.service.EmailLogService
import com.cfgglobal.test.util.handleStatus
import com.cfgglobal.test.web.api.vo.EmailLogVo
import com.cfgglobal.test.web.base.BaseController
import com.github.leon.bean.JpaBeanUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/v1/email-log")
class EmailLogController(
        @Autowired
        val emailLogService: EmailLogService

) : BaseController() {


    @GetMapping
    fun page(pageable: Pageable, request: HttpServletRequest): ResponseEntity<*> {
        return ResponseEntity.ok(emailLogService.findByRequestParameters(request.parameterMap, pageable))
    }


    @GetMapping("{id}")
    operator fun get(@PathVariable id: Long): ResponseEntity<EmailLogVo> {
        var email = emailLogService.findOne(id)
        var vo = EmailLogVo()
        JpaBeanUtil.copyNonNullProperties(email, vo)
        return ResponseEntity.ok(vo.copy(html = String(vo.content!!)))
    }

    @PutMapping("resend")
    fun resend(ids: String): ResponseEntity<*> {
        handleStatus(ids) { id: Long ->
            val emailLog = emailLogService.findOne(id).get().copy(status = TaskStatus.TODO)
            emailLogService.save(emailLog)
        }
        return ResponseEntity.ok(ids)
    }

}