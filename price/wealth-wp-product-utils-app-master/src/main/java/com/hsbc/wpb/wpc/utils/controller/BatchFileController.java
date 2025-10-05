package com.dummy.wpb.wpc.utils.controller;

import com.dummy.wpb.wpc.utils.model.BatchFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/api")
public class BatchFileController {

    @Value("${product.log.path}")
    private String rootPath;

    private static final Logger log = LoggerFactory.getLogger(BatchFileController.class);


    @GetMapping("/listFiles")
    public ResponseEntity<Object> listFilesInDirectory(@RequestParam(required = false) String path) {
        try {
            File directory = (path == null || path.isEmpty())
                ? new File(rootPath)
                : new File(path);

           String canonicalDestinationPath = directory.getCanonicalPath();
            if (!canonicalDestinationPath.startsWith(new File(rootPath).getCanonicalPath())) {
                throw new IOException("Entry is outside of the target directory");
            }

            if (!directory.exists() || !directory.isDirectory()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Directory not found");
            }
            File[] files = directory.listFiles();
            if (files == null) {
                return ResponseEntity.ok(new ArrayList<>());
            }
            return ResponseEntity.ok(Stream.of(files)
                 .filter(file ->  file.getAbsolutePath().startsWith("/appvol/product"))
                 .map(BatchFile::new)
                 .collect(Collectors.toList()));
        } catch (IOException e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Download Batch file with given parameters
     *
     * @param filePath
     */
    @GetMapping(value = "/download")
    public void downloadBatchFile(@RequestParam("filePath") String filePath,
            HttpServletResponse response) {
        try {
            File file = new File(filePath);

            String canonicalDestinationPath = file.getCanonicalPath();

            if (!canonicalDestinationPath.startsWith(new File(rootPath).getCanonicalPath())) {
                throw new IOException("Entry is outside of the target directory");
            }
            // Validate the file
            if (!file.exists() || file.isDirectory()) {
                response.setStatus(404);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
            }
            // Set response headers for file download
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            response.setContentType("application/octet-stream");
            response.setContentLengthLong(file.length());

            // Stream the file content to the response
            try (OutputStream out = response.getOutputStream()) {
                Files.copy(file.toPath(), out);
            }
        } catch (IOException e) {
            response.setStatus(500);
            log.error("Error reading file: {}", e.getMessage(), e);
        }
    }

}
