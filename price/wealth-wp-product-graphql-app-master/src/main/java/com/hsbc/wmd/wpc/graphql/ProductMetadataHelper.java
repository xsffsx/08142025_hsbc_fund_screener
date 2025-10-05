package com.dummy.wmd.wpc.graphql;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Data
@Component
public class ProductMetadataHelper {
    @Autowired
    private GraphQLSchemaGenerator graphQLSchemaGenerator;

    private URL productInputSchema;
    private URL productOutputSchema;

    @PostConstruct
    public void init() throws IOException {
        // Generate product schema against metadata, save as <java.io.tmpdir>/wpc-schema.graphqls
        String outputSdl = graphQLSchemaGenerator.generateProductTypeSchema();
        String inputSdl = graphQLSchemaGenerator.generateProductInputSchema();
        String tmpdir = System.getProperty("java.io.tmpdir");

        Path outputSdlPath = Paths.get(tmpdir, "product-output-schema.graphqls");
        Files.write(outputSdlPath, outputSdl.getBytes());
        productOutputSchema = outputSdlPath.toUri().toURL();
        log.info("Product Output Schema Generated: " + outputSdlPath);

        Path inputSdlPath = Paths.get(tmpdir, "product-input-schema.graphqls");
        Files.write(inputSdlPath, inputSdl.getBytes());
        productInputSchema = inputSdlPath.toUri().toURL();
        log.info("Product Input Schema Generated: " + inputSdlPath);
    }
}
