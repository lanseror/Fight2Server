package com.fight2.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encodes a string using MD5 hashing
 * 
 * @author Rafael Steil
 * @version $Id: MD5.java,v 1.7 2006/08/23 02:13:44 rafaelsteil Exp $
 */
public class MD5 {
    /**
     * Encodes a string
     * 
     * @param str
     *            String to encode
     * @return Encoded String
     * @throws NoSuchAlgorithmException
     */
    public static String crypt(final String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }

        final StringBuffer hexString = new StringBuffer();

        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            final byte[] hash = md.digest();

            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return hexString.toString();
    }
}
