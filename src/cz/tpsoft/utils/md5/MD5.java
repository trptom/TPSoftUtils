/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.tpsoft.utils.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author trptom
 */
public class MD5 {
    public static String getMD5(String fromText) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(fromText.getBytes());
            byte[] textCode = algorithm.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < textCode.length; i++)
            {
                String hex = Integer.toHexString(0xFF & textCode[i]);
                if (hex.length() == 1)
                {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
}
