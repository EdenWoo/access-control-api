package com.github.leon.sms.controller


import com.github.leon.aci.web.base.BaseController
import com.github.leon.sms.domain.MessageTemplate
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/v1/message-template")
class MessageTemplateController(

) : BaseController<MessageTemplate, Long>() {


}
