package com.github.leon.backup.service

import com.github.leon.aci.service.base.BaseService
import com.github.leon.backup.domain.DbSnapshort
import org.springframework.stereotype.Service


@Service
class DbSnapshortService : BaseService<DbSnapshort, Long>()
