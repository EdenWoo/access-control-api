package com.cfgglobal.test.service

import com.cfgglobal.test.domain.Attachment
import com.cfgglobal.test.enums.AttachmentType
import com.cfgglobal.test.service.base.BaseService
import com.github.leon.ams.s3.UploadUtil
import io.vavr.control.Either
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class AttachmentService : BaseService<Attachment, Long>() {

    @Autowired
    private val uploadUtil: UploadUtil? = null

    fun createFile(tempFile: MultipartFile, type: AttachmentType): Either<String, Attachment> {
        if (tempFile.isEmpty) {
            return Either.left("no file uploaded")
        }
        val name = uploadUtil!!.write(tempFile, "")
        val attachment = Attachment(name,
                tempFile.contentType!!,
                tempFile.size,
                tempFile.originalFilename!!, null, type, "/v1/attachment/download?filename=" + name)
        save(attachment)
        return Either.right(attachment)
    }

    fun createExportFile(file: File): Either<String, Attachment> {
        val name = uploadUtil!!.write(file, "")
        val attachment = Attachment(
                originalFilename = file.name,
                name = name,
                size = file.length(),
                type = AttachmentType.EXPORT,
                fullPath = "/v1/attachment/download?filename=" + name)
        return Either.right(attachment)
    }


}
