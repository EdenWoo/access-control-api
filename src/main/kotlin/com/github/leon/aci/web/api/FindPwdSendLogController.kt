package com.github.leon.aci.web.api

import com.github.leon.aci.dao.UserDao
import com.github.leon.aci.domain.FindPwdSendLog
import com.github.leon.aci.domain.User
import com.github.leon.aci.extenstions.responseEntityOk
import com.github.leon.aci.service.FindPwdLogService
import com.github.leon.aci.service.UserService
import com.github.leon.email.service.EmailLogService
import com.github.leon.encrypt.DESUtil
import com.github.leon.fm.FreemarkerBuilderUtil
import com.github.leon.setting.dao.SettingDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime
import javax.servlet.http.HttpServletRequest


@Controller
@RequestMapping("/find-pwd-send-log")
class FindPwdSendLogController(
        @Autowired
        val findPwdLogService: FindPwdLogService,
        @Autowired
        val userDao: UserDao,
        @Autowired
        val passwordEncoder: BCryptPasswordEncoder,
        @Autowired
        val settingDao: SettingDao,
        @Autowired
        val userService: UserService,
        @Autowired
        val emailLogService: EmailLogService

) {

    /**
     * 输入邮箱获取修改密码链接,发送邮件并且返回页面
     */
    @GetMapping("/to-reset")
    @ResponseBody
    fun toReset(username: String, request: HttpServletRequest): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val log = FindPwdSendLog()
        log.email = user.email
        log.used = false
        findPwdLogService.insert(log)
        val encryptId = DESUtil.encrypt(log.id!!.toString() + "", "aaasssdd")

        val model = mapOf(
                "id" to encryptId,
                "domain" to settingDao.findByActive(true).serverDomain
        )

        userService.getEmails(user)
                .forEach {
                    emailLogService.sendSystem("Reset password", it, "/mail/findPwd.ftl", model)
                }
        return ResponseEntity.ok().build<Any>()

    }

    /**
     * 处理邮箱地址链接，跳到修改密码的页面
     */
    @GetMapping("{id}")
    fun emailUrl(@PathVariable id: String): ResponseEntity<Map<String, Any?>> {
        val decryptedId = DESUtil.decrypt(id, "aaasssdd")
        val log = findPwdLogService.getLogById(decryptedId.toLong())
        val isExpired = ZonedDateTime.now().isAfter(log.expireDate)
        val map = mapOf(
                "isExpired" to isExpired,
                "used" to log.used,
                "id" to id)
        return map.responseEntityOk()
    }

    /**
     * 修改密码，不需要登录
     */
    @PostMapping("/reset")
    @ResponseBody
    fun resetPwd(id: String, username: String, newPwd: String, confirmPassword: String): ResponseEntity<*> {
        val decryptedId = DESUtil.decrypt(id, "aaasssdd")
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val log = findPwdLogService.getLogById(decryptedId.toLong())
        if (newPwd != confirmPassword) {
            throw IllegalArgumentException("new password not equal")
        }
        user.setPassword(passwordEncoder.encode(newPwd))
        userDao.save(user)
        log.used = true
        findPwdLogService.update(log)
        return "success".responseEntityOk()

    }

}
