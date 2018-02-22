package com.cfgglobal.test.service.excel

import com.cfgglobal.test.domain.User
import com.github.leon.files.parser.FileParser
import org.springframework.stereotype.Component


@Component
class UserExcelParsingRule : ExcelParsingRule<User> {
    override val fileParser: FileParser
        get() {
            val fileParser = FileParser()
            fileParser.start = 1
            fileParser.addCell(0, "username")
            fileParser.addCell(1, "password")
            return fileParser
        }

    override val entityClass: Class<*>
        get() = User::class.java

    override val ruleName: String
        get() = "user"

    override fun process(data: List<User>) {

    }
}
