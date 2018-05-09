package ${project.packageName}.controller.base

import ${project.packageName}.controller.base.Base${entity.name}Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ${project.packageName}.service.${entity.name}Service
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


abstract class Base${entity.name}Controller(

) : BaseController<${entity.name}, Long>() {

    @GetMapping
    override fun page(pageable: Pageable, request: HttpServletRequest): ResponseEntity<Page<${entity.name}>> {
        return ResponseEntity.ok(baseService.findByRequestParameters(request.parameterMap, pageable))
    }

    @GetMapping("{id}")
    override fun findOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<${entity.name}> {
        return ResponseEntity.ok(baseService.findOne(id))
    }

    @PostMapping
    override fun saveOne(@Validated @RequestBody input: ${entity.name}): ResponseEntity<*> {
        return super.saveOne(input)
    }

    @PutMapping("{id}")
    override fun updateOne(@PathVariable id: Long, @Validated @RequestBody input: ${entity.name}, request: HttpServletRequest): ResponseEntity<*> {
        return super.updateOne(id, input, request)
    }

    @DeleteMapping("{id}")
    override fun deleteOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        return super.deleteOne(id, request)
    }

    @GetMapping("excel")
    fun excel(pageable: Pageable, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Page<${entity.name}>> {
        val page = baseService.findBySecurity("GET", "/v1/${Utils.lowerHyphen(entity.name)}", request.parameterMap, pageable)
        val headers:List<String> = listOf(<#list entity.requiredFields as f>"${f.name}" <#if f_has_next>,</#if></#list>)
        PoiExporter.data(page.content)
                 .sheetNames("${entity.name?uncap_first}")
                 .headers(headers)
                 .columns(headers)
                 .render(response)

        return ResponseEntity.ok(page)
    }

}