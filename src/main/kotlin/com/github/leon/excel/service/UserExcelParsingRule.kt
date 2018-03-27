package com.github.leon.excel.service

import com.github.leon.aci.dao.UserDao
import com.github.leon.aci.domain.User
import com.github.leon.files.parser.FileParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component


@Component
class UserExcelParsingRule(
        @Autowired
        val userDao: UserDao,
        @Autowired
        val passwordEncoder: PasswordEncoder
) : ExcelParsingRule<User> {
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
        data.forEach {
            it.password = passwordEncoder.encode(it.password)
            userDao.save(it)
        }
    }
}
