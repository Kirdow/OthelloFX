package com.kirdow.othello.util;

public class Utils {

    private static String os_name = null;
    public static String getOSString() {
        return os_name != null ? os_name : (os_name = System.getProperty("os.name").toLowerCase());
    }

    public static class OS {
        public static final boolean isMac;
        public static final boolean isWin;
        public static final boolean isLinux;
        public static final boolean isOther;

        static {
            String osStr = getOSString();

            isMac = osStr.contains("mac");
            isWin = osStr.contains("windows");
            isLinux = osStr.contains("linux");
            isOther = !(isMac || isWin || isLinux);
        }
    }

    public static void setAppName(String appName) {
        if (OS.isMac) {
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", appName);
        }
    }

}
