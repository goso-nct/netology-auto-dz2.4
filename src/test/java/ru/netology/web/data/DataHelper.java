package ru.netology.web.data;

import lombok.Value;

public class DataHelper
{
    private DataHelper() {}

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("petya", "123qwerty");
    }

    public static AuthInfo getInvalidAuthInfo() {
        return new AuthInfo("mumu", "qwerty");
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    public static String validCards[] = {"5559 0000 0000 0001", "5559 0000 0000 0002" };
    public static String invalidCard = "5559 0000 0000 0003";
}
