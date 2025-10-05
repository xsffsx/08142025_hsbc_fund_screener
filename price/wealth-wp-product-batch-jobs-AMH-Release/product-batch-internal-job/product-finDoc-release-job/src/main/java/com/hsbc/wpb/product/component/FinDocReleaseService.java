/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name		FinDocProdRL
 *
 * Creation Date	Mar 8, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Mar 8, 2006
 *    CMM/PPCR No.		
 *    Programmer		Anthony Chan
 *    Description
 * 
 */
package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.error.ErrorLogger;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.*;
import com.dummy.wpb.product.script.ChmodScript;
import com.dummy.wpb.product.utils.FinDocUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.dummy.wpb.product.FinDocReleaseJobApplication.JOB_NAME;
import static com.dummy.wpb.product.error.ErrorCode.OTPSERR_EBJ108;


@Service
@Slf4j
public class FinDocReleaseService {
	
	@Value("${finDoc.pdf.rej}")
    private String pdfRejPath;

    @Value("${finDoc.pdf.aprv}")
    private String pdfAprvPath;

    @Value("${finDoc.pdf.chk}")
    private String pdfChkPath;

	@Value("${finDoc.pdf.done}")
	private String pdfDonePath;

	@Value("${finDoc.S3.region}")
	private String finDocS3Region;

	@Value("${finDoc.S3.bucketName}")
	private String finDocS3BucketName;

	@Value("${finDoc.S3.endPointUrl}")
	private String finDocS3EndPointUrl;

	@Value("${finDoc.Dflt.FsUrl}")
	private String dfltFsUrl;

	@Value("${finDoc.maxNo.PWS}")
	private String maxToPws;

	@Value("${CHMOD.Script}")
	private String chmodScriptPath;


	@Value("${finDoc.spoms.ack}")
	private String finDocSpomsAck;

    @Autowired
    public FinDocReleaseDao finDocReleaseDao;

	public boolean aprvRecHandle(String ctryCde, String orgnCde) throws IOException {
		FinDocULReqPo[] fdq = retrieveProcRejRecord(ctryCde, orgnCde);
		log.info("No of Reject records will be processed : " + fdq.length);

		boolean success = true;

		List<FinDocULReqPo> finDocULReqPos = new ArrayList<>();
		for (int i = 0; i < fdq.length; i++) {
			FinDocSmry fds = setULRQ2FinDocSmry(fdq[i]);
			PDFFileService pdfService = new PDFFileService(fds);

			try{
				File fupd = pdfService.fileToRej(pdfChkPath, pdfRejPath, fds);

				if (fupd != null && fupd.exists())
					fdq[i].setUrlLclSysText(PDFFileService.setFileUrl(fupd, true));

				fdq[i].setDocStatCde(FinDocConstants.REJECT);
				finDocULReqPos.add(fdq[i]);
			} catch (productBatchException e) {
				log.error( e.getStackTrace().getClass() + ":" + e.getMessage());
				success = false;
			}

		}
		if (!finDocULReqPos.isEmpty()) {
			updateUploadReqTOBatch(finDocULReqPos);
		}
		return success;
	}

	public FinDocULReqPo[] retrieveProcRejRecord(String ctryCde, String orgnCde) {
		return finDocReleaseDao.retrieveByStatCde(ctryCde, orgnCde, FinDocConstants.PROC_REJECT);
	}
    
	public FinDocSmry setULRQ2FinDocSmry(FinDocULReqPo finDocULReqPo) {
		FinDocSmry fds = new FinDocSmry();
		
		if (finDocULReqPo.getDocRecvName() != null) {
            fds.setDocIncmName(finDocULReqPo.getDocRecvName().trim());
        }
        fds.setCreatDt(new Date(finDocULReqPo.getRecCreatDtTm().getTime()));
        fds.setCreatTm(new Time(finDocULReqPo.getRecCreatDtTm().getTime()));
        
        return fds;
	}

	public void updateUploadReqTO(FinDocULReqPo finDocULReqPo) throws productBatchException {
		finDocReleaseDao.update(finDocULReqPo);
	}

	public void updateUploadReqTOBatch(List<FinDocULReqPo> finDocULReqPos) {
		finDocReleaseDao.updateBatchFinDocULReq(finDocULReqPos);
	}


