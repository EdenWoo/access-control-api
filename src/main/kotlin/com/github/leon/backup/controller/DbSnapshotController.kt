package com.github.leon.backup.controller

import com.github.leon.aci.extenstions.responseEntityOk
import com.github.leon.aci.service.AttachmentService
import com.github.leon.aci.web.base.BaseController
import com.github.leon.aws.s3.AmazonService
import com.github.leon.aws.s3.UploadUtil
import com.github.leon.backup.domain.DbSnapshot
import com.github.leon.backup.service.DbSnapshotService
import com.github.leon.extentions.execCmd
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.File
import java.io.OutputStreamWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/v1/db-snapshot")
class DbSnapshotController(

        @Value("\${spring.datasource.url}")
        val jdbcUrl: String,
        @Value("\${spring.datasource.username}")
        val username: String,
        @Value("\${spring.datasource.password}")
        val password: String,
        @Autowired
        val uploadUtil: UploadUtil,
        @Autowired
        val amazonService: AmazonService,
        @Autowired
        val attachmentService: AttachmentService,
        @Autowired
        val snapshotService: DbSnapshotService

) : BaseController<DbSnapshot, Long>() {
    val log = LoggerFactory.getLogger(DbSnapshotController::class.java)!!

    @GetMapping
    override fun page(pageable: Pageable, request: HttpServletRequest): ResponseEntity<Page<DbSnapshot>> {
        return snapshotService.findByRequestParameters(request.parameterMap,pageable).responseEntityOk()
    }

    @GetMapping("{id}")
    override fun findOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<DbSnapshot> {
        return super.findOne(id, request)
    }

    @PostMapping
    override fun saveOne(@RequestBody input: DbSnapshot, request: HttpServletRequest): ResponseEntity<*> {
        val (hostAndPort, db) = StringUtils.substringBetween(jdbcUrl, "jdbc:mysql://", "?").split("/")
        val (host, port) = hostAndPort.split(":")
        val name = "/tmp/${Instant.now().epochSecond}.sql"
        val command = "mysqldump -h$host -P$port -u$username -p$password $db  "
        val result = command.execCmd()
        Files.write(Paths.get(name), result)
        return attachmentService
                .createExportFile(File(name))
                .fold(
                        { ResponseEntity.ok(it) },
                        { attachment ->
                            attachmentService.save(attachment)
                            input.attachment = attachment
                            baseService.save(input)
                            ResponseEntity.ok(input)
                        }
                )
    }

    @PutMapping("{id}")
    override fun updateOne(@PathVariable id: Long, @RequestBody input: DbSnapshot, request: HttpServletRequest): ResponseEntity<*> {
        return super.updateOne(id, input, request)
    }

    @DeleteMapping("{id}")
    override fun deleteOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
        return super.deleteOne(id, request)
    }


    @PostMapping("rollback/{id}")
    fun importSql(@PathVariable id: Long):ResponseEntity<String> {
        val dbSnapshort = baseService.findOne(id)
        val filename = dbSnapshort.attachment!!.name
        val (_, db) = StringUtils.substringBetween(jdbcUrl, "jdbc:mysql://", "?").split("/")

        val file = amazonService.getFile(filename)
        val command = "mysql -u$username -p$password"
        val command2 = "use $db"
        val command3 = "source /tmp/${file.name}"
        log.debug("command {}", command)
        log.debug("command2 {}", command2)
        log.debug("command3 {}", command3)

        val runtime = Runtime.getRuntime()
        val process = runtime.exec(command)
        val os = process.outputStream
        val writer = OutputStreamWriter(os)
        writer.write(command2 + "\r\n" + command3)
        writer.flush()
        writer.close()
        os.close()
        return "success".responseEntityOk()
    }
}