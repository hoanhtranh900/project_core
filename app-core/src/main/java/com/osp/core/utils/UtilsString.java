package com.osp.core.utils;

import org.apache.commons.lang.StringUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.*;

import static org.mariadb.jdbc.internal.com.send.authentication.ed25519.Utils.bytesToHex;

public class UtilsString {
    private static final String DASH = "-";
    public static final String UNDERSCORE = "_";
    public static final String SEMICOLON = ";";
    public static final String SPACE = " ";

    public static String randomUUID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest salt = MessageDigest.getInstance("SHA-256");
        salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
        String digest = bytesToHex(salt.digest());
        return digest;
    }

    public static String stringToHex(String string) {
        StringBuilder buf = new StringBuilder(200);
        for (char ch: string.toCharArray()) {
            if (buf.length() > 0)
                buf.append(' ');
            buf.append(String.format("%04x", (int) ch));
        }
        return buf.toString();
    }

    public static String toHexString(byte[] ba) {
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < ba.length; i++)
            str.append(String.format("%x", ba[i]));
        return str.toString();
    }

    public static String fromHexString(String hex) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }

    public static String toHexString(Integer decimal) {
        try {
            String hex = Integer.toHexString(decimal);
            return hex;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeBase64(String originalInput){
        try {
            String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
            return encodedString;
        } catch (Exception e) {
            return null;
        }
    }

    public static String decodeBase64(String encodedString){
        try {
            String decodedString = new String(Base64.getDecoder().decode(encodedString));
            return decodedString;
        } catch (Exception e) {
            return null;
        }
    }

    public static String standardized(String str) {
        str = str.trim();
        str = str.replaceAll("\\s+", " ");
        return str;
    }

    public static String standardizedCode(String str) {
        str = str.trim();
        str = str.replaceAll(" ", "");

        return str;
    }

    public static List<String> parseString(String text, String seperator) {
        List<String> vResult = new ArrayList<>();
        if (text == null || "".equals(text)) {
            return vResult;
        }

        String tempStr = text.trim();
        String currentLabel = null;

        int index = tempStr.indexOf(seperator);
        while (index != -1) {
            currentLabel = tempStr.substring(0, index).trim();
            // Only accept not null element
            if (!"".equals(currentLabel)) {
                vResult.add(currentLabel);
            }
            tempStr = tempStr.substring(index + 1);
            index = tempStr.indexOf(seperator);
        }
        // Last label
        currentLabel = tempStr.trim();
        if (!"".equals(currentLabel)) {
            vResult.add(currentLabel);
        }
        return vResult;
    }

    public static String join(List<String> list, String sperator) {
        if (list == null || list.size() == 0) return null;
        String string = "";
        for (int i=0; i < list.size(); i++) {
            if (i == (list.size() -1)) {
                string += list.get(i);
            } else {
                string += list.get(i) + sperator;
            }
        }
        return string;
    }

    public static boolean isDigit(char c) {
        int x = (int) c;
        if ((x >= 48) && (x <= 57)) {
            return true;
        }
        return false;
    }
    public static boolean isNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return false;
        }
        char[] c = number.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (!isDigit(c[i])) {
                return false;
            }
        }
        return true;
    }

    public static String convertUnicodeToASCII(String str) throws UnsupportedEncodingException {

        if (str == null) {
            return "";
        }

        String rs = str.replace('\u0111', 'd');
        rs = rs.replace('\u0110', 'D');
        rs = rs.replace('\u00D0', 'D');
        rs = rs.replace('\u0089', 'D');
        rs = rs.replaceAll("\\%", "\\*");

        rs = Normalizer.normalize(rs, Normalizer.Form.NFKD);
        String regex = "[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+";

        rs = new String(rs.replaceAll(regex, "").getBytes("ascii"), "ascii");

        return rs;
    }

    public static String cutString(String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        if ("".equals(str.trim())) {
            return "";
        }
        String temp = str.trim().toUpperCase();
        temp = SPACE + temp;
        int i=0;
        String rs = "";
        while (i < temp.length()) {
            if((temp.charAt(i) == ' ') && (temp.charAt(i+1) != ' ')) {
                rs = rs + temp.charAt(i+1);
            }
            i++;
        }
        return rs;
    }

    public static String encodeStringToUrlParam(String fileName) {
        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return encodedFileName;
    }

    public static String OTP(int len) {
        // Using numeric values
        String numbers = "0123456789";

        // Using random method
        Random rndm_method = new Random();

        String otp = "";

        for (int i = 0; i < len; i++) {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp += numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return otp;
    }

    public static String geek_Password(int len) {
        // A strong password has Cap_chars, Lower_chars,
        // numeric value and symbols. So we are using all of
        // them to generate our password
        String Capital_chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Small_chars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*_=+-/.?<>)";


        String values = Capital_chars + Small_chars + numbers + symbols;

        // Using random method
        Random rndm_method = new Random();

        String password = "";

        for (int i = 0; i < len; i++) {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            password += values.charAt(rndm_method.nextInt(values.length()));

        }
        return password;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        List<String> lst = parseString("1_2_3", "_");
        System.out.println(lst.toString());
        System.out.println(standardized(" Uchiha    mAdara  "));
        System.out.println(standardizedCode(" Uchiha    mAdara  "));
        System.out.println(convertUnicodeToASCII("Thành phố hà nội"));
        System.out.println(cutString("Thành phố hà nội"));
        System.out.println(encodeStringToUrlParam("file n"));

        int length = 4;
        System.out.println(OTP(length));    // random otp
        System.out.println(geek_Password(length));  // random password
    }
}
