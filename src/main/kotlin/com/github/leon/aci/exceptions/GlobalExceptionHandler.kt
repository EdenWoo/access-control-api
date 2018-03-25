package com.github.leon.aci.exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.ModelAndView
import java.util.concurrent.Executors
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

@ControllerAdvice
class GlobalExceptionHandler(
        @Value("\${spring.application.name}")
        val project: String,

        @Autowired
        val objectMapper: ObjectMapper

) {
    val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)!!

    @ExceptionHandler(value = [(MethodArgumentNotValidException::class)])
    fun methodArgumentNotValid(req: HttpServletRequest, e: Exception): ResponseEntity<ApiResp> {
        val exception = e as MethodArgumentNotValidException
        val errorMsg = exception.bindingResult.fieldErrors
                .map { "${it.field}-${it.rejectedValue}-${it.defaultMessage}" }
                .joinToString("|")
        val apiResp = ApiResp()
        apiResp.error = errorMsg
        return ResponseEntity.badRequest().body(apiResp)
    }

    @ExceptionHandler(value = [(ConstraintViolationException::class)])
    fun constraintViolationExceptionHandler(req: HttpServletRequest, e: Exception): ResponseEntity<*> {
        val rootCause = e as ConstraintViolationException

        val apiResp = ApiResp()
        val message: String
        message = rootCause.constraintViolations
                .map { it.propertyPath + " " + it.message + ", but the actual value is " + it.invalidValue }
                .joinToString(";")
        apiResp.error = message
        return ResponseEntity.badRequest().body(apiResp)
    }

    @ExceptionHandler(value = [(AccessDeniedException::class)])
    @Throws(Exception::class)
    fun noPermission(req: HttpServletRequest, e: Exception): ResponseEntity<ApiResp> {
        val apiResp = ApiResp()
        apiResp.error = e.message
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResp)
    }

    @ExceptionHandler(value = [(HttpRequestMethodNotSupportedException::class)])
    @Throws(Exception::class)
    fun methodNotSupported(req: HttpServletRequest, e: Exception): ResponseEntity<ApiResp> {
        val apiResp = ApiResp()
        apiResp.error = "method " + req.method + " ,url " + req.requestURI
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResp)
    }


    @ExceptionHandler(value = [(HttpMessageNotReadableException::class)])
    @Throws(Exception::class)
    fun httpMessageNotReadableException(req: HttpServletRequest, e: Exception): ResponseEntity<ApiResp> {
        val apiResp = ApiResp()
        apiResp.error = e.message
        return ResponseEntity.badRequest().body(apiResp)
    }


    @ExceptionHandler(value = [(IllegalArgumentException::class)])
    @Throws(Exception::class)
    fun illegalArgumentException(e: Exception): ResponseEntity<ApiResp> {
        // log.error(e.getMessage(), e);
        val apiResp = ApiResp()
        apiResp.error = e.message
        return ResponseEntity.badRequest().body(apiResp)
    }

    @ExceptionHandler(value = [(DataIntegrityViolationException::class)])
    @Throws(Exception::class)
    fun sqlErrorHandler(req: HttpServletRequest, e: Exception): ResponseEntity<ApiResp> {
        val rootCause = (e as DataIntegrityViolationException).rootCause
        var message = rootCause.message!!
        val mav = ModelAndView()
        mav.addObject("exception", e)
        if (message.contains("Duplicate entry")) {
            message = StringUtils.substringBetween(message, "Duplicate entry", " for key")
            message = "Duplicate data: $message"
        } else {
            // 外键约束
            val db = "collinson"
            message = StringUtils.substringBetween(message, "a foreign key constraint fails (`$db`.`", "`, CONSTRAINT")
            message = "It's used by a $message"

        }
        return ResponseEntity.badRequest().body(
                ApiResp(
                        error = message, message = e.message
                )
        )
    }

    @ExceptionHandler(value = [(Exception::class)])
    fun defaultErrorHandler(req: HttpServletRequest, e: Exception): ResponseEntity<ApiResp> {
        log.error("unknown error ", e)
        val apiResp = ApiResp()
        apiResp.error = e.message
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResp)
    }

    /*   @ExceptionHandler(value = [(Exception::class)])
       @Throws(Exception::class)
       fun defaultErrorHandler(req: HttpServletRequest, e: Exception): ModelAndView {
           val mav = ModelAndView()
           mav.addObject("exception", e)
           mav.addObject("url", req.requestURL)
           mav.viewName = "error"
           if ("XMLHttpRequest" == req.getHeader("X-Requested-With")) {
               mav.addObject("msg", e.message)
               mav.viewName = "ajaxError"
           }
           //   reportError(req, e);
           return mav
       }

       private fun reportError(req: HttpServletRequest, e: Exception) {
           val ip = req.remoteAddr
           val stackTrace = ExceptionUtils.getStackTrace(e)
           service.submit {
               val param = Maps.newHashMap<String, String>()
               param["ip"] = ip
               param["url"] = req.requestURI
               param["project"] = project
               param["query_string"] = req.queryString
               param["stackTrace"] = stackTrace
               var data = ""
               try {
                   data = objectMapper!!.writeValueAsString(param)
               } catch (e1: JsonProcessingException) {
                   e1.printStackTrace()
               } finally {

                   val urlConn: HttpURLConnection
                   try {
                       val mUrl = URL("http://discover.cfg-global.com/exception")
                       urlConn = mUrl.openConnection() as HttpURLConnection
                       urlConn.addRequestProperty("Content-Type", "application/" + "POST")
                       urlConn.setRequestProperty("Content-Length", Integer.toString(data.length))
                       urlConn.outputStream.write(data.toByteArray(charset("UTF8")))
                   } catch (ex: Exception) {
                       ex.printStackTrace()
                   }

               }
           }
       }
   */
    companion object {

        private val service = Executors.newCachedThreadPool()
    }


}