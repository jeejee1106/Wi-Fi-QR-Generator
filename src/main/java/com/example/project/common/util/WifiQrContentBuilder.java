package com.example.project.common.util;

public class WifiQrContentBuilder {

    public static String build(String ssid, String password, String authType, String hiddenYn) {
        String pwd = password != null ? password : "";

        // WIFI:T:WPA;S:ssid;P:password;H:false;;
        return "WIFI:"
                + "T:" + authType + ";"
                + "S:" + escape(ssid) + ";"
                + "P:" + escape(pwd) + ";"
                + "H:" + hiddenYn + ";;";
    }

    // 세미콜론, 콜론 등 특수문자 처리
    private static String escape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace(";", "\\;");
    }

}
