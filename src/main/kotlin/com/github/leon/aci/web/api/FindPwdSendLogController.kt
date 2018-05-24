package com.github.leon.aci.web.api

import com.github.leon.aci.dao.FindPwdSendLogDao
import com.github.leon.aci.dao.UserDao
import com.github.leon.aci.domain.FindPwdSendLog
import com.github.leon.aci.domain.User
import com.github.leon.aci.extenstions.responseEntityOk
import com.github.leon.aci.service.UserService
import com.github.leon.email.service.EmailLogService
import com.github.leon.encrypt.DESUtil
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
@RequestMapping("/v1/find-pwd-send-log")
class FindPwdSendLogController(
        @Autowired
        val findPwdSendLogDao: FindPwdSendLogDao,
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
    @GetMapping("/apply")
    @ResponseBody
    fun toReset(request: HttpServletRequest): ResponseEntity<FindPwdSendLog> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val log = FindPwdSendLog()
        log.email = user.email
        log.used = false
        findPwdSendLogDao.save(log)
        val encryptId = DESUtil.encrypt(log.id!!.toString() + "", "aaasssdd")
        log.encryptId = encryptId
        findPwdSendLogDao.save(log)
        val model = mapOf(
                "encryptId" to encryptId,
                "domain" to settingDao.findByActive(true)!!.serverDomain
        )

        userService.getEmails(user)
                .forEach {
                    emailLogService.sendSystem(
                            subject = "Reset password",
                            sendTo = it,
                            ftl = "/mail/findPwd.ftl",
                            model = model)
                }
        return log.responseEntityOk()

    }

    /**
     * 处理邮箱地址链接，跳到修改密码的页面
     */
    @GetMapping("{encryptId}")
    fun emailUrl(@PathVariable encryptId: String): ResponseEntity<Map<String, Any?>> {
        val decryptedId = DESUtil.decrypt(encryptId, "aaasssdd")
        val log = findPwdSendLogDao.findOne(decryptedId.toLong())
        val isExpired = ZonedDateTime.now().isAfter(log.expireDate)
        val map = mapOf(
                "isExpired" to isExpired,
                "used" to log.used,
                "encryptId" to encryptId)
        return map.responseEntityOk()
    }

    /**
     * 修改密码，不需要登录
     */
    @PostMapping("/reset")
    @ResponseBody
    fun resetPwd(encryptId: String, username: String, newPwd: String, confirmPassword: String): ResponseEntity<*> {
        val key = "aaasssdd"
        val decryptedId = DESUtil.decrypt(encryptId, key)
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val log = findPwdSendLogDao.findOne(decryptedId.toLong())
        if (newPwd != confirmPassword) {
            throw IllegalArgumentException("new password not equal")
        }
        user.setPassword(passwordEncoder.encode(newPwd))
        userDao.save(user)
        log.used = true
        findPwdSendLogDao.save(log)
        return "success".responseEntityOk()

    }

}
