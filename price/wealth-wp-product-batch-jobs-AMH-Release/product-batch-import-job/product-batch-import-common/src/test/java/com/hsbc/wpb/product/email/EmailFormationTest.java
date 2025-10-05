package com.dummy.wpb.product.email;

import com.dummy.wpb.product.constant.EmailContent;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmailFormationTest {

    private EmailFormation emailFormation = new EmailFormation();

    private EmailUtils emailUtils = new EmailUtils();

    private String chmodScriptPath;

    private String rejLogPath;

    @Before
    public void setUp() throws IOException {
        chmodScriptPath = new ClassPathResource("/test/ChmodScript.java").getFile().getAbsolutePath();
        rejLogPath = new ClassPathResource("/test").getFile().getAbsolutePath();
    }

    @Test
    public void testEmailFormation_givenScriptPathAndENSPath_doNotThrow_genENSFile() {
        EmailContent emailContent = new EmailContent("xxxxxxxxxx", "xxxxxxxxxxxxxxxxx", "xxxxxxxxxx");
        EmailContent[] emailContents = new EmailContent[]{emailContent};

        try {
            emailFormation.emailFormation("sender", emailContents, chmodScriptPath, rejLogPath);
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    @Test
    public void testEmailFormation_givenLagerSubjectAndNullContent_doNotThrow_genENSFile() {
        EmailContent emailContent = new EmailContent("xxxxxxxxxxkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk", "xxxxxxxxxxxxxxxxx", "");
        EmailContent[] emailContents = new EmailContent[]{emailContent};

        try {
            emailFormation.emailFormation("sender", emailContents, chmodScriptPath, rejLogPath);
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    @Test
    public void testEmailFormation_givenErrorPath_printIOExceptionLog() {
        try {
            emailFormation.emailFormation("sender", new EmailContent[]{}, chmodScriptPath, "///EEE/");
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    @Test
    public void testEmailUtils_chkRvcrAdd_givenNull_returnNull() {
        Assert.assertEquals("#WPC_SUPPORT", EmailUtils.chkRvcrAdd(null));
    }

    @Test
    public void testEmailUtils_checkDir_givenErrorPath_printIOExceptionLog() {
        try {
            emailUtils.checkDir("////aaa");
        } catch (IOException e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

}