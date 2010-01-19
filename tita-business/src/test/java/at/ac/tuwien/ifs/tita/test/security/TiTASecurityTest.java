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
package at.ac.tuwien.ifs.tita.test.security;

import static org.junit.Assert.fail;

import java.security.NoSuchAlgorithmException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.ifs.tita.business.security.TiTASecurity;

/**
 * Security Testcases.
 * 
 * @author rene
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:datasourceContext-test.xml" })
@TransactionConfiguration
@Transactional
public class TiTASecurityTest extends AbstractTransactionalJUnit4SpringContextTests {

    /**
     * Test.
     */
    @Test
    public void testCalcHash() {
        try {
            String pwd = "pwd";
            String hash = "oRWenfNnDVSdBFJFMmKfVHfOt97sm0XkfowAlQbsssg=";
            Assert.assertEquals(hash, TiTASecurity.calcHash(pwd));
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }

    /**
     * Test saving and Restoring passwords.
     */
    @Test
    public void testEncryptingDecryptingPasswords() {
        // String originalPassword = "PasswordToEncrypt";
        // String userName = "testUser";
        // String userPassword = "testPassword";
        // String encryptedPassword = "";
        // String decryptedPassword = "";
        //
        // try {
        // encryptedPassword = TiTASecurity.getEncryptedPassword(userName,
        // userPassword, originalPassword);
        // decryptedPassword = TiTASecurity.getDecryptedPassword(userName,
        // userPassword, encryptedPassword);
        //
        // Assert.assertEquals(originalPassword, decryptedPassword);
        //
        // } catch (TiTASecurityException e) {
        // Assert.fail();
        // }
    }

    /**
     * Tests restoring a Password from a Non-Existant User.
     */
    @Test
    public void testRestoringPasswordOfNonExistantUser() {
        // boolean pass = false;
        // try {
        // TiTASecurity.getDecryptedPassword("NonExistingUser", "test",
        // "fooPassword");
        // } catch (TiTASecurityException e) {
        // pass = true;
        // }
        //
        // Assert.assertTrue(pass);
    }
}
