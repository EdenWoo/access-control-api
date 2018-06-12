package com.github.leon.backup.service

import com.github.leon.aci.service.base.BaseService
import com.github.leon.backup.domain.DbSnapshot
import org.springframework.stereotype.Service


@Service
class DbSnapshotService : BaseService<DbSnapshot, Long>()
