package com.github.leon.aci.web.api

import com.github.leon.aci.dao.FindPwdSendLogDao
import com.github.leon.aci.dao.UserDao
import com.github.leon.aci.domain.FindPwdSendLog
import com.github.leon.aci.extenstions.responseEntityOk
import com.github.leon.aci.service.UserService
import com.github.leon.cache.CacheClient
import com.github.leon.email.service.EmailLogService
import com.github.leon.encrypt.DESUtil
import com.github.leon.setting.dao.SettingDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime


@Controller
@RequestMapping("/v1/find-pwd-send-log")
class FindPwdSendLogController(
        @Value("\${spring.application.name}")
        val application: String,
        @Autowired
        val cacheClient: CacheClient,
        @Autowired
        val findPwdSendLogDao: FindPwdSendLogDao,
        @Autowired
        val userDao: UserDao,
        @Autowired
        val passwordEncoder: PasswordEncoder,
        @Autowired
        val settingDao: SettingDao,
        @Autowired
        val userService: UserService,
        @Autowired
        val emailLogService: EmailLogService

) {
    @PostMapping("apply")
    @ResponseBody
    fun apply(username: String): ResponseEntity<FindPwdSendLog> {
        val setting = settingDao.findByActive(true)!!
        val user = userDao.findByUsername(username)
                ?: throw IllegalArgumentException("username ($username) doesn't exist.")
        val log = FindPwdSendLog(
                username = username,
                email = user.email,
                used = false,
                expireDate = ZonedDateTime.now().plusDays(1)
        )
        findPwdSendLogDao.save(log)
        val encryptId = DESUtil.encrypt(log.id!!.toString() + "", KEY)
        log.encryptId = encryptId
        findPwdSendLogDao.save(log)
        val model = mapOf(
                "encryptId" to encryptId,
                "setting" to setting
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


    @GetMapping("{encryptId}")
    fun getByEncryptId(@PathVariable encryptId: String): ResponseEntity<Map<String, Any?>> {
        val decryptedId = DESUtil.decrypt(encryptId, KEY)
        val log = findPwdSendLogDao.findOne(decryptedId.toLong())
        val isExpired = ZonedDateTime.now().isAfter(log.expireDate)
        val map = mapOf(
                "expired" to isExpired,
                "used" to log.used,
                "encryptId" to encryptId)
        return map.responseEntityOk()
    }


    @PostMapping("reset/{encryptId}")
    @ResponseBody
    fun resetPwd(@PathVariable encryptId: String, newPassword: String, confirmPassword: String): ResponseEntity<*> {
        val decryptedId = DESUtil.decrypt(encryptId, KEY)
        val log = findPwdSendLogDao.findOne(decryptedId.toLong())
        val username = log.username!!
        val user = userDao.findByUsername(username)
                ?: throw IllegalArgumentException("username $username doesn't exist.")

        if (newPassword != confirmPassword) {
            throw IllegalArgumentException("new password doesn't equal")
        }

        user.setPassword(passwordEncoder.encode(newPassword))
        userDao.save(user)
        log.used = true
        findPwdSendLogDao.save(log)
        cacheClient.deleteByKey(application + "-" + user.username)

        return "success".responseEntityOk()

    }

    companion object {
        const val KEY = "aaasssdd"
    }

}
