package com.myself.MySweety.core.common;

//汉字数字转阿拉伯数字
class ZiFun {
    public static String convertToNum(char c) {
        switch (c) {
            case '零':
                return "0";
            case '一':
                return "1";
            case '二':
                return "2";
            case '三':
                return "3";
            case '四':
                return "4";
            case '五':
                return "5";
            case '六':
                return "6";
            case '七':
                return "7";
            case '八':
                return "8";
            case '九':
                return "9";
            case '十':
                return "1";
        }
        return "";
    }
}