package com.github.leon.aci.web.api

import com.github.leon.aci.domain.Branch
import com.github.leon.aci.service.BranchService
import com.github.leon.aci.web.base.BaseController
import com.github.leon.bean.JpaBeanUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["/v1/branch"])

class BranchController : BaseController() {

    @Autowired
    private val branchService: BranchService? = null

    @GetMapping
    fun page(pageable: Pageable, request: HttpServletRequest): ResponseEntity<*> {
        val page = branchService!!.findBySecurity(request.method, request.requestURI, request.parameterMap, pageable)
        return ResponseEntity.ok(page)
    }

    @GetMapping("{id}")
    operator fun get(@PathVariable id: Long): ResponseEntity<Branch> {
        return ResponseEntity.ok(branchService!!.findOne(id))
    }

    @PostMapping
    fun save(@RequestBody branch: Branch): ResponseEntity<Branch> {
        return ResponseEntity.ok(branchService!!.save(branch))
    }


    @PutMapping("{id}")
    fun save(@PathVariable id: Long, @RequestBody branch: Branch): ResponseEntity<*> {
        val oldBranch = branchService!!.findOne(id)
        JpaBeanUtil.copyNonNullProperties(branch, oldBranch)
        return ResponseEntity.ok(branchService.save(oldBranch))
    }

    @DeleteMapping("delete")
    fun delete(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        branchService!!.deleteBySecurity(id, request.method, request.requestURI)
        return ResponseEntity.noContent().build<Any>()
    }

}