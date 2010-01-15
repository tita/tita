/**
   Copyright 2009 TiTA Project, Vienna University of Technology
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE\-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package at.ac.tuwien.ifs.tita.business.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class for Security functionality.
 * 
 * @author rene
 * 
 */
public class TiTASecurity {
    private static final String HASHALG = "SHA-256";

    /**
     * Hashes the password with SHA-256 Algorithm.
     * 
     * @param pwd password to hash as String.
     * @return hashed password
     * @throws NoSuchAlgorithmException if algorithm wasn't found.
     */
    public static String calcHash(String pwd) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(HASHALG);
        byte[] encryptMsg = md.digest(pwd.getBytes());
        return bytes2String(encryptMsg);
    }

    /**
     * Converts byte array in readable String.
     * 
     * @param bytes digest to convert.
     * @return digest as readable String.
     */
    private static String bytes2String(byte[] bytes) {
        StringBuilder string = new StringBuilder();
        for (byte b : bytes) {
            // CHECKSTYLE:OFF
            String hexString = Integer.toHexString(0x00FF & b);
            // CHKECKSTYLE:ON
            string.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return string.toString();
    }
}