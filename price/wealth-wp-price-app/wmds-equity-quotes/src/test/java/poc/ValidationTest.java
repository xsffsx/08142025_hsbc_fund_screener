package poc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hhhh.group.secwealth.mktdata.starter.validation.configuration.ValidationAutoConfiguration;


public class ValidationTest {

    @Test
    public void testLoad() {
        String testStr = "asdfasdfasdf<p>sdfsdf<br/> &nbsp; sadfsdfa";
        testStr = removeCdataTag(testStr);
        System.out.println(testStr);
        Assert.assertNotNull(testStr);
    }

    public static String removeCdataTag( String newsContent){
        newsContent=newsContent.replaceAll("<\\!\\[CDATA\\[", "")
                .replaceAll("\\]\\]>", "").replace("\t", "").replace("’", "'")
                .replace("\n", "")
                .replace("<Vp>", "")
                .replaceAll("<[^>]+>", "");
        return newsContent;
    }
}
