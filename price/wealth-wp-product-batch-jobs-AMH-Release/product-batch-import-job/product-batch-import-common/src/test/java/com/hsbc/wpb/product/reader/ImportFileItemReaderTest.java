package com.dummy.wpb.product.reader;


import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import com.dummy.wpb.product.reader.ImportFileItemReader.productFileComparator;
import org.springframework.batch.item.ResourceAware;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ImportFileItemReaderTest {

    String importFilePath = new ClassPathResource("/import").getFile().getAbsolutePath();

    ImportFileItemReaderTest() throws IOException {
    }

    @BeforeEach
    public void before() {
        File importFile = new File(importFilePath);
        if (importFile.isDirectory()) {
            for (File file : importFile.listFiles()) {
                if (file.getName().endsWith(".bak")) {
                    // restore file name
                    file.renameTo(new File(StringUtils.substringBefore(file.getAbsolutePath(), ".bak")));
                }
            }
        }
    }

    @Test
    void testImport_giveDirectoryPath() throws Exception {
        List<Text> results = read("/import", "txt-(.*).txt");
        Assertions.assertEquals(2, results.size());
        Assertions.assertNotNull(results.get(0).resource);
    }

    @Test
    void testImport_giveFilePath() throws Exception {
        Assertions.assertEquals(1, read("/import/txt-1.txt", "txt-(.*).txt").size());
    }

    @Test
    void testImport_giveNoImportFile() throws Exception {
        Assertions.assertEquals(0, read("/import", "i do not exist").size());
        File importDirectory = new ClassPathResource("/import").getFile();
        File[] bakFiles = importDirectory.listFiles(file -> file.getName().endsWith(".bak"));
        Assertions.assertEquals(0, bakFiles.length);
    }

    @Test
    void testImport_giveNoExistPath() throws Exception {
        FlatFileItemReader<Text> delegate = new FlatFileItemReader<>();
        ImportFileItemReader<Text> reader = new ImportFileItemReader<>("/i do not exist", "txt-(.*).txt", delegate);
        ExecutionContext executionContext = new ExecutionContext();
        reader.update(executionContext);
        Assertions.assertThrows(IllegalArgumentException.class, () -> reader.open(executionContext));
    }

    private List<Text> read(String path, String pattern) throws Exception {
        String filePath = new ClassPathResource(path).getFile().getAbsolutePath();
        FlatFileItemReader<Text> delegate = new FlatFileItemReader<>();
        delegate.setLineMapper((line, lineNumber) -> new Text(line));
        ImportFileItemReader<Text> reader = new ImportFileItemReader<>(filePath, pattern, delegate);
        ExecutionContext executionContext = new ExecutionContext();
        reader.open(executionContext);
        Assertions.assertNull(reader.getCurrentResource());
        List<Text> lines = new ArrayList<>();
        Text line;
        while ((line = reader.read()) != null) {
            lines.add(line);
        }
        Assertions.assertNull(reader.getCurrentResource());
        reader.close();
        return lines;
    }

    private static class Text implements ResourceAware {

        String content;

        public Text(String content) {
            this.content = content;
        }

        Resource resource;

        @Override
        public void setResource(Resource resource) {
            this.resource = resource;
        }
    }


    @Test
    void testproductFileComparator() throws IOException {
        List<File> testedFiles = Lists.newArrayList(new File("xml-1.xml"), new File("xml-2.xml"));
        testedFiles.sort(new productFileComparator());
        Assertions.assertEquals(Arrays.asList(new File("xml-1.xml"), new File("xml-2.xml")), testedFiles);

        testedFiles = Lists.newArrayList(new File("HK_HBAP_AMHGSOPS.AS_SEC~CA_20241201000001.xml"), new File("HK_HBAP_AMHGSOPS.AS_SEC~CA_20241101000001.xml"));
        testedFiles.sort(new productFileComparator());
        Assertions.assertEquals(Arrays.asList(new File("HK_HBAP_AMHGSOPS.AS_SEC~CA_20241101000001.xml"), new File("HK_HBAP_AMHGSOPS.AS_SEC~CA_20241201000001.xml")), testedFiles);

        testedFiles = Lists.newArrayList(new ClassPathResource("/import/txt-2.txt").getFile(), new ClassPathResource("/import/txt-1.txt").getFile());
        testedFiles.sort(new productFileComparator());
        Assertions.assertEquals(Lists.newArrayList(new ClassPathResource("/import/txt-1.txt").getFile(), new ClassPathResource("/import/txt-2.txt").getFile()), testedFiles);
    }
}
