package com.github.leon.setting.service

import com.github.leon.aci.service.base.BaseService
import com.github.leon.setting.domain.Setting
import org.springframework.stereotype.Service


@Service
class SettingService : BaseService<Setting, Long>()
