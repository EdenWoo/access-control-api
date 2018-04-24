package com.github.leon.sms.clicksend

import com.github.leon.sms.HttpRequestProxy
import com.github.leon.sms.clicksend.config.ClicksendProperties
import com.github.leon.sms.domain.MessageLog
import com.github.leon.sms.service.MessageProvider
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.net.URLEncoder
import java.text.MessageFormat

@EnableConfigurationProperties(ClicksendProperties::class)
@Component
class ClicksendMessageProvider(
        @Autowired
        val clicksendProperties: ClicksendProperties
) : MessageProvider {
    val log = LoggerFactory.getLogger(ClicksendMessageProvider::class.java)!!
    override fun send(messageLog: MessageLog): Pair<String?, String> {

        var urlParameters = MessageFormat.format("username=${clicksendProperties.username}&key=${clicksendProperties.apiKey}&to={0}&message={1}",
                URLEncoder.encode(messageLog.sendTo, "UTF-8"),
                URLEncoder.encode(messageLog.message, "UTF-8")
        )
        val hrp = HttpRequestProxy()
        val map: Map<String, String>? = null
        val result = hrp.doRequest("${clicksendProperties.apiUrl}?$urlParameters", map, map, "UTF-8") as String
        log.debug(result)
        val code = StringUtils.substringBetween(result, "<result>", "</result>")
        //val xml = JaxbKit.unmarshal(result, Xml::class.java)!!
        val error = when (code) {
            "0000" -> null
            else -> "error"
        }
        return Pair(error, result)
    }
}
