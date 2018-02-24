package com.cfgglobal

import com.github.leon.extentions.execCmd


fun main(args: Array<String>) {
    val result = "mysqldump --compact -uroot -proot collinson user role branch permission rule role_permission role_permission_rule".execCmd()
    println(result.joinToString("\n"))


}