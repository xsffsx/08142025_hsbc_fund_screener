package com.dummy.wpb.product.utils;

import com.dummy.wpb.product.ImportTermsheetService;
import com.dummy.wpb.product.configuration.TermsheetConfiguration;
import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.TermsheetFile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SidTsFinDocUtil {

    public static void postProcess(File directory, String ctryRecCde, String grpMembrRecCde, String prodTypeCde, ImportTermsheetService importTermsheetService,
                            TermsheetConfiguration termsheetConfiguration) throws Exception {
        if (directory.isDirectory()) {
            // list all *.ack files
            List<File> ackFiles = CommonUtils.scanFileInPath(directory, "", BatchConstants.FILE_SUFFIX_ACK);
            for (File ackFile : ackFiles) {
                // check valid ack files, sample: sid_ap_FH9450_bl.ack
                if (!importTermsheetService.isValidIncomingFile(directory, ackFile)) {
                    continue;
                }
                processAckFile(ackFile, directory, ctryRecCde, grpMembrRecCde, prodTypeCde, importTermsheetService, termsheetConfiguration);
            }
        }
    }

    public static void processAckFile(File ackFile, File directory, String ctryRecCde, String grpMembrRecCde, String prodTypeCde, ImportTermsheetService importTermsheetService,
                TermsheetConfiguration termsheetConfiguration) throws Exception {
        try (FileChannel channel = FileChannel.open(ackFile.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            FileLock lock = channel.tryLock();
            try {
                processTermsheet(ackFile, directory, ctryRecCde, grpMembrRecCde, prodTypeCde, importTermsheetService, termsheetConfiguration);
            } finally {
                if (lock != null) {
                    lock.release();
                }
            }
        } catch (OverlappingFileLockException e) {
            log.warn("Skip locked file {}", ackFile.getName());
        } catch (NoSuchFileException e) {
            log.warn("Skip processed file {}", ackFile.getName());
        }
    }

    private static void processTermsheet(File ackFile, File directory, String ctryRecCde, String grpMembrRecCde, String prodTypeCde, ImportTermsheetService importTermsheetService,
                TermsheetConfiguration termsheetConfiguration) throws Exception {
        String[] filenameArray = ackFile.getName().split(BatchConstants.UNDERLINE);
        String pdfName = ackFile.getName().replace(BatchConstants.FILE_SUFFIX_ACK, BatchConstants.FILE_SUFFIX_PDF);
        List<File> pdfFiles = CommonUtils.scanFileInPath(directory, pdfName, BatchConstants.FILE_SUFFIX_PDF);

        log.info("Processing incoming term sheet {}", pdfName);
        String docFinTypeCde = termsheetConfiguration.getDocTypeMap().get(filenameArray[1].toLowerCase());
        String prodAltPrimNum = filenameArray[2];
        String langFinDocCde = filenameArray[3].split("\\.")[0].toUpperCase();
        TermsheetFile file = new TermsheetFile(
                pdfFiles.get(0),
                BatchConstants.FILE_SUFFIX_PDF,
                docFinTypeCde,
                langFinDocCde,
                ctryRecCde,
                grpMembrRecCde,
                prodTypeCde,
                prodAltPrimNum);

        Document originalProduct = importTermsheetService.queryProductByCode(ctryRecCde, grpMembrRecCde, prodTypeCde, prodAltPrimNum);
        if (originalProduct == null) {
            log.warn("Product not found (CTRY_REC_CDE: {}, GRP_MEMBR_REC_CDE: {}, PROD_TYPE_CDE: {}, PROD_ALT_PRIM_NUM: {}) ",
                    ctryRecCde, grpMembrRecCde, prodTypeCde, prodAltPrimNum);
        } else {
            importTermsheetService.processTermsheetData(originalProduct, file, ackFile);
            int exitValue = transferToS3(termsheetConfiguration, ctryRecCde, grpMembrRecCde, prodTypeCde, pdfName);
            if (exitValue == 0) {
                log.info("Transfer to S3 successful");
            }
        }
    }

    public static int transferToS3(TermsheetConfiguration termsheetConfiguration, String ctryRecCde, String grpMembrRecCde, String prodTypeCde, String fileName) {
        int exitValue = Integer.MAX_VALUE;
        String localFile = termsheetConfiguration.getLocalPath()
                + File.separator + ctryRecCde + grpMembrRecCde
                + File.separator + prodTypeCde
                + File.separator + fileName;
        String remoteFile = termsheetConfiguration.getS3bucket()
                + File.separator + termsheetConfiguration.getS3path()
                + File.separator + prodTypeCde.toLowerCase()
                + File.separator + fileName;
        String command = "aws s3 cp " + localFile
				+ " " + remoteFile
				+ " --metadata Content-Type=application/pdf --region " + termsheetConfiguration.getS3region()
				+ " --sse --endpoint-url " + termsheetConfiguration.getS3url();
        log.info("Running command: {}", command);
        try {
			Runtime rtime = Runtime.getRuntime();
			Process child = rtime.exec(command);//NOSONAR

			BufferedReader errorReader = new BufferedReader(new InputStreamReader((child.getErrorStream())));
			String line;
			while ( (line = errorReader.readLine()) != null) {
				log.error(line);
			}
			boolean completed = child.waitFor(60, TimeUnit.SECONDS);
			if (completed) {
                exitValue = child.exitValue();
			} else {
				log.info("Transfer to S3 timed out after 60 seconds");
				child.destroy();
			}
		} catch (IOException e) {
			throw new productBatchException("IOException:" + e.getMessage(), e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new productBatchException("InterruptedException:" + e.getMessage(), e);
		}
        return exitValue;
    }
}
