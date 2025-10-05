package com.hhhh.group.secwealth.mktdata.test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import lombok.extern.slf4j.Slf4j;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RunWith(Cucumber.class)
@CucumberOptions(
//        tags = "not @news",
        plugin = {"pretty", "json:target/cucumber.json"},
        features = {"classpath:features/"})
public class CucumberIT {

    @AfterClass
    public static void generateHtmlReport() throws IOException {
        File reportOutputDirectory = new File("target");
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target/cucumber.json");

        String buildNumber = "1";
        String projectName = "wealth-wp-price-feature-test";

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        // optional configuration - check javadoc for details
        configuration.addPresentationModes(PresentationMode.EXPAND_ALL_STEPS);
        // do not make scenario failed when step has status SKIPPED
        configuration.setNotFailingStatuses(Collections.singleton(Status.SKIPPED));
        configuration.setBuildNumber(buildNumber);
        // addidtional metadata presented on main page
        configuration.addClassifications("Platform", "Windows");
        configuration.addClassifications("Browser", "Firefox");
        configuration.addClassifications("Branch", "release/1.0");

        // optionally specify qualifiers for each of the report json files
        configuration.addPresentationModes(PresentationMode.PARALLEL_TESTING);

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();

        mergeResourcesInHtml(reportOutputDirectory);
    }

    private static void mergeResourcesInHtml(File reportOutputDirectory) throws IOException {
        File reportFile = new File(reportOutputDirectory.getAbsolutePath() + File.separatorChar + "cucumber-html-reports");
        // merge results
        String jqueryJs = FileUtils.readFileToString(new File(reportOutputDirectory, "cucumber-html-reports" + File.separatorChar + "js" + File.separatorChar + "jquery.min.js"), "UTF-8");
        String tablesorterJs = FileUtils.readFileToString(new File(reportOutputDirectory, "cucumber-html-reports" + File.separatorChar + "js" + File.separatorChar + "jquery.tablesorter.min.js"), "UTF-8");
        String chartJs = FileUtils.readFileToString(new File(reportOutputDirectory, "cucumber-html-reports" + File.separatorChar + "js" + File.separatorChar + "Chart.min.js"), "UTF-8");
        String momentJs = FileUtils.readFileToString(new File(reportOutputDirectory, "cucumber-html-reports" + File.separatorChar + "js" + File.separatorChar + "moment.min.js"), "UTF-8");
        String bootstrapJs = FileUtils.readFileToString(new File(reportOutputDirectory, "cucumber-html-reports" + File.separatorChar + "js" + File.separatorChar + "bootstrap.min.js"), "UTF-8");

        String bootstrapCss = FileUtils.readFileToString(new File(reportOutputDirectory, "cucumber-html-reports" + File.separatorChar + "css" + File.separatorChar + "bootstrap.min.css"), "UTF-8");
        String cucumberCss = FileUtils.readFileToString(new File(reportOutputDirectory, "cucumber-html-reports" + File.separatorChar + "css" + File.separatorChar + "cucumber.css"), "UTF-8");
        String fontCss = FileUtils.readFileToString(new File(reportOutputDirectory, "cucumber-html-reports" + File.separatorChar + "css" + File.separatorChar + "font-awesome.min.css"), "UTF-8");

        for (File file : reportFile.listFiles()) {
            if (file.getName().endsWith(".html")) {
                String html = FileUtils.readFileToString(file, "UTF-8");
                html = html.replace("<script src=\"js/jquery.min.js\"></script>", "<script>" + jqueryJs + "</script>");
                html = html.replace("<script src=\"js/jquery.tablesorter.min.js\"></script>", "<script>" + tablesorterJs + "</script>");
                html = html.replace("<script src=\"js/Chart.min.js\"></script>", "<script>" + chartJs + "</script>");
                html = html.replace("<script src=\"js/moment.min.js\"></script>", "<script>" + momentJs + "</script>");
                html = html.replace("<script src=\"js/bootstrap.min.js\"></script>", "<script>" + bootstrapJs + "</script>");

                html = html.replace("<link rel=\"stylesheet\" href=\"css/bootstrap.min.css\" type=\"text/css\"/>", "<style>" + bootstrapCss + "</style>");
                html = html.replace("<link rel=\"stylesheet\" href=\"css/cucumber.css\" type=\"text/css\"/>", "<style>" + cucumberCss + "</style>");
                html = html.replace("<link rel=\"stylesheet\" href=\"css/font-awesome.min.css\"/>", "<style>" + fontCss.replaceAll("\\.\\./", "") + "</style>");
                html = html.replace("<link rel=\"shortcut icon\" href=\"images/favicon.png\"/>", "");
                FileUtils.write(file, html, "UTF-8");
            }
        }
    }

}
