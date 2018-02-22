package com.cfgglobal.test.web.interceptors

import com.cfgglobal.test.config.ActionReportProperties
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.io.File
import java.io.IOException
import java.io.Writer
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@EnableConfigurationProperties(ActionReportProperties::class)
class ActionReportInterceptor : HandlerInterceptor {
    @Autowired
    private val actionReportProperties: ActionReportProperties? = null

    private val maxOutputLengthOfParaValue = 512

    private fun fileName(clazz: Class<*>): String {
        var controllerFile = System.getProperty("user.dir") + File.separator + actionReportProperties!!.module + File.separator + "src"

        if (actionReportProperties.isMaven) {
            controllerFile += File.separator + "main" + File.separator + "java"
        }

        for (temp in clazz.name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            controllerFile = controllerFile + File.separator + temp
        }
        return controllerFile + ".java"
    }

    private fun lineNum(codeFragment: String, fileName: String): Int {
        val lines: List<String>
        var lineNum = 1
        val path = Paths.get(fileName)
        try {
            lines = Files.readAllLines(path, Charset.forName("UTF-8"))
            for (i in lines.indices) {
                val line = lines[i]
                if (StringUtils.isNotBlank(line) && StringUtils.deleteWhitespace(line).contains(codeFragment)) {
                    lineNum = i + 1
                    break
                }
            }
        } catch (ignored: NoSuchFileException) {
        } catch (e2: IOException) {
            e2.printStackTrace()
        }

        return lineNum
    }

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, o: Any): Boolean {
        if (!actionReportProperties!!.isSwitcher) {
            return true
        }
        if (o !is HandlerMethod) {
            return true
        }
        report(request, o)

        return true
    }

    @Throws(Exception::class)
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, o: Any, modelAndView: ModelAndView?) {
    }

    @Throws(Exception::class)
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, o: Any, e: Exception?) {
    }

    fun report(request: HttpServletRequest?, handlerMethod: HandlerMethod) {
        val sb = StringBuilder("\nSpring MVC controller report -------- ").append(sdf.get().format(Date())).append(" ------------------------------\n")
        val str = handlerMethod.returnType.method!!.toGenericString()
        val arr = str.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val search = arr[0] + StringUtils.substringAfterLast(arr[1], ".") + handlerMethod.method.name
        val cc = handlerMethod.beanType
        sb.append("URL         :").append(request!!.requestURI).append("\n")
        sb.append("Controller  : ").append(cc.name).append(".(").append(cc.simpleName).append(".java:")
                .append(lineNum(search, fileName(cc))).append(")")
        sb.append("\nMethod      : ").append(handlerMethod.method.name).append("\n")
        val e = request.parameterNames
        if (e.hasMoreElements()) {
            sb.append("Parameter   : ")
            while (e.hasMoreElements()) {
                val name = e.nextElement()
                val values = request.getParameterValues(name)
                if (values.size == 1) {
                    sb.append(name).append("=")

                    if (values[0] != null && values[0].length > maxOutputLengthOfParaValue) {
                        sb.append(values[0].substring(0, maxOutputLengthOfParaValue)).append("...")
                    } else {
                        sb.append(values[0])
                    }
                } else {
                    sb.append(name).append("[]={")
                    for (i in values.indices) {
                        if (i > 0)
                            sb.append(",")
                        sb.append(values[i])
                    }
                    sb.append("}")
                }
                sb.append("  ")
            }
            sb.append("\n")
        }
        sb.append("--------------------------------------------------------------------------------\n")

        try {
            writer.write(sb.toString())
        } catch (ex: IOException) {
            throw RuntimeException(ex)
        }

    }

    private class SystemOutWriter : Writer() {
        @Throws(IOException::class)
        override fun write(str: String) {
            print(str)
        }

        @Throws(IOException::class)
        override fun write(cbuf: CharArray, off: Int, len: Int) {
        }

        @Throws(IOException::class)
        override fun flush() {
        }

        @Throws(IOException::class)
        override fun close() {
        }
    }

    companion object {

        private val sdf = ThreadLocal.withInitial { SimpleDateFormat("yyyy-MM-dd HH:mm:ss") }
        private val writer = SystemOutWriter()
    }
}
