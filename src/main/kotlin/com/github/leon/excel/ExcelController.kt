package com.github.leon.excel

import com.github.leon.aci.domain.BaseEntity
import com.github.leon.aci.security.ApplicationProperties
import com.github.leon.excel.service.ExcelParsingRule
import com.github.leon.aci.web.base.BaseController
import com.github.leon.files.PoiExporter
import com.github.leon.files.PoiImporter
import org.apache.poi.hssf.usermodel.DVConstraint
import org.apache.poi.hssf.usermodel.HSSFDataValidation
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.util.CellRangeAddressList
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
import javax.persistence.EntityManager
import javax.servlet.http.HttpServletResponse
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/v1/excel")

class ExcelController(
        @Autowired
        val entityManager: EntityManager
) {

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
    fun findOne(rule: String, response: HttpServletResponse) {
        val entity = ApplicationProperties.entityScanPackages.last()+".${rule.capitalize()}"
        val clazz = Reflect.on(entity).get() as Class<out BaseEntity>
        val fields = clazz.declaredFields.filter { it.getDeclaredAnnotation(NotNull::class.java) != null }

        val wb = HSSFWorkbook()
        val sheet = wb.createSheet("template sheet")
        val row = sheet.createRow(0)

        fields.forEachIndexed { index, s ->
            val cell = row.createCell(index)
            when(s.type.superclass){
                BaseEntity::class.java->{
                    val hql = "SELECT e from ${s.type.simpleName} e"
                    val list = entityManager.createQuery(hql).resultList.map { Reflect.on(it).get<Long>("id").toString()+"-"+Reflect.on(it).get<String>("name") }
                    val regions = CellRangeAddressList(1, 10, index, index)
                    val constraint = DVConstraint.createExplicitListConstraint(list.toTypedArray())
                    val dataValidation = HSSFDataValidation(regions, constraint)
                    sheet.addValidationData(dataValidation)
                }
            }
            cell.setCellValue(s.name)
        }
        wb.write(response.outputStream)
    }
}