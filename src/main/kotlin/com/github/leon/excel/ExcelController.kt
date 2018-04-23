package com.github.leon.excel

import com.github.leon.aci.domain.BaseEntity
import com.github.leon.aci.security.ApplicationProperties
import com.github.leon.excel.service.ExcelParsingRule
import com.github.leon.aci.web.base.BaseController
import com.github.leon.files.PoiExporter
import com.github.leon.files.PoiImporter
import org.joor.Reflect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.Instant
import javax.servlet.http.HttpServletResponse
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/v1/excel")

class ExcelController {

    @Autowired
    var excelParsingRules: MutableList<ExcelParsingRule<*>> = mutableListOf()

    @PostMapping
    fun submit(file: MultipartFile, rule: String) {
        val excelParsingRule = excelParsingRules.first { it.ruleName == rule }
        val fileName = "/tmp/" + Instant.now().epochSecond
        val tmpFile = File(fileName)
        file.transferTo(tmpFile)
        val fileParser = excelParsingRule.fileParser
        excelParsingRule.process(PoiImporter.processSheet(tmpFile, fileParser, excelParsingRule.entityClass))
    }

    @GetMapping("template")
    fun findOne(rule: String, response: HttpServletResponse): ResponseEntity<*> {
        val entity = ApplicationProperties.enumPackages.first()+".${rule.capitalize()}"
        val clazz = Reflect.on(entity).get() as Class<out BaseEntity>
        val fields = clazz.declaredFields.filter { it.getDeclaredAnnotation(NotNull::class.java) != null }.map { it.name }
        PoiExporter.data(fields)
                .headers(fields)
                .columns(fields)
                .render(response)
        return ResponseEntity.ok("success")
    }
}