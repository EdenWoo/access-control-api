package com.github.leon.setting.controller

import com.github.leon.aci.extenstions.responseEntityOk
import com.github.leon.aci.web.base.BaseController
import com.github.leon.bean.JpaBeanUtil
import com.github.leon.email.dao.EmailLogDao
import com.github.leon.email.dao.EmailServerDao
import com.github.leon.setting.domain.Setting
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/v1/setting")
class SettingController(
        val emailServerDao: EmailServerDao
) : BaseController<Setting, Long>() {


    @GetMapping
    override fun page(pageable: Pageable, request: HttpServletRequest): ResponseEntity<Page<Setting>> {
        return super.page(pageable, request)
    }

    @GetMapping("{id}")
    override fun findOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<Setting> {
        return super.findOne(id, request)
    }

    @PostMapping
    override fun saveOne(@RequestBody input: Setting, request: HttpServletRequest): ResponseEntity<*> {
        return super.saveOne(input, request)
    }

    @PutMapping("{id}")
    override fun updateOne(@PathVariable id: Long, @RequestBody input: Setting, request: HttpServletRequest): ResponseEntity<*> {
        val persisted = baseService.findOne(id)
        val emailServer = emailServerDao.findOne(input.emailServer!!.id!!)
        JpaBeanUtil.copyNonNullProperties(input as Any, persisted as Any)
        persisted.emailServer = emailServer
        baseService.saveBySecurity(persisted, request.method, request.requestURI)

        return super.updateOne(id, input, request)
    }

    @DeleteMapping("{id}")
    override fun deleteOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        return super.deleteOne(id, request)
    }

    @GetMapping("active")
    fun findActive(): ResponseEntity<Setting> {
        val params = mutableMapOf<String, Array<String>>()
        params["f_active"] = arrayOf("true")
        params["f_active_op"] = arrayOf("=")
        return baseService.findByRequestParameters(params).first().responseEntityOk()
    }

    @PutMapping("active")
    fun active(id: Long): ResponseEntity<Setting> {
        val list = baseService.findAll()
        val (enableSetting, disabledSettings) = list.partition { it.id == id }
        enableSetting.forEach {
            it.active = true
            baseService.save(it)
        }
        disabledSettings.forEach {
            it.active = false
            baseService.save(it)
        }
        return enableSetting.first().responseEntityOk()

    }


}