package com.osp.core.utils;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsValidator {

    public static final String DOT3 = "...";
    public static final String ENTER = "\n";
    public static final String SPACE = " ";
    public static final String COLON = ":";
    public static final String MASK = "\"";
    public static final String PLUS = "\\+";
    public static final String MINUS = "\\-";
    public static final String SEMI_COLON = ";";
    public static final String UNIT_SEPARATOR = "_";
    public static final String PERCENT = "%";
    public static final String VERTICAL_LINE = "|";
    public static final String SHARP = "#";
    public static final String BULLET = "-";
    //Danh sách biểu thức bất quy tắc
    public static final String REGEX_NUMBER = "^[0-9]*$";
    public static final String REGEX_SEARCH_NUMBER = "^[0-9*]*$";
    public static final String REGEX_TEXT_NUMBER = "^[a-zA-Z0-9]+$";
    public static final String REGEX_TEXT_USERNAME = "^[_a-zA-Z0-9]+$";
    public static final String REGEX_DATE = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]|(?:Jan|Mar|May|Jul|Aug|Oct|Dec)))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2]|(?:Jan|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec))\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)(?:0?2|(?:Feb))\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9]|(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep))|(?:1[0-2]|(?:Oct|Nov|Dec)))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
    public static final String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String REGEX_TEL = "\\+?[0-9]{10,12}";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,50}$";

    public static boolean verifyPassword(String password) {
        if (password == null) {
            return false;
        }
        return password.matches(PASSWORD_PATTERN);
    }

    /**
     * Hàm validate email
     */
    public static boolean validateEmail(Object value) {
        String emailValue = value.toString();
        Pattern pattern = Pattern.compile(REGEX_EMAIL);
        Matcher matcher = pattern.matcher(emailValue);
        boolean check = matcher.matches();
        return check;
    }

    // check mã
    public static boolean validateCode(Object value) {
        String code = value.toString();
        Pattern pattern = Pattern.compile(REGEX_NUMBER);
        Matcher matcher = pattern.matcher(code);
        boolean check = matcher.matches();
        return check;
    }

    public static boolean isNumeric(String inputData) {
        Scanner sc = new Scanner(inputData);
        return sc.hasNextBigDecimal();
    }

}
