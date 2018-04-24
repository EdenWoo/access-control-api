package com.github.leon.string


object StrKit {
    fun remainLast(str: String, length: Int): String {
        return str.substring(str.length - length)
    }


    fun deleteChars(str: String, vararg chars: String): String {
        var str = str
        for (aChar in chars) {
            str = str.replace(aChar, "")
        }
        return str
    }

    fun emptyChars(str: String, vararg chars: String): String {
        var str = str
        for (aChar in chars) {
            str = str.replace(aChar, " ")
        }
        return str
    }

}
