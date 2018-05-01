package com.github.leon.encrypt

import java.net.URLDecoder
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec

/**
 * Created with IntelliJ IDEA.
 * User: jacky zhung
 * Date: 14-12-3
 * Time: 下午3:08
 */
object DESUtil {

    /**
     * 加密逻辑方法
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun encryptProcess(message: String, key: String): ByteArray {
        val cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
        val desKeySpec = DESKeySpec(key.toByteArray(charset("UTF-8")))
        val keyFactory = SecretKeyFactory.getInstance("DES")
        val secretKey = keyFactory.generateSecret(desKeySpec)
        val iv = IvParameterSpec(key.toByteArray(charset("UTF-8")))
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)
        return cipher.doFinal(message.toByteArray(charset("UTF-8")))
    }

    /**
     * 解密逻辑方法
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun decryptProcess(message: String, key: String): String {
        val bytesrc = convertHexString(message)
        val cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
        val desKeySpec = DESKeySpec(key.toByteArray(charset("UTF-8")))
        val keyFactory = SecretKeyFactory.getInstance("DES")
        val secretKey = keyFactory.generateSecret(desKeySpec)
        val iv = IvParameterSpec(key.toByteArray(charset("UTF-8")))
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)
        val retByte = cipher.doFinal(bytesrc)
        return String(retByte)
    }

    /**
     * 16进制数组数转化
     *
     * @param ss
     * @return
     */
    @Throws(Exception::class)
    private fun convertHexString(ss: String): ByteArray {
        val digest = ByteArray(ss.length / 2)
        for (i in digest.indices) {
            val byteString = ss.substring(2 * i, 2 * i + 2)
            val byteValue = Integer.parseInt(byteString, 16)
            digest[i] = byteValue.toByte()
        }
        return digest
    }

    /**
     * 十六进制数转化
     *
     * @param b
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun toHexString(b: ByteArray): String {
        val hexString = StringBuffer()
        for (i in b.indices) {
            var plainText = Integer.toHexString(0xff and b[i].toInt())
            if (plainText.length < 2)
                plainText = "0$plainText"
            hexString.append(plainText)
        }

        return hexString.toString()
    }

    /**
     * 加密方法
     */
    @Throws(Exception::class)
    fun encrypt(message: String, key: String): String {
        val orignStr = java.net.URLEncoder.encode(message, "utf-8")
        return toHexString(encryptProcess(orignStr, key))
    }


    /**
     * 解密方法
     */
    @Throws(Exception::class)
    fun decrypt(message: String, key: String): String {
        return URLDecoder.decode(decryptProcess(message, key), "utf-8")
    }

    /**
     * 测试Main方法
     *
     * @param args
     * @throws Exception
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val key = "aaasssdd" //密码只能为8位
        val message = "中国人0132d39e8361f10"
        val enStr = encrypt(message, key)
        println("加密后:$enStr")
        val decStr = decrypt(enStr, key)
        println("解密后:$decStr")
    }
}
