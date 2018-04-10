package com.github.leon.sms.controller


import com.github.leon.aci.enums.TaskStatus
import com.github.leon.aci.util.handleStatus
import com.github.leon.bean.JpaBeanUtil
import com.github.leon.sms.dao.MessageLogDao
import com.github.leon.sms.domain.MessageLog
import com.github.leon.sms.service.MessageLogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/v1/message-log")
class MessageLogController(
        @Autowired
        val messageLogService: MessageLogService,
        @Autowired
        val messageLogDao: MessageLogDao
) {

    @GetMapping
    fun page(pageable: Pageable): ResponseEntity<Page<MessageLog>> {
        return ResponseEntity.ok(messageLogDao.findAll(pageable))
    }


    @GetMapping("{id}")
    fun get(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<MessageLog> {
        return ResponseEntity.ok(messageLogService.findOneBySecurity(id, request.method, request.requestURI))
    }

    @PostMapping
    fun save(@RequestBody messageLog: MessageLog): ResponseEntity<*> {
        messageLogService.save(messageLog)
        return ResponseEntity.ok(messageLog)
    }

    @PutMapping("{id}")
    fun save(@PathVariable id: Long, @RequestBody messageLog: MessageLog): ResponseEntity<*> {
        val oldMessageLog = messageLogService.findOne(id)
        JpaBeanUtil.copyNonNullProperties(messageLog, oldMessageLog)
        messageLogService.save(oldMessageLog)
        return ResponseEntity.ok(messageLog)
    }

    @PutMapping("resend")
    fun resend(ids: String): ResponseEntity<*> {
        handleStatus(ids) { id ->
            val messageLog = messageLogService.findOne(id)
            messageLog.status = TaskStatus.TODO
            messageLogService.save(messageLog)

        }
        return ResponseEntity.ok(ids)
    }

}
