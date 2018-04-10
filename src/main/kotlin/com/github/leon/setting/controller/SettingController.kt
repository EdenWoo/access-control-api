package com.github.leon.setting.controller

import com.github.leon.aci.web.base.BaseController
import com.github.leon.setting.domain.Setting
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/setting")
class SettingController : BaseController<Setting, Long>() {
}