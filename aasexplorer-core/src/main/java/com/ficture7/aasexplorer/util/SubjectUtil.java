package com.ficture7.aasexplorer.util;

public final class SubjectUtil {

    public static String parseName(String fullName) {
        int i = fullName.lastIndexOf('(');
        return fullName.substring(0, i - 1);
    }

    public static int parseId(String fullName) {
        int i = fullName.lastIndexOf('(');
        int j = fullName.lastIndexOf(')');
        String idStr = fullName.substring(i + 1, j);

        return Integer.parseInt(idStr);
    }
}
