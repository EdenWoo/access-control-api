package com.github.leon.aci.service

import com.github.leon.aci.domain.Branch
import com.github.leon.aci.service.base.BaseService
import org.springframework.stereotype.Service

@Service
class BranchService : BaseService<Branch, Long>()
