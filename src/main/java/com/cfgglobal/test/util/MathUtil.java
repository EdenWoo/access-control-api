package com.cfgglobal.test.util;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: jacky zhung
 * Date: 15-2-11
 * Time: 上午11:40
 */
public class MathUtil {
    /**
     * double型小数向上保留两位小数。比如：6.120869，则变为6.13.
     *
     * @param d 要操作的double数字
     * @return 两位小数的double。是向上取整 的。
     */
    public static Double upToTwoDecimal(Double d) {
        if (d < 0) {
            return -upToDecimal(Math.abs(d), 2);
        } else {
            return upToDecimal(d, 2);
        }
    }

    /**
     * double型小数向上保留decimalLen位小数.
     *
     * @param d          要操作的double数字
     * @param decimalLen 位数
     * @return
     */
    public static Double upToDecimal(Double d, int decimalLen) {
        BigDecimal bg = new BigDecimal(d + "");
        double f1 = bg.setScale(decimalLen, BigDecimal.ROUND_CEILING).doubleValue();
        return f1;
    }

    public static Double halfUpToDecimal(Double d, int decimalLen) {
        BigDecimal bg = new BigDecimal(d + "");
        return halfUpToDecimal(bg, decimalLen);
    }

    public static Double halfUpToDecimal(BigDecimal bg, int decimalLen) {
        String plainString = bg.toPlainString();
        bg = new BigDecimal(plainString);
        double f1 = bg.setScale(decimalLen, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    public static Double downToDeimal(Double d, int decimalLen) {
        BigDecimal bg = new BigDecimal(d + "");
        String plainString = bg.toPlainString();
        bg = new BigDecimal(plainString);
        double f1 = bg.setScale(decimalLen, BigDecimal.ROUND_DOWN).doubleValue();
        return f1;
    }

    /**
     * double操作。因为double的操作有时会出现精度缺失问题，特别是除法的时候，所以写本方法，用来处理double的操作。
     *
     * @param a
     * @param b
     * @param operator 操作符。+\-\*\/分别对应加减乘除
     * @return
     */
    public static Double doubleOperate(Double a, Double b, String operator) {
        BigDecimal aa = new BigDecimal(a.toString());
        BigDecimal bb = new BigDecimal(b.toString());
        double val = 0;
        if (operator.equals("/")) {
            val = aa.divide(bb, 10, BigDecimal.ROUND_HALF_UP).doubleValue();
        } else if (operator.equals("+")) {
            val = aa.add(bb).doubleValue();
        } else if (operator.equals("-")) {
            val = aa.subtract(bb).doubleValue();
        } else if (operator.equals("*")) {
            val = aa.multiply(bb).doubleValue();
        } else {
            val = a;
        }
        return val;
    }

    /**
     * double操作。因为double的操作有时会出现精度缺失问题，特别是除法的时候，所以写本方法，用来处理double的操作。
     *
     * @param aa
     * @param bb
     * @param operator 操作符。+\-\*\/分别对应加减乘除
     * @return
     */
    public static BigDecimal doubleOperate(BigDecimal aa, BigDecimal bb, String operator) {
        BigDecimal val = null;
        if (operator.equals("/")) {
            val = aa.divide(bb, 10, BigDecimal.ROUND_HALF_UP);
        } else if (operator.equals("+")) {
            val = aa.add(bb);
        } else if (operator.equals("-")) {
            val = aa.subtract(bb);
        } else if (operator.equals("*")) {
            val = aa.multiply(bb);
        } else {
            val = aa;
        }
        return val;
    }

    public static int doubleOperateString(String a) {
        int val = 0;
        if (a.length() - a.indexOf(".") == 3) {
            val = Integer.valueOf(a.replace(".", ""));//应付金额Excel
        } else if (a.indexOf(".") == -1) {
            val = Integer.valueOf(a + "00");//没有小数，直接拼上00
        } else {
            val = Integer.valueOf(a.replace(".", "") + "0");//应收金额Excel
        }

        return val;
    }


    private static void calc() {
        System.out.println(halfUpToDecimal(1.223, 2));
        //  System.out.println();
        //  double receiveAmount = MathUtil.doubleOperate((double) 1, 100.0, "/");
        //  System.out.println(receiveAmount - MathUtil.doubleOperate((double) 10, 100.0, "/"));
    }
}