	public boolean procAprvHandle(String ctryCde, String orgnCde) throws IOException {
		FinDocHistPo[] fds = retrieveProcAprvRecord(ctryCde,orgnCde);
        
        log.info("No of Approval records will be processed : "
                + fds.length);

		boolean success = true;

		List<FinDocULReqPo> finDocULReqPos = new ArrayList<>();
		List<FinDocHistPo> finDocHistPos = new ArrayList<>();
        for (int i = 0; i < fds.length; i++) {
        	FinDocSmry fd = setHistPo2FinDocSmry(fds[i]);
            PDFFileService pdfService = new PDFFileService(fd);
            File fupd = null;
            try {
                fupd = pdfService.fileToAprvToDone(pdfChkPath,pdfAprvPath);

                if (fupd != null && fupd.exists())
                    fds[i].setUrlLclSysText(PDFFileService.setFileUrl(fupd,
                        true));

                fds[i].setDocStatCde(FinDocConstants.APPROVAL);
				finDocHistPos.add(fds[i]);

                FinDocULReqPo fdurTO = retrieveUploadReqTO(fds[i]);
                fdurTO.setUrlLclSysText(fds[i].getUrlLclSysText().trim());
                fdurTO.setDocStatCde(FinDocConstants.APPROVAL);
				finDocULReqPos.add(fdurTO);

            } catch (productBatchException e) {
                log.error("FinDoc file missing."
                    + e.getStackTrace().getClass() + ":"
                    + e.getMessage());
				success = false;
			}
        }
		if(!finDocHistPos.isEmpty()){
			updateFinDocSmryBatch(finDocHistPos);
		}
		if (!finDocULReqPos.isEmpty()) {
			updateUploadReqTOBatch(finDocULReqPos);
		}
		return success;
	}

	public FinDocHistPo[] retrieveProcAprvRecord(String ctryCde, String orgnCde) {
		return finDocReleaseDao.retrieveFinDocSmryRecordByStatCde(ctryCde, orgnCde, FinDocConstants.PROC_APPROVAL);
	}
    
	public FinDocSmry setHistPo2FinDocSmry(FinDocHistPo finDocHistPo) {
		FinDocSmry fds = new FinDocSmry();
		
		if (finDocHistPo.getDocRecvName() != null) {
            fds.setDocIncmName(finDocHistPo.getDocRecvName().trim());
        }
		fds.setDocSerNum(finDocHistPo.getRsrcItemIdFinDoc());
        
        return fds;
	}

	public void updateFinDocSmryBatch(List<FinDocHistPo> finDocHistPos) throws productBatchException {
		finDocReleaseDao.updateBatchFinDocHist(finDocHistPos);
	}

	public FinDocULReqPo retrieveUploadReqTO(FinDocHistPo finDocHistPo) throws productBatchException {
		return finDocReleaseDao.retrieveByDocSerNum(finDocHistPo);
	}

	public FinDocHistPo[] retrievePendingRecord(String ctryCde, String orgnCde) {
		return finDocReleaseDao.retrieveFinDocSmryRecordByStatusFS(ctryCde, orgnCde, FinDocConstants.PENDING);
	}

	public void updateDB (FinDocHistPo fds) throws productBatchException, IOException {
		FinDocSmry fd = setHistPo2FinDocSmry(fds);
		PDFFileService pdfServ = new PDFFileService(fd);

		File pdfFile = getFile(pdfServ);

		String urlLclSysText = PDFFileService.setFileUrl(pdfFile, true);
		String urlFileServrText = getFSURL(fds);

		fds.setFileDocName(fds.getDocRecvName().trim());
		fds.setUrlLclSysText(urlLclSysText);
		fds.setUrlFileServrText(urlFileServrText);
		fds.setDocServrStatCde(FinDocConstants.CONFIRM);
		updateFinDocSmryAndProdFinDoc(fds);

		FinDocULReqPo fdurTO = retrieveUploadReqTO(fds);
		fdurTO.setFileDocName(fds.getDocRecvName().trim());
		fdurTO.setUrlLclSysText(urlLclSysText);
		fdurTO.setUrlFileServrText(urlFileServrText);
		fdurTO.setDocServrStatCde(FinDocConstants.CONFIRM);
		updateUploadReqTO(fdurTO);

	}

	private File getFile(PDFFileService pdfServ) throws IOException {
        return pdfServ.fileToAprvToDone(pdfAprvPath, pdfDonePath);
	}


	public String getFSURL(FinDocHistPo fds) {
		SystemParmPo sps = finDocReleaseDao.retrieveFinDocSysPramByProdType(fds.getCtryRecCde(), fds.getGrpMembrRecCde(), fds.getDocFinTypeCde(), fds.getDocFinCatCde(), FinDocConstants.FSURL);
		if (sps != null) {
			return sps.getParmValueText().trim();
		}
		sps = finDocReleaseDao.retrieveFinDocSysPramByProdType(fds.getCtryRecCde(), fds.getGrpMembrRecCde(), fds.getDocFinTypeCde(), FinDocConstants.SUBTYPCDE_GN, FinDocConstants.FSURL);
		if (sps != null) {
			return sps.getParmValueText().trim();
		} else {
			return dfltFsUrl;
		}
	}

