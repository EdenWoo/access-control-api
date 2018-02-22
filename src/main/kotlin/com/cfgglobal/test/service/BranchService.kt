package com.cfgglobal.test.service

import com.cfgglobal.test.domain.Branch
import com.cfgglobal.test.service.base.BaseService
import org.springframework.stereotype.Service

@Service
class BranchService : BaseService<Branch, Long>()
