package com.github.leon.math

import java.math.BigDecimal


object MathUtil {
    /**
     * double型小数向上保留两位小数。比如：6.120869，则变为6.13.
     *
     * @param d 要操作的double数字
     * @return 两位小数的double。是向上取整 的。
     */
    fun upToTwoDecimal(d: Double): Double? {
        return if (d < 0) {
            (-1 * upToDecimal(Math.abs(d), 2)!!)
        } else {
            upToDecimal(d, 2)
        }
    }

    /**
     * double型小数向上保留decimalLen位小数.
     *
     * @param d          要操作的double数字
     * @param decimalLen 位数
     * @return
     */
    fun upToDecimal(d: Double?, decimalLen: Int): Double? {
        val bg = BigDecimal(d!!.toString() + "")
        return bg.setScale(decimalLen, BigDecimal.ROUND_CEILING).toDouble()
    }

    fun halfUpToDecimal(d: Double?, decimalLen: Int): Double? {
        val bg = BigDecimal(d!!.toString() + "")
        return halfUpToDecimal(bg, decimalLen)
    }

    fun halfUpToDecimal(bg: BigDecimal, decimalLen: Int): Double? {
        var bg = bg
        val plainString = bg.toPlainString()
        bg = BigDecimal(plainString)
        return bg.setScale(decimalLen, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    fun downToDeimal(d: Double?, decimalLen: Int): Double? {
        var bg = BigDecimal(d!!.toString() + "")
        val plainString = bg.toPlainString()
        bg = BigDecimal(plainString)
        return bg.setScale(decimalLen, BigDecimal.ROUND_DOWN).toDouble()
    }

    /**
     * double操作。因为double的操作有时会出现精度缺失问题，特别是除法的时候，所以写本方法，用来处理double的操作。
     *
     * @param a
     * @param b
     * @param operator 操作符。+\-\*\/分别对应加减乘除
     * @return
     */
    fun doubleOperate(a: Double, b: Double, operator: String): Double? {
        val aa = BigDecimal(a.toString())
        val bb = BigDecimal(b.toString())
        var `val` = 0.0
        if (operator == "/") {
            `val` = aa.divide(bb, 10, BigDecimal.ROUND_HALF_UP).toDouble()
        } else if (operator == "+") {
            `val` = aa.add(bb).toDouble()
        } else if (operator == "-") {
            `val` = aa.subtract(bb).toDouble()
        } else if (operator == "*") {
            `val` = aa.multiply(bb).toDouble()
        } else {
            `val` = a
        }
        return `val`
    }

    /**
     * double操作。因为double的操作有时会出现精度缺失问题，特别是除法的时候，所以写本方法，用来处理double的操作。
     *
     * @param aa
     * @param bb
     * @param operator 操作符。+\-\*\/分别对应加减乘除
     * @return
     */
    fun doubleOperate(aa: BigDecimal, bb: BigDecimal, operator: String): BigDecimal? {
        var `val`: BigDecimal? = null
        if (operator == "/") {
            `val` = aa.divide(bb, 10, BigDecimal.ROUND_HALF_UP)
        } else if (operator == "+") {
            `val` = aa.add(bb)
        } else if (operator == "-") {
            `val` = aa.subtract(bb)
        } else if (operator == "*") {
            `val` = aa.multiply(bb)
        } else {
            `val` = aa
        }
        return `val`
    }

    fun doubleOperateString(a: String): Int {
        var `val` = 0
        if (a.length - a.indexOf(".") == 3) {
            `val` = Integer.valueOf(a.replace(".", ""))//应付金额Excel
        } else if (a.indexOf(".") == -1) {
            `val` = Integer.valueOf(a + "00")//没有小数，直接拼上00
        } else {
            `val` = Integer.valueOf(a.replace(".", "") + "0")//应收金额Excel
        }

        return `val`
    }

}
