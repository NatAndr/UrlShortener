package com.example.url.shortener;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by natal on 24-May-17.
 */
public class Utils {
    public static String randomString(int length) {
        char[] characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new SecureRandom();
        char[] result = new char[length];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }
}
