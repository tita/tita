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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

import org.apache.axis.encoding.Base64;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.ifs.tita.business.security.exception.TiTASecurityException;

/**
 * Class for Security functionality.
 * 
 * @author rene
 * 
 */
public class TiTASecurity {

    // the specification of the Hash Algorithm
    private static final String HASHALG = "SHA-256";
    // the specification of the Key-Algorithm
    private static final String KEYALG = "RSA";
    // Each Certificate has a validity of 10 years
    private static final Long VALID_TIME_RANGE = 10L * 365L * 24L * 60L * 60L * 1000L;
    // the Keysize
    private static final int KEYSIZE = 1024;
    // the path to the Key Store
    private static final String KEY_STORE_PATH = "security" + File.separatorChar + "keystore" + File.separatorChar
        + "keystore.store";
    // hard-coded keystore password
    private static final String KEY_STORE_PASSWORD = "KEYSTORE123";

    // Logger
    private static Logger log = LoggerFactory.getLogger(TiTASecurity.class);

    private static KeyStore keyStore;

    /**
     * Hashes the password with SHA-256 Algorithm.
     * 
     * @param pwd password to hash as String.
     * @return hashed password
     * @throws NoSuchAlgorithmException if algorithm wasn't found.
     */
    public static String calcHash(String pwd) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(HASHALG);
        String encryptMsg = Base64.encode(md.digest(pwd.getBytes()));
        return encryptMsg;
    }

    /**
     * decrypts an encrypted and Base64 encoded Password.
     * 
     * @param userName the user to decrypt the Password.
     * @param password the password for the KeyStore
     * @param encryptedIssueTrackerPassword the password to decrypt
     * @return the decrypted password
     * @throws TiTASecurityException when anything goes wrong
     */
    public static String getDecryptedPassword(String userName, String password, String encryptedIssueTrackerPassword)
        throws TiTASecurityException {

        if (!isKeyStoreInitialized()) {
            initOrLoadKeyStore();
        }

        String decryptedPassword = "";

        PrivateKey privateKey = null;

        try {
            if (keyStore.containsAlias("privKey" + userName)) {
                privateKey = (PrivateKey) keyStore.getKey("privKey" + userName, password.toCharArray());
            } else {
                throw new TiTASecurityException("User was not found in KeyStore");
            }

            Cipher cipher = Cipher.getInstance(KEYALG);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            decryptedPassword = new String(cipher.doFinal(Base64.decode(encryptedIssueTrackerPassword)));
        } catch (KeyStoreException e) {
            log.error("Error Decrypting given Password, KeystoreException was thrown.");
            throw new TiTASecurityException("Could not restore Password, KeystoreException was thrown.\n"
                + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("Error Decrypting given Password, given Algorithm was not found.");
            throw new TiTASecurityException("Could not restore Password, given Algorithm was not found.\n"
                + e.getMessage());
        } catch (UnrecoverableKeyException e) {
            log.error("Error Decrypting given Password, UnrecoverableKeyException was thrown.");
            throw new TiTASecurityException("Could not restore Password, UnrecoverableKeyException was thrown.\n"
                + e.getMessage());
        } catch (NoSuchPaddingException e) {
            log.error("Error Decrypting given Password, NoSuchPaddingException was thrown.");
            throw new TiTASecurityException("Could not restore Password, NoSuchPaddingException was thrown.\n"
                + e.getMessage());
        } catch (InvalidKeyException e) {
            log.error("Error Decrypting given Password, Key was invalid.");
            throw new TiTASecurityException("Could not restore Password, Key was invalid.\n" + e.getMessage());
        } catch (BadPaddingException e) {
            log.error("Error Decrypting given Password, BadPaddingException was thrown.");
            throw new TiTASecurityException("Could not restore Password, BadPaddingException was thrown.\n"
                + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            log.error("Error Decrypting given Password, Blocksize of Cipher is not valid.");
            throw new TiTASecurityException("Could not restore Password, Blocksize of Cipher is not valid.\n"
                + e.getMessage());
        }

        return decryptedPassword;
    }

    /**
     * Encrypts the issueTrackerPassword for a specified User and returns it.
     * 
     * @param userName the user to encrypt the password.
     * @param password the password to store the keys.
     * @param issueTrackerPassword the password to encrypt.
     * @return the encrypted password
     * @throws TiTASecurityException if anything goes wrong.
     */
    public static String getEncryptedPassword(String userName, String password, String issueTrackerPassword)
        throws TiTASecurityException {

        if (password == null || userName == null || issueTrackerPassword == null) {
            throw new TiTASecurityException("Username, Password and IssueTrackerPassword must not be null!");
        }

        if (!isKeyStoreInitialized()) {
            initOrLoadKeyStore();
        }

        String encryptedPassword = "";

        PublicKey publicKey = null;

        try {
            if (keyStore.containsAlias("certificate" + userName)) {
                publicKey = keyStore.getCertificate("certificate" + userName).getPublicKey();
            } else {
                KeyPair keyPair = generateKeyPair(userName);
                X509Certificate cert = generateV3Certificate(keyPair, userName);
                keyStore.setKeyEntry("privKey" + userName, keyPair.getPrivate(), password.toCharArray(),
                    new Certificate[] { cert });
                keyStore.setCertificateEntry("certificate" + userName, cert);

                publicKey = keyPair.getPublic();
            }

            Cipher cipher = Cipher.getInstance(KEYALG);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            encryptedPassword = Base64.encode(cipher.doFinal(issueTrackerPassword.getBytes()));

            saveKeyStore();
        } catch (KeyStoreException e) {
            log.error("Error Decrypting given Password, KeystoreException was thrown.");
            throw new TiTASecurityException("Could not restore Password, KeystoreException was thrown.\n"
                + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("Error Decrypting given Password, given Algorithm was not found.");
            throw new TiTASecurityException("Could not restore Password, given Algorithm was not found.\n"
                + e.getMessage());
        } catch (NoSuchPaddingException e) {
            log.error("Error Decrypting given Password, NoSuchPaddingException was thrown.");
            throw new TiTASecurityException("Could not restore Password, NoSuchPaddingException was thrown.\n"
                + e.getMessage());
        } catch (InvalidKeyException e) {
            log.error("Error Decrypting given Password, Key was invalid.");
            throw new TiTASecurityException("Could not restore Password, Key was invalid.\n" + e.getMessage());
        } catch (BadPaddingException e) {
            log.error("Error Decrypting given Password, BadPaddingException was thrown.");
            throw new TiTASecurityException("Could not restore Password, BadPaddingException was thrown.\n"
                + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            log.error("Error Decrypting given Password, Blocksize of Cipher is not valid.");
            throw new TiTASecurityException("Could not restore Password, Blocksize of Cipher is not valid.\n"
                + e.getMessage());
        }

        return encryptedPassword;
    }

    /**
     * Encrypts the issueTrackerPassword for a specified User, if user-key is not available, an exception will be
     * thrown.
     * 
     * @param userName the user to encrypt the password.
     * @param issueTrackerPassword the password to encrypt.
     * @return the encrypted password
     * @throws TiTASecurityException if anything goes wrong.
     */
    public static String getEncryptedPassword(String userName, String issueTrackerPassword)
        throws TiTASecurityException {
        if (!isKeyStoreInitialized()) {
            initOrLoadKeyStore();
        }

        String encryptedPassword = "";

        PublicKey publicKey = null;

        try {
            if (keyStore.containsAlias("certificate" + userName)) {
                publicKey = keyStore.getCertificate("certificate" + userName).getPublicKey();
            } else {
                throw new TiTASecurityException("User in Keystore not found");
            }

            Cipher cipher = Cipher.getInstance(KEYALG);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            encryptedPassword = Base64.encode(cipher.doFinal(issueTrackerPassword.getBytes()));

            saveKeyStore();
        } catch (KeyStoreException e) {
            log.error("Error Decrypting given Password, KeystoreException was thrown.");
            throw new TiTASecurityException("Could not restore Password, KeystoreException was thrown.\n"
                + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("Error Decrypting given Password, given Algorithm was not found.");
            throw new TiTASecurityException("Could not restore Password, given Algorithm was not found.\n"
                + e.getMessage());
        } catch (NoSuchPaddingException e) {
            log.error("Error Decrypting given Password, NoSuchPaddingException was thrown.");
            throw new TiTASecurityException("Could not restore Password, NoSuchPaddingException was thrown.\n"
                + e.getMessage());
        } catch (InvalidKeyException e) {
            log.error("Error Decrypting given Password, Key was invalid.");
            throw new TiTASecurityException("Could not restore Password, Key was invalid.\n" + e.getMessage());
        } catch (BadPaddingException e) {
            log.error("Error Decrypting given Password, BadPaddingException was thrown.");
            throw new TiTASecurityException("Could not restore Password, BadPaddingException was thrown.\n"
                + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            log.error("Error Decrypting given Password, Blocksize of Cipher is not valid.");
            throw new TiTASecurityException("Could not restore Password, Blocksize of Cipher is not valid.\n"
                + e.getMessage());
        }

        return encryptedPassword;
    }

    /**
     * reads the Keys of a specific User from the FileSystem.
     * 
     * @param userName the KeyPair-owner
     * @return the KeyPair for the specified user.
     * @throws TiTASecurityException if generation of the KeyPair failed
     */
    private static KeyPair generateKeyPair(String userName) throws TiTASecurityException {

        KeyPairGenerator kpg;
        KeyPair keyPair;
        try {
            kpg = KeyPairGenerator.getInstance(KEYALG);
            kpg.initialize(KEYSIZE);
            keyPair = kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm while generating KeyPair was not found.");
            throw new TiTASecurityException("KeyPair-generation failed. The specified Algorithm was not found.\n"
                + e.getMessage());
        }
        return keyPair;
    }

    /**
     * Inits the KeyStore if already available, else it will be loaded.
     * 
     * @throws TiTASecurityException if initialization of KeyStore fails.
     */
    private static void initOrLoadKeyStore() throws TiTASecurityException {

        File keyStoreFile = new File(KEY_STORE_PATH);
        try {
            InputStream is = null;
            if (checkExistance(keyStoreFile)) {
                is = new FileInputStream(keyStoreFile);
            }

            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(is, KEY_STORE_PASSWORD.toCharArray());
        } catch (NoSuchAlgorithmException e) {
            log.error("Could not initialize the Keystore, specified Algorithm was not found.");
            throw new TiTASecurityException("Specified Algorithm: " + KeyStore.getDefaultType() + " was not found.\n"
                + e.getMessage());
        } catch (CertificateException e) {
            log.error("Certificate Exception was thrown. Initialization Process will be aborted.");
            throw new TiTASecurityException("CertificateException was thrown.\n" + e.getMessage());
        } catch (IOException e) {
            log.error("Could not read the Keystore File. Initialization Process will be aborted.");
            throw new TiTASecurityException("I/O-Exception was thrown while reading the KeyStoreFile.\n"
                + e.getMessage());
        } catch (KeyStoreException e) {
            log.error("Keystore could not be created, KeyStoreException was thrown.");
            throw new TiTASecurityException("Could not initialize KeyStore, Exception was thrown.\n" + e.getMessage());
        }
    }

    /**
     * saves the Keystore to a file on the underlying File System.
     * 
     * @throws TiTASecurityException if File could not be written.
     */
    private static void saveKeyStore() throws TiTASecurityException {
        if (!isKeyStoreInitialized()) {
            throw new TiTASecurityException("KeyStore is not initialized and could therefore not be stored.");
        }
        File keyStoreFile = new File(KEY_STORE_PATH);

        try {
            checkExistance(keyStoreFile);

            OutputStream os = new FileOutputStream(keyStoreFile);
            try {
                keyStore.store(os, KEY_STORE_PASSWORD.toCharArray());
            } catch (KeyStoreException e) {
                log.error("Error while saving the KeyStore. KeyStoreException was thrown.");
                throw new TiTASecurityException("Could not save KeyStore. KeyStoreException was thrown.\n"
                    + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                log.error("Error while saving the KeyStore. Specified Algorithm not found.");
                throw new TiTASecurityException("Could not save KeyStore. Specified Algorithm not found.\n"
                    + e.getMessage());
            } catch (CertificateException e) {
                log.error("Error while saving the KeyStore. CertificateException was thrown.");
                throw new TiTASecurityException("Could not save KeyStore. CertificateException was thrown.\n"
                    + e.getMessage());
            } catch (IOException e) {
                log.error("Error while saving the KeyStore. IOException was thrown.");
                throw new TiTASecurityException("Could not save KeyStore. IOException was thrown.\n" + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            log.error("Error while saving the KeyStore. File Could not be fond.");
            throw new TiTASecurityException("Could not save KeyStore. File Could not be found.\n" + e.getMessage());
        }

    }

    /**
     * Marks wheter the Keystore is initialized or not.
     * 
     * @return true if KeyStore is initialized, else false
     */
    private static boolean isKeyStoreInitialized() {
        if (keyStore == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Generates a fresh Certificate for a Users KeyPair.
     * 
     * @param pair the KeyPair to create a Certificate for.
     * @param userName the Issuer of the Certificate
     * @return a 10 Year valid Certificate for the User.
     * @throws TiTASecurityException If an error occurs during the generation Process.
     */
    private static X509Certificate generateV3Certificate(KeyPair pair, String userName) throws TiTASecurityException {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certGen.setIssuerDN(new X500Principal("CN=" + userName + " Certificate"));
        certGen.setNotBefore(new Date(System.currentTimeMillis()));
        certGen.setNotAfter(new Date(System.currentTimeMillis() + VALID_TIME_RANGE));
        certGen.setSubjectDN(new X500Principal("CN=" + userName + " Certificate"));
        certGen.setPublicKey(pair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

        certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(false));
        certGen.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature
            | KeyUsage.keyEncipherment));
        certGen
            .addExtension(X509Extensions.ExtendedKeyUsage, true, new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth));

        X509Certificate targetCertificate = null;
        try {
            targetCertificate = certGen.generate(pair.getPrivate(), "BC");
        } catch (NoSuchProviderException e) {
            log.error("Could create a certificate for: " + userName + ".");
            throw new TiTASecurityException("Error while Generating a Certificate for: " + userName
                + ". Specified provider was not found.\n" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("Could create a certificate for: " + userName + ".");
            throw new TiTASecurityException("Error while Generating a Certificate for: " + userName
                + ". Specified algorithm was not found.\n" + e.getMessage());
        } catch (SignatureException e) {
            log.error("Could create a certificate for: " + userName + ".");
            throw new TiTASecurityException("Error while Generating a Certificate for: " + userName
                + ". Signature is not valid.\n" + e.getMessage());
        } catch (CertificateEncodingException e) {
            log.error("Could create a certificate for: " + userName + ".");
            throw new TiTASecurityException("Error while Generating a Certificate for: " + userName
                + ". Wrong encoding for Signature.\n" + e.getMessage());
        } catch (InvalidKeyException e) {
            log.error("Could create a certificate for: " + userName + ".");
            throw new TiTASecurityException("Error while Generating a Certificate for: " + userName
                + ". The Key is not valid.\n" + e.getMessage());
        }

        return targetCertificate;
    }

    /**
     * checks if File already exists and creates the Folders and Files of the Filestore, if not existant.
     * 
     * @param keyStoreFile the file to check for existency
     * @return true if file exists, else false
     * @throws TiTASecurityException if I/O-Exception occurs
     */
    private static boolean checkExistance(File keyStoreFile) throws TiTASecurityException {
        if (!keyStoreFile.exists()) {
            try {
                // first create Folders
                File dir = new File("security");
                dir.mkdir();

                File subDir = new File("security" + File.separatorChar + "keystore");
                subDir.mkdir();
                // then the file
                keyStoreFile.createNewFile();
                return true;
            } catch (IOException e) {
                log.error("Error while saving the KeyStore. File Could not be created.");
                throw new TiTASecurityException("Could not save KeyStore. File Could not be created.\n"
                    + e.getMessage());
            }
        } else {
            return false;
        }
    }
}