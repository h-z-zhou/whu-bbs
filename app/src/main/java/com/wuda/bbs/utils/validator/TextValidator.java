package com.wuda.bbs.utils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextValidator {

    public static boolean isUidValid(String uid) {
        return uid.length() >= 2 && Character.isAlphabetic(uid.charAt(0));
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isIdNumberValid(String idNumber) {
        String expression = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(idNumber);
        return matcher.matches();
    }

    public static boolean isCampusIdValid(String campusId) {
        // 学号，工号
//        return campusId.length() == 13;
        return !campusId.isEmpty();
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.length() == 11 || phoneNumber.length() == 8;
    }
}
