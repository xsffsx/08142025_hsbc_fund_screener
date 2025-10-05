package com.dummy.wmd.wpc.graphql.email;

import com.dummy.wmd.wpc.graphql.constant.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@RunWith(MockitoJUnitRunner.class)
public class EmailFormationAndEmailUtilsTest {

    @InjectMocks
    private EmailFormation emailFormation;

    @InjectMocks
    private EmailUtils emailUtils;

    @Mock
    private DataOutputStream dos;

    private String chmodScriptPath;

    private String rejLogPath;

    @Before
    public void setUp() {
        chmodScriptPath = "src/main/java/com/dummy/wmd/wpc/graphql/script/ChmodScript.java";
        rejLogPath = "src/test/resources/ENS/";
    }

    @Test
    public void testEmailFormation_givenScriptPathAndENSPath_doNotThrow_genENSFileAndDelect() {
        EmailContent emailContent = new EmailContent("xxxxxxxxxx", "xxxxxxxxxxxxxxxxx", "xxxxxxxxxx");
        EmailContent[] emailContents = new EmailContent[]{emailContent};

        try {
            emailFormation.emailFormation("sender", emailContents, chmodScriptPath, rejLogPath);
            delectEmailFiles(rejLogPath);
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    @Test
    public void testEmailFormation_givenLagerSubjectAndNullContent_doNotThrow_genENSFileAndDelect() {
        EmailContent emailContent = new EmailContent("xxxxxxxxxxkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk", "xxxxxxxxxxxxxxxxx", "");
        EmailContent[] emailContents = new EmailContent[]{emailContent};

        try {
            emailFormation.emailFormation("sender", emailContents, chmodScriptPath, rejLogPath);
            delectEmailFiles(rejLogPath);
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
        Assert.assertNotNull(EmailUtils.chkRvcrAdd(null));
    }

    @Test
    public void testEmailUtils_checkDir_givenErrorPath_printIOExceptionLog() {
        try {
            emailUtils.checkDir("////aaa");
        } catch (IOException e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    private void delectEmailFiles(String path) throws IOException {
        File folder = new File(path);
        if (folder.exists()) {
            String[] fileNames = folder.list();
            for (String fileName : fileNames) {
                File file = new File(path, fileName);
                Files.delete(file.toPath());
            }
        }
    }

}
