package com.github.leon.string;


public class StrKit {
    public String remainLast(String str, int length) {
        return str.substring(str.length() - length);
    }


    public String deleteChars(String str, String... chars) {
        for (String aChar : chars) {
            str = str.replace(aChar, "");
        }
        return str;
    }

    public String emptyChars(String str, String... chars) {
        for (String aChar : chars) {
            str = str.replace(aChar, " ");
        }
        return str;
    }

}