    public void updateFinDocSmryAndProdFinDoc(FinDocHistPo fds) {
		//to update the fin_doc_hist
		finDocReleaseDao.update(fds);

		//to update the fin_doc table and the prod_fin_doc, prod_type_fin_doc and prod_subtp_fin_doc tables begin
		FinDocPo finDocPo = finDocReleaseDao.retrieveFinDocSmryRecordByDocCdeNLangCdeLatest(fds);

		//finDocSmryTOList.length should be >0 or = 1 only
		if (finDocPo != null) {
			log.info("Record can be found in fin_doc collection: " + fds.getCtryRecCde()
					+ ":" + fds.getGrpMembrRecCde()
					+ ":" + fds.getDocFinTypeCde()
					+ ":" + fds.getDocFinCatCde()
					+ ":" + fds.getDocFinCde()
					+ ":" + fds.getLangFinDocCde()
					+ ":" + fds.getDocStatCde()
					+ ":" + fds.getDocServrStatCde()
					+ ":" + finDocPo.getRsrcItemIdFinDoc());
			finDocReleaseDao.updateLatest(fds);
			finDocReleaseDao.updateProdRLByDocSerNum(finDocPo.getRsrcItemIdFinDoc(), fds.getRsrcItemIdFinDoc());
		} else {
			finDocReleaseDao.insertLatest(fds);
		}
		//to update the fin_doc table and the prod_fin_doc, prod_type_fin_doc and prod_subtp_fin_doc tables end
	}


	boolean isEliAndSmallFile(FinDocHistPo fds, long fileSize) {
		boolean isEli = FinDocConstants.ELI.equalsIgnoreCase(fds.getDocFinCatCde().trim());
		log.info("file type is " + fds.getDocFinCatCde().trim());
		boolean isSmallFile = fileSize <= 2048; // Use stored file size
		log.info("isEli: " + isEli + ", isSmallFile: " + isSmallFile);
		return isEli && isSmallFile;
	}


	protected boolean executeCommand(String command, String errorMessage) throws IOException, InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(command); // NOSONAR

