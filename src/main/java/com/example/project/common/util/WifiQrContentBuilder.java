package com.example.project.common.util;

public class WifiQrContentBuilder {

    public static String build(String ssid, String password, String authType, String hiddenYn) {

        boolean isNoPass = "nopass".equalsIgnoreCase(authType);

        StringBuilder sb = new StringBuilder("WIFI:");
        sb.append("T:").append(authType).append(";");
        sb.append("S:").append(escape(ssid)).append(";");

        if (!isNoPass) {
            String pwd = password != null ? password : "";
            sb.append("P:").append(escape(pwd)).append(";");
        }

        sb.append("H:").append(hiddenYn).append(";;");

        return sb.toString();
    }

    // 세미콜론, 콜론 등 특수문자 처리
    private static String escape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace(";", "\\;");
    }

}
