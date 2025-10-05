//package com.hhhh.group.secwealth.mktdata.test;
//
//import static org.junit.Assert.assertEquals;
//
//import java.io.File;
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.apache.commons.io.FileUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import com.intuit.karate.Results;
//import com.intuit.karate.Runner;
//
//import net.masterthought.cucumber.Configuration;
//import net.masterthought.cucumber.ReportBuilder;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ApplicationTest.class, properties = {
//		"server.port=3000" }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@ActiveProfiles("test")
//public class KarateApiTest {
//	
//	@Test
//	public void test() {
//		Results results = Runner.path("classpath:features").tags("@api-test").parallel(5);
//		String[] fileSuffixes = { "json" };
//		List<String> filePathList = filterJsonFile(results.getReportDir(), fileSuffixes, true);
//		Configuration config = new Configuration(new File("target"), "mds utb Service API Test");
//		ReportBuilder reportBuilder = new ReportBuilder(filePathList, config);
//		reportBuilder.generateReports();
//		assertEquals(results.getFailCount(), 0);
//	}
//
//	private List<String> filterJsonFile(String filePath, String[] fileSuffixes, boolean IsSearchSubFolder) {
//		Collection<File> jsonFiles = FileUtils.listFiles(new File(filePath), fileSuffixes, IsSearchSubFolder);
//		return jsonFiles.stream().map(it -> it.getAbsolutePath()).collect(Collectors.toList());
//	}
//
//}