		try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
			String line;
			while ((line = errorReader.readLine()) != null) {
				log.error(line);
			}
		}

		boolean completed = process.waitFor(60, TimeUnit.SECONDS);
		if (!completed) {
			log.error(errorMessage);
			process.destroy();
			return false;
		}

		return process.exitValue() == 0;
	}

	protected boolean copyFileToS3(String copyCommand, String filePath) throws IOException, InterruptedException {
		if (executeCommand(copyCommand, "Copy process timed out for file: " + filePath)) {
			log.info("File successfully copied to S3: " + filePath);
			return true;
		} else {
			log.error("Failed to copy file to S3: " + filePath);
			ErrorLogger.logErrorMsg(OTPSERR_EBJ108, JOB_NAME, filePath);
			return false;
		}
	}

	private void deleteFileFromS3(String deleteCommand, String filePath) throws IOException, InterruptedException {
		if (executeCommand(deleteCommand, "Deletion process timed out for file: " + filePath)) {
			log.info("File successfully deleted from S3: " + filePath);
		} else {
			log.error("Failed to delete file from S3: " + filePath);
			ErrorLogger.logErrorMsg(OTPSERR_EBJ108, JOB_NAME, filePath);
		}
	}

	public int transferToS3(FinDocHistPo fds) throws IOException, InterruptedException {
		File docFile = new File(fds.getUrlLclSysText().trim());
		long fileSize = new File(pdfAprvPath + File.separator + docFile.getName()).length(); // Store file size before copying. The length is 0 if not existed.
		log.info("File size: " + fileSize);
		String docPath = getDocPath(docFile.getName());
		String copyCommand = "aws s3 cp " + pdfAprvPath + File.separator + docFile.getName()
				+ " s3://" + finDocS3BucketName + docPath
				+ " --metadata Content-Type=application/pdf --region " + finDocS3Region
				+ " --sse --endpoint-url " + finDocS3EndPointUrl;
		//log.info("copyCommand: " + copyCommand);

		if (!copyFileToS3(copyCommand, fds.getUrlLclSysText())) {
			return Integer.MAX_VALUE;
		}

		if (isEliAndSmallFile(fds, fileSize)) {  // Pass file size instead of checking existence
			log.info("Conditions met for deleting file from S3: " + docPath);
			String deleteCommand = "aws s3 rm s3://" + finDocS3BucketName + docPath
					+ " --region " + finDocS3Region
					+ " --endpoint-url " + finDocS3EndPointUrl;
			log.info("deleteCommand: " + deleteCommand);
			deleteFileFromS3(deleteCommand, docPath);
		}else {
			log.info("Conditions not met for deleting file from S3: " + docPath);
		}

		return 0;
	}

	String getDocPath(String docName) {

		String docPath;
		String newName =  docName.substring(docName.indexOf(".") + 1 );

		if(newName.toLowerCase().startsWith("ut")) {
			docPath =  "unit" ;
		} else {
			docPath = newName.toLowerCase().substring(0,newName.indexOf("_")) ;
		}
		return docPath + File.separator + newName;

	}

	public boolean releaseFilesAndUpdateDB(String ctryRecCde, String grpMembrRecCde)  {

		FinDocHistPo[] fds = retrievePendingRecord(ctryRecCde, grpMembrRecCde);

		int noOfRecAvb = Integer.parseInt(maxToPws);

		int available = Math.min(fds.length, noOfRecAvb);
		log.info("No of records will be processed to S3 : "
				+ available);
		boolean success = true;
		for (int i = 0; i < available; i++) {
			FinDocHistPo oneFds = fds[i];

			try {
				log.info("Start to transfer to S3 bucket for " + oneFds.getUrlLclSysText() );
				if (transferToS3(oneFds) == 0) {

					updateDB(oneFds);

					if (needCreateAck(oneFds)) {
						createAck(finDocSpomsAck, oneFds);
					}

				}
			} catch (IOException e) {
				log.error("Error when releasing file " + oneFds.getUrlLclSysText() + " : " + e.getMessage());
				success =  false;
			} catch (InterruptedException e) {
				log.error("Error in transferring to S3 bucket for " + oneFds.getUrlLclSysText() + " : " + e.getMessage());
				Thread.currentThread().interrupt();
				success =  false;
			} catch (productBatchException e) {
            	log.error("Error in processing file " + oneFds.getUrlLclSysText() + " : " + e.getMessage());
            	success = false;
        }

		}
		return success;
	}

	protected boolean needCreateAck(FinDocHistPo fds) {
		String docFinCatCde = fds.getDocFinCatCde().trim().toUpperCase();
		String docFinTypeCde = fds.getDocFinTypeCde().trim().toUpperCase();

		boolean isEli = docFinCatCde.equals(FinDocConstants.ELI) &&
				StringUtils.equalsAny(docFinTypeCde, FinDocConstants.DOC_TYP_TS, FinDocConstants.DOC_TYP_PS);

		boolean isSn = docFinCatCde.equals(FinDocConstants.SN) && docFinTypeCde.equals(FinDocConstants.DOC_TYP_TS);

		boolean isSid = docFinCatCde.equals(FinDocConstants.SID) && docFinTypeCde.equals(FinDocConstants.DOC_TYP_AP);

		return isEli || isSn || isSid;
	}

	protected void createAck(String filename2, FinDocHistPo fds) throws IOException {
		String inName = fds.getDocRecvName().trim();

		String ackName = null;
		String ext = getName(inName);

		if (supportedSuffixes.contains(ext)) {
			ackName = Paths.get(filename2, StringUtils.replace(inName, ext, FinDocConstants.SUFFIX_ACK)).toString();

			FinDocUtils.createList(ackName);
		}else{
			log.info( "Error : Invalid format type: " + inName + " can not be accepted");
		}

		log.info("SPOMS ack: " + ackName + " created");
		ChmodScript.chmodScript(ackName, "777", chmodScriptPath);
	}

	private String getName(String name) {
		int loc = name.toLowerCase().lastIndexOf(".");
		return (loc >= 0) ? name.substring(loc) : name;
	}
	protected static List<String> supportedSuffixes = Arrays.asList(
			FinDocConstants.SUFFIX_PDF,
			FinDocConstants.SUFFIX_DOC,
			FinDocConstants.SUFFIX_XLS,
			FinDocConstants.SUFFIX_TIF,
			FinDocConstants.SUFFIX_TIFF,
			FinDocConstants.SUFFIX_GIF,
			FinDocConstants.SUFFIX_JPEG,
			FinDocConstants.SUFFIX_JPG,
			FinDocConstants.SUFFIX_PPT,
			FinDocConstants.SUFFIX_MP3,
			FinDocConstants.SUFFIX_MP4,
			FinDocConstants.SUFFIX_ZIP,
			FinDocConstants.SUFFIX_TXT,
			FinDocConstants.SUFFIX_VSD,
			FinDocConstants.SUFFIX_HTML,
			FinDocConstants.SUFFIX_XML
	);
}