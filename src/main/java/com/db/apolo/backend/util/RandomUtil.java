package com.db.apolo.backend.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtil {
    public static byte[] randomBytes(int length) {
        Random r = new SecureRandom();
        byte[] buf = new byte[length];
        r.nextBytes(buf);
        return buf;
    }
}