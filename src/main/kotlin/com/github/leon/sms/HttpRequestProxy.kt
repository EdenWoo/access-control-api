package com.github.leon.sms

import org.apache.http.NameValuePair
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory
import java.util.*


class HttpRequestProxy {
    val log = LoggerFactory.getLogger(HttpRequestProxy::class.java)
    private val client: CloseableHttpClient
    //返回数据编码格式
    private val encoding = "UTF-8"

    init {
        val config = RequestConfig.custom()
                .setConnectionRequestTimeout(connectTimeOut)
                .setConnectTimeout(connectTimeOut).build()

        client = HttpClients.custom().setDefaultRequestConfig(config).build()
    }

    /**
     * 用法：
     * HttpRequestProxy hrp = new HttpRequestProxy();
     * hrp.doRequest("http://www.163.com",null,null,"gbk");
     *
     * @param url      请求的资源ＵＲＬ
     * @param postData POST请求时form表单封装的数据 没有时传null
     * @param header   request请求时附带的头信息(header) 没有时传null
     * @param encoding response返回的信息编码格式 没有时传null
     * @return response返回的文本数据
     */
    fun doRequest(url: String, postData: Map<String, String>?, header: Map<String, String>?, encoding: String = "UTF-8"): String? {
        var responseString: String? = null
        //        CloseableHttpClient httpClient = HttpClients.createDefault();

        //        HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
        //        // 设置连接超时时间(单位毫秒)
        //        managerParams.setConnectionTimeout(10_000);
        //        // 设置读数据超时时间(单位毫秒)
        //        managerParams.setSoTimeout(10_000);

        //post方式
        if (postData != null) {
            val postRequest = HttpPost(url.trim { it <= ' ' })
            try {
                addHeaders(postRequest, header)
                val params = ArrayList<NameValuePair>()
                println(postData)
                postData.forEach { key, value ->
                    val pair = BasicNameValuePair(key, value)
                    params.add(pair)
                }
                postRequest.entity = UrlEncodedFormEntity(params, encoding)
                responseString = this.executeMethod(postRequest, encoding)
            } catch (e: Exception) {
                log.error(e.message, e)
            } finally {
                postRequest.releaseConnection()
            }
        }
        //get方式
        if (postData == null) {
            val getRequest = HttpGet(url.trim { it <= ' ' })
            try {
                addHeaders(getRequest, header)
                responseString = executeMethod(getRequest, encoding)
            } catch (e: Exception) {
                log.error(e.message, e)
            } finally {
                getRequest.releaseConnection()
            }
        }

        return responseString
    }

    /**
     * 用法：
     * HttpRequestProxy hrp = new HttpRequestProxy();
     * hrp.doRequest("http://www.163.com",null,null,"gbk");
     *
     * @param url      请求的资源ＵＲＬ
     * @param postData POST请求时form表单封装的数据 没有时传null
     * @param header   request请求时附带的头信息(header) 没有时传null
     * @param encoding response返回的信息编码格式 没有时传null
     * @return response返回的文本数据
     */
    fun doRequest(url: String, postData: String?, header: Map<String, String>, encoding: String): String? {
        //post方式
        if (postData != null) {
            val postRequest = HttpPost(url.trim { it <= ' ' })
            try {
                addHeaders(postRequest, header)
                postRequest.entity = StringEntity(postData, encoding)
                return executeMethod(postRequest, encoding)
            } catch (e: Exception) {
                log.error(e.message, e)
            } finally {
                postRequest.releaseConnection()
            }
        }
        //get方式
        if (postData == null) {
            val getRequest = HttpGet(url.trim { it <= ' ' })
            try {
                addHeaders(getRequest, header)
                return executeMethod(getRequest, encoding)
            } catch (e: Exception) {
                log.error(e.message, e)
            } finally {
                getRequest.releaseConnection()
            }
        }
        return null
    }

    private fun addHeaders(request: HttpUriRequest, header: Map<String, String>?) {
        header?.entries?.forEach { s -> request.addHeader(s.key, s.value) }
    }

    private fun executeMethod(request: HttpUriRequest, encoding: String): String? {
        try {
            client.execute(request).use { response ->
                return EntityUtils.toString(response.entity, encoding)
                //            Header locationHeader = request.getResponseHeader("location");
                //            //返回代码为302,301时，表示页面己经重定向，则重新请求location的url，这在
                //            //一些登录授权取cookie时很重要
                //            if (locationHeader != null) {
                //                String redirectUrl = locationHeader.getValue();
                //                this.doRequest(redirectUrl, null, null, null);
                //            }
            }
        } catch (e: Exception) {
            log.error(e.message, e)
        }

        return null
    }

    companion object {
        //超时间隔
        private val connectTimeOut = 30000
    }
}