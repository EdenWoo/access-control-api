package ${project.packageName}.controller.base

import ${project.packageName}.controller.base.Base${entity.name}Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ${project.packageName}.entity.${entity.name}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.github.leon.aci.web.base.BaseController
import com.github.leon.files.PoiExporter
import com.github.leon.bean.JpaBeanUtil


abstract class Base${entity.name}Controller(

) : BaseController<${entity.name}, Long>() {

    @GetMapping
    override fun page(pageable: Pageable, request: HttpServletRequest): ResponseEntity<Page<${entity.name}>> {
        <#if entity.security >
        return super.page(pageable, request)
        <#else>
        return ResponseEntity.ok(baseService.findByRequestParameters(request.parameterMap, pageable))
        </#if>
    }

    @GetMapping("{id}")
    override fun findOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<${entity.name}> {
        <#if entity.security >
        return super.findOne(id, request)
        <#else>
        return ResponseEntity.ok(baseService.findOne(id))
        </#if>
    }

    @PostMapping
    override fun saveOne(@Validated @RequestBody input: ${entity.name}, request: HttpServletRequest): ResponseEntity<*> {
        <#if entity.security >
        return super.saveOne(input, request)
        <#else>
        return ResponseEntity.ok(baseService.save(input))
        </#if>
    }

    @PutMapping("{id}")
    override fun updateOne(@PathVariable id: Long, @Validated @RequestBody input: ${entity.name}, request: HttpServletRequest): ResponseEntity<*> {
        <#if entity.security >
        return super.updateOne(id, input, request)
        <#else>
        val persisted = baseService.findOne(id)
        JpaBeanUtil.copyNonNullProperties(input as Any, persisted as Any)
        baseService.save(persisted)
        return ResponseEntity.ok(persisted)
        </#if>

    }

    @DeleteMapping("{id}")
    override fun deleteOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        <#if entity.security >
        return super.deleteOne(id, request)
        <#else>
        baseService.delete(id)
        return ResponseEntity.noContent().build<Any>()
        </#if>
    }

    @GetMapping("excel")
    fun excel(pageable: Pageable, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Page<${entity.name}>> {
        <#if entity.security >
        val page = baseService.findBySecurity(request.method, request.requestURI, request.parameterMap, pageable)
        <#else>
        val page = baseService.findByRequestParameters( request.parameterMap, pageable)
        </#if>
        val headers:List<String> = ${entity.headerListStr}
        val columns:List<String> = ${entity.columnListStr}
        PoiExporter.data(page.content)
                 .sheetNames("${entity.name?uncap_first}")
                 .headers(headers)
                 .columns(columns)
                 .render(response)

        return ResponseEntity.ok(page)
    }

}