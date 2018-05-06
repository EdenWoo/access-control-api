package com.cfgglobal.website.web.action

import com.github.leon.aci.dao.UserDao
import com.github.leon.aci.domain.FindPwdSendLog
import com.github.leon.aci.service.FindPwdLogService
import com.github.leon.email.service.EmailLogService
import com.github.leon.encrypt.DESUtil
import com.github.leon.fm.FreemarkerBuilderUtil
import com.github.leon.setting.dao.SettingDao
import com.google.common.collect.Maps
import org.apache.commons.lang3.math.NumberUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest


@Controller
@RequestMapping("/find-pwd")
class FindPasswordController(
        @Autowired
        val findPwdLogService: FindPwdLogService,
        @Autowired
        val userDao: UserDao,
        @Autowired
        val freemarkerBuilderUtil: FreemarkerBuilderUtil,
        @Autowired
        val passwordEncoder: BCryptPasswordEncoder,
        @Autowired
        val settingDao: SettingDao,
        @Autowired
        val emailLogService: EmailLogService

) {

    /**
     * 输入邮箱获取修改密码链接,发送邮件并且返回页面
     */
    @RequestMapping(value = ["/to-reset"], method = arrayOf(RequestMethod.GET, RequestMethod.POST))
    @ResponseBody
    fun toReset(username: String, request: HttpServletRequest): ResponseEntity<*> {
        var type = true
        val user = userDao.findByUsername(username)

        if (user == null) {
            type = false
        }
        if (!type) {
            val map = Maps.newHashMap<String, Any>()
            map["error"] = "email doesn't exist"
            return ResponseEntity.badRequest().body<Map<String, Any>>(map)
        }
        val log = FindPwdSendLog()
        log.email = user.email
        log.used = false
        // log.ip = IpUtil.getIpAddr(request)
        findPwdLogService.insert(log)
        val encryptId = DESUtil.encrypt(log.id!!.toString() + "", "aaasssdd")

        val model = Maps.newHashMap<String, Any>()
        model["domain"] = settingDao.findByActive(true).serverDomain
        model["id"] = encryptId
        //model["id"] = log.id
        val content = freemarkerBuilderUtil.build("/mail/findPwd.ftl", model)
        //emailLogService.sendSystem()
        // webMailService!!.sendComplexMessage(user.merchantBase.email, "Reset password", content, true)
        return ResponseEntity.ok().build<Any>()

    }

    /**
     * 处理邮箱地址链接，跳到修改密码的页面
     *
     * @param id
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = ["/link-in-email"], method = [(RequestMethod.GET), (RequestMethod.POST)])
    fun emailUrl(id: String, model: ModelMap): String {
        var id = id
        //解密id，判断时间是否过期
        id = DESUtil.decrypt(id, "aaasssdd")
        val log = findPwdLogService.getLogById(NumberUtils.createLong(id))
        val email = log.email
        val date = log.expireDate
        val expiredDate = Date(date!!)
        val calendar = Calendar.getInstance()
        val now = calendar.time
        val isExpired = if (now.after(expiredDate)) 1 else 0
        model.addAttribute("isExpired", isExpired)
        model.addAttribute("used", log.used)
        model.addAttribute("id", id)
        model.addAttribute("email", email)
        return "/findPassword/newPassword"
    }

    /**
     * 重置密码，不需要登录
     */
    @RequestMapping(value = ["/reset"], method = [(RequestMethod.GET), (RequestMethod.POST)])
    @ResponseBody
    fun resetPwd(id: Long, username: String, newPwd: String, confirmPassword: String): ResponseEntity<*> {
        val user = userDao.findByUsername(username)
        val log = findPwdLogService.getLogById(id)
        //对新密码进行验证
        if (newPwd == confirmPassword) {
            user.password = passwordEncoder.encode(newPwd)
            userDao.save(user)
        } else {
            return ResponseEntity.badRequest().build<Any>()
        }
        log.used = true
        findPwdLogService.update(log)
        return ResponseEntity.ok().build<Any>()

    }

}
