package com.dummy.wpb.product.utils;

import com.dummy.wpb.product.component.ImportEliFinDocService;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.error.ErrorLogger;
import com.dummy.wpb.product.model.MergeFileParams;
import com.dummy.wpb.product.model.PdfDocumentParams;
import com.dummy.wpb.product.model.ProductTo;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.dummy.wpb.product.error.ErrorCode.OTPSERR_EBJ111;

@Component
@Slf4j
public class EliTsFinDocUtil {

	@Value("${CMB.DCDC.ELI.PROD_SUB_TP_CDE}")
	private String cmbDcdcEliProdSubtpCde;

	@Value("${CMB.PPR.ELI.PROD_SUB_TP_CDE}")
	private String cmbPprEliProdSubtpCde;

	@Value("${CMB.PPR.PPN.PROD_SUB_TP_CDE}")
	private String cmbPprPpnProdSubtpCde;

	@Value("${CMB.UPOUT.PPN.PROD_SUB_TP_CDE}")
	private String cmbUpoutPpnProdSubtpCde;

	@Value("${CMB.STPDN.PPN.PROD_SUB_TP_CDE}")
	private String cmbStpdnPpnProdSubtpCde;

	@Value("${CMB.FCN.PPN.PROD_SUB_TP_CDE}")
	private String cmbFcnPpnProdSubtpCde;

	@Value("${CMB.TWSF.PPN.PROD_SUB_TP_CDE}")
	private String cmbTwsfPpnProdSubtpCde;

	@Value("${PDF.IMPORTANT_FILE_FOR_DCDC_ELI}")
	private String pdfImportantFileForDcdcEli;

	@Value("${PDF.IMPORTANT_FILE_FOR_PPR_ELI}")
	private String pdfImportantFileForPprEli;

	@Value("${PDF.IMPORTANT_FILE_FOR_PPR_PPN_1}")
	private String pdfImportantFileForPprPpn1;

	@Value("${PDF.IMPORTANT_FILE_FOR_PPR_PPN_2}")
	private String pdfImportantFileForPprPpn2;

	@Value("${PDF.IMPORTANT_FILE_FOR_PPN_FCN_1}")
	private String pdfImportantFileForPpnFcn1;

	@Value("${PDF.IMPORTANT_FILE_FOR_PPN_FCN_2}")
	private String pdfImportantFileForPpnFcn2;

	@Value("${PDF.IMPORTANT_FILE_FOR_PPN_STPDWN_1}")
	private String pdfImportantFileForPpnStpdwn1;

	@Value("${PDF.IMPORTANT_FILE_FOR_PPN_STPDWN_2}")
	private String pdfImportantFileForPpnStpdwn2;

	@Value("${PDF.IMPORTANT_FILE_FOR_PPN_UPOUT_1}")
	private String pdfImportantFileForPpnUpout1;

	@Value("${PDF.IMPORTANT_FILE_FOR_PPN_UPOUT_2}")
	private String pdfImportantFileForPpnUpout2;

	@Value("${PDF.IMPORTANT_FILE_FOR_PPN_TWSF_1}")
	private String pdfImportantFileForPpnTwsf1;

	@Value("${PDF.IMPORTANT_FILE_FOR_PPN_TWSF_2}")
	private String pdfImportantFileForPpnTwsf2;

	@Value("${PDF.IMPORTANT_FILE_FOR_DCDC}")
	private String pdfImportantFileForDcdc;

	@Value("${PDF.IMPORTANT_FILE_FOR_PPR}")
	private String pdfImportantFileForPpr;

	@Value("${PDF.IMPORTANT_FILE_FOR_STPDN}")
	private String pdfImportantFileForStpdn;

	@Value("${PDF.IMPORTANT_FILE_FOR_UPOUT}")
	private String pdfImportantFileForUpout;

	@Value("${PDF.IMPORTANT_FILE_FOR_FCN}")
	private String pdfImportantFileForFcn;

	@Value("${PDF.IMPORTANT_FILE_FOR_TWSF}")
	private String pdfImportantFileForTwsf;

	@Value("${PDF.IMPORTANT_FILE_FOR_CFFN}")
	private String pdfImportantFileForCffn;

	@Value("${PDF.IMPORTANT_FILE_FOR_DCNFORPP}")
	private String pdfImportantFileForDcnforpp;

	@Value("${PDF.IMPORTANT_FILE_FOR_DCNFORNPP}")
	private String pdfImportantFileForDcnfornpp;

	@Value("${PDF.IMPORTANT_FILE_FOR_PNOTE}")
	private String pdfImportantFileForPnote;

	@Value("${PDF.IMPORTANT_FILE_FOR_TWAC}")
	private String pdfImportantFileForTwac;

	@Value("${PDF.IMPORTANT_FILE_FOR_RAN}")
	private String pdfImportantFileForRan;

	@Value("${PDF.IMPORTANT_FILE_FOR_STEEPENER}")
	private String pdfImportantFileForSteepener;

	@Value("${PDF.IMPORTANT_FILE_FOR_UPOUTELI}")
	private String pdfImportantFileForUpoutEli;

	@Value("${PDF.IMPORTANT_FILE_FOR_FCELI}")
	private String pdfImportantFileForFceli;

	@Value("${PDF.IMPORTANT_FILE_FOR_INVERSE}")
	private String pdfImportantFileForInverse;

	@Value("${PDF.IMPORTANT_FILE_FOR_SBN}")
	private String pdfImportantFileForSbn;

	@Value("${PDF.IMPORTANT_FILE_FOR_LINEAR}")
	private String pdfImportantFileForLinear;

	@Value("${PDF.IMPORTANT_FILE_FOR_FXN}")
	private String pdfImportantFileForFxn;

	@Value("${PDF.IMPORTANT_FILE_FOR_FXWCN}")
	private String pdfImportantFileForFxwcn;

	@Value("${PDF.IMPORTANT_FILE_FOR_BEN}")
	private String pdfImportantFileForBen;

	@Value("${PDF.IMPORTANT_FILE_FOR_BENELI}")
	private String pdfImportantFileForBenEli;

	@Value("${PDF.IMPORTANT_FILE_FOR_APENDIX1}")
	private String pdfImportantFileForApendix1;

	@Value("${PDF.IMPORTANT_FILE_FOR_APENDIX2}")
	private String pdfImportantFileForApendix2;

	@Value("${PDF.IMPORTANT_FILE_FOR_APENDIX3}")
	private String pdfImportantFileForApendix3;

	@Value("${PDF.IMPORTANT_FILE_FOR_APENDIX4}")
	private String pdfImportantFileForApendix4;

	@Value("${PDF.IMPORTANT_FILE_FOR_APENDIX5}")
	private String pdfImportantFileForApendix5;

	@Value("${ISSUER.INFO.UNDL_TYPE_CODE}")
	private String isrInfoUndlTypeCde;

	@Value("${Chmod.Script}")
	private String chmodScript;

	@Value("${Chown.Script}")
	private String chownScript;

	@Value("${ELI.S3.Script}")
	private String eliFindocScript;

	@Value("${finDoc.eli.url}")
	private String findocEliUrl;

	@Value("${PDF.basePath}")
	private String pdfBasePath;

	private static int maxRetryCount = 3;
	private static Pattern ackFilePattern = Pattern.compile("\\w+_(FINAL_)?(TERMSHEETS|PRCSUPPL)_\\w+\\.ack");//NOSONAR

	private static String mergeFileError = "Merge pdf file error ";

	private static String renameFileError = "the File rename from {} change to {} error, please re-try";

	public void handleFinDocFile(File ackFile, String ctry, String group, String prodType, ImportEliFinDocService importEliFinDocService,
										boolean isPostProc) throws Exception {
		String ackfileName = ackFile.getName();
		String pdfFileName = ackfileName.replace(FinDocConstants.SUFFIX_ACK, FinDocConstants.SUFFIX_PDF);
		File pdfFile = new File(new File(ackFile.getParent()).getCanonicalPath(), pdfFileName);
		Matcher ackFileMatcher = ackFilePattern.matcher(ackfileName);
		if (inValidAckAndPdf(pdfFile, ackFileMatcher)) {
			log.error("The pdf file does not exist or the ack file name is not standard. " + ackfileName);
			return;
		}
		log.info("Start to handle file " + ackfileName);
		String[] fileStr = pdfFileName.split(FinDocConstants.UNDERLINE);
		String tProdAltPrimNum = null;
		String tFinDocType = null;
		if (pdfFileName.contains("FINAL")) {
			tProdAltPrimNum = fileStr[0];
			tFinDocType = "FINA" + fileStr[2];
		} else {
			tProdAltPrimNum = fileStr[0];
			tFinDocType = fileStr[1];
		}

		ProductTo prod = new ProductTo();
		prod.setCtryRecCde(ctry);
		prod.setGrpMembrRecCde(group);
		prod.setProdTypeCde(prodType);
		prod.setProdAltPrimNum(tProdAltPrimNum);
		// for post process no need to retry and no need to wait product creation
		int maxRetryCnt = getMaxRetryCnt(isPostProc);
		for (int cnt = 0; cnt < maxRetryCnt; cnt++) {
			Document productTO = importEliFinDocService.queryProductByPriNum(prod);
			if (productTO != null && productTO.getInteger(Field.prodId) != null) {
				Map<String, Object> eqtyLinkInvst = JsonPathUtils.readValue(productTO, Field.eqtyLinkInvst);
				List<Map<String, Object>> undlAsets = JsonPathUtils.readValue(productTO, Field.undlAset);
				boolean isrInfoDoc = undlAsets.stream()
						.filter(Objects::nonNull)
						.map(undlAset -> undlAset.get(Field.asetUndlCde))
						.anyMatch(asetUndlCde -> isrInfoUndlTypeCde.contains(asetUndlCde.toString()));
				log.debug("Issuer info doc is ' " + isrInfoDoc + " '.");
				BatchCommonUtil.renameFile(ackFile, FinDocConstants.SUFFIX_BAK);
				boolean flag = false;
				Double cptlProtcPct = (Double) eqtyLinkInvst.get(Field.cptlProtcPct);
				if(null != cptlProtcPct && cptlProtcPct > 0) {
					flag = true;
				}
				String eliPayoffTypeCde = (String) eqtyLinkInvst.get(Field.eliPayoffTypeCde);
				Double distbrFeePct = (Double) eqtyLinkInvst.get(Field.distbrFeePct);
				String prodOrig = (String) eqtyLinkInvst.get(Field.prodOrig);
				String eliIssuer = (String) eqtyLinkInvst.get(Field.eliIssuer);
				String custTier = (String) eqtyLinkInvst.get(Field.custTier);
				mergePdfDocument(new PdfDocumentParams(pdfFile, pdfFileName, productTO.getString(Field.prodSubtpCde),
						flag, isrInfoDoc, eliPayoffTypeCde, distbrFeePct, prodOrig, tProdAltPrimNum, eliIssuer, custTier));
				transferToFinServer(ctry, group, pdfFile);

				importEliFinDocService.updateToDB(productTO, tFinDocType, findocEliUrl + pdfFileName);
				return;
			} else if (!isPostProc) {
				log.debug("Try the product " + tProdAltPrimNum + " does not exit, wait 1 min.");
				try {
					TimeUnit.MINUTES.sleep(1L);
				} catch (InterruptedException e) {
					log.error("The thread can not be interrupted.");
					Thread.currentThread().interrupt();
				}
				log.debug("Try the product " + tProdAltPrimNum + " " + cnt + " time(s).");
			}
			log.info("The product " + tProdAltPrimNum + " has not created yet.");
		}
		log.info("End to handle file " + ackfileName);
	}

	private int getMaxRetryCnt(boolean isPostProc) {
		return isPostProc ? 1 : maxRetryCount;
	}

	private boolean inValidAckAndPdf(File pdfFile, Matcher ackFileMatcher) {
		return !(ackFileMatcher.matches() && pdfFile.exists());
	}

	private void mergePdfDocument(PdfDocumentParams pdfDocumentParams){
		String prodSubtpCde = pdfDocumentParams.getProdSubtpCde();
		if (StringUtils.isBlank(prodSubtpCde)) return;
		File file = pdfDocumentParams.getFile();
		String mergeFile = file.getPath().replace(".pdf", "_merge.pdf");
		String newFileName = file.getPath();
		String importantFile = null;
		String importantFileApendix = null;
		String importantFileEnd = null;
		String issueInfoDoc = getIssueInfoDoc(pdfDocumentParams);
		String dcdcEliProdSubTpCde = cmbDcdcEliProdSubtpCde;
		String pprEliProdSubTpCde = cmbPprEliProdSubtpCde;
		String pprPPNProdSubTpCde = cmbPprPpnProdSubtpCde;
		String fcnPPNProdSubTpCde = cmbFcnPpnProdSubtpCde;
		String upoutPPNProdSubTpCde = cmbUpoutPpnProdSubtpCde;
		String stpdnPPNProdSubTpCde = cmbStpdnPpnProdSubtpCde;
		String twsfPPNProdSubTpCde = cmbTwsfPpnProdSubtpCde;
		log.info("mergePdfDocument__dcdcEliProdSubTpCde=="+dcdcEliProdSubTpCde);
		log.info("mergePdfDocument__pdfName=="+pdfDocumentParams.getPdfName());
		String prodTypeCde="DCDC ELI";
		if(pdfDocumentParams.getPdfName().contains("CMB")){
			if(dcdcEliProdSubTpCde.contains(prodSubtpCde)){
				importantFile = pdfImportantFileForDcdcEli;
			}else if(pprEliProdSubTpCde.contains(prodSubtpCde)){
				importantFile =  pdfImportantFileForPprEli;
			}else {
				prodTypeCde="PPN";
				if(pprPPNProdSubTpCde.contains(prodSubtpCde)){
					importantFile =  pdfImportantFileForPprPpn1;
					importantFileEnd =  pdfImportantFileForPprPpn2;
				}else if(fcnPPNProdSubTpCde.contains(prodSubtpCde)){
					importantFile =  pdfImportantFileForPpnFcn1;
					importantFileEnd =  pdfImportantFileForPpnFcn2;
				}else if(upoutPPNProdSubTpCde.contains(prodSubtpCde)){
					importantFile =  pdfImportantFileForPpnUpout1;
					importantFileEnd =  pdfImportantFileForPpnUpout2;
				}else if(stpdnPPNProdSubTpCde.contains(prodSubtpCde)){
					importantFile =  pdfImportantFileForPpnStpdwn1;
					importantFileEnd =  pdfImportantFileForPpnStpdwn2;
				}else if(twsfPPNProdSubTpCde.contains(prodSubtpCde)){
					importantFile =  pdfImportantFileForPpnTwsf1;
					importantFileEnd =  pdfImportantFileForPpnTwsf2;
				}
			}
		}else{
			importantFile = getImportantFileForNonCmb(prodSubtpCde, pdfDocumentParams.isFlag(), pdfDocumentParams.getEliPayoffTypeCde());
			importantFileApendix = getImportantFileForApendix(pdfDocumentParams.getDistbrFeePct());

		}
        try {
            mergeFile(new MergeFileParams(file, pdfDocumentParams.getProdOrig(), importantFileApendix, issueInfoDoc, mergeFile, newFileName, importantFile, prodTypeCde,
                    importantFileEnd, pdfDocumentParams.getTProdAltPrimNum(), pdfDocumentParams.getEliIssuer()));
        } catch (IOException e) {
			ErrorLogger.logErrorMsg(OTPSERR_EBJ111, "ELI Financial Document Job", "Error occurred while merging files, ErrorCode: " + e.getMessage());
        }
    }

	private String getIssueInfoDoc(PdfDocumentParams pdfDocumentParams) {
		String issueInfoDoc = null;
		if(pdfDocumentParams.isIsrInfoDoc() && "Mass".equals(pdfDocumentParams.getCustTier())){
			issueInfoDoc = getIssueInfoDoc(pdfDocumentParams.getEliIssuer());
		}
		return issueInfoDoc;
	}

	private void mergeFile(MergeFileParams params) throws IOException {
		File file = params.getFile();
		String prodOrig = params.getProdOrig();
		String importantFileApendix = params.getImportantFileApendix();
		String issueInfoDoc = params.getIssueInfoDoc();
		String mergeFile = params.getMergeFile();
		String newFileName = params.getNewFileName();
		String importantFile = params.getImportantFile();
		String prodTypeCde = params.getProdTypeCde();
		String importantFileEnd = params.getImportantFileEnd();
		String tProdAltPrimNum = params.getTProdAltPrimNum();
		String eliIssuer = params.getEliIssuer();
		if(StringUtils.isNotBlank(issueInfoDoc)){
			mergePdf(file, mergeFile, newFileName, issueInfoDoc);
		}
		if(StringUtils.isNotBlank(importantFileApendix) && prodOrig.equals("CT")){
			mergePdf(file, mergeFile, newFileName, importantFileApendix);
		}
		mergePdf(file, mergeFile, newFileName, importantFile);
		if("PPN".equals(prodTypeCde)){
			mergePdfForPPN(file, mergeFile, newFileName, importantFileEnd);
		}

		if(!StringUtils.equals(eliIssuer,"HBAP") && !StringUtils.equals(eliIssuer,"HBEU")){
			addFooter(newFileName,tProdAltPrimNum,file.getAbsolutePath());
		}
		String chmod = chmodScript + " " + newFileName;
		String chown = chownScript + " " + newFileName;
		BatchCommonUtil.executeOperationSystemCommand(chmod);
		BatchCommonUtil.executeOperationSystemCommand(chown);
	}

	private static void renameFile(File file, String mergeFile, String newFileName) throws IOException {
		Files.delete(Paths.get(file.getPath()));
		File mergedFile = new File(mergeFile);
		File newFile = new File(newFileName);
		boolean mergeFlag = mergedFile.renameTo(newFile);
		if(!mergeFlag){
			log.error(renameFileError, mergedFile.getName(), newFile.getName());
		}
	}


	private void mergePdfForPPN(File file, String mergeFile, String newFileName, String importantFileEnd) throws IOException {
		String mergeFile2 = file.getPath().replace(".pdf", "_merge.pdf");
		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(file), new PdfWriter(mergeFile2));
			 PdfDocument pdfDocument2 = new PdfDocument(new PdfReader(importantFileEnd))) {
			PdfMerger merger = new PdfMerger(pdfDocument);
			merger.merge(pdfDocument2, 1, pdfDocument2.getNumberOfPages());
		} catch (IOException e) {
			log.error(mergeFileError + file.getName(), e);
		}
		renameFile(file, mergeFile, newFileName);
	}

	private void mergePdf(File file, String mergeFile, String newFileName, String importantFile) throws IOException {
		if (!new File(importantFile).exists()) {
			log.warn("Important file for merge not found: {}", importantFile);
			return; // Skip merging if the file does not exist
		}
		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(importantFile), new PdfWriter(mergeFile));
			 PdfDocument pdfDocument2 = new PdfDocument(new PdfReader(file))) {
			PdfMerger merger = new PdfMerger(pdfDocument);
			merger.merge(pdfDocument2, 1, pdfDocument2.getNumberOfPages());
		} catch (IOException e) {
			log.error(mergeFileError + file.getName(), e);
		}
		renameFile(file, mergeFile, newFileName);
	}

	private String getImportantFileForNonCmb(String prodSubtpCde, boolean flag, String eliPayoffTypeCde) {
		String importantFile;
		switch (prodSubtpCde) {
		case "PPN_PPR":
			importantFile = pdfImportantFileForPpr;
			break;
		case "PPN_STPDWN":
			importantFile = pdfImportantFileForStpdn;
			break;
		case "PPN_UPOUT":
			importantFile = pdfImportantFileForUpout;
			break;
		case "PPN_FCN":
			importantFile = pdfImportantFileForFcn;
			break;
		case "PPN_TWSF":
			importantFile = pdfImportantFileForTwsf;
			break;
		case "PPN_60":
			importantFile = pdfImportantFileForCffn;
			break;
		case "PPN_50":
			if(flag) {
				importantFile = pdfImportantFileForDcnforpp;
			}else {
				importantFile = pdfImportantFileForDcnfornpp;
			}
			break;
		case "PPN_51":
			importantFile = pdfImportantFileForPnote;
			break;
		case "PPN_52":
			importantFile = pdfImportantFileForTwac;
			break;
		case "PPN_53":
			importantFile = pdfImportantFileForBen;
			break;
		case "PPN_61":
		case "HY_01":
			importantFile = pdfImportantFileForRan;
			break;
		case "PPN_62":
			importantFile = pdfImportantFileForSteepener;
			break;
		case "UO_ELI":
			importantFile = pdfImportantFileForUpoutEli;
			break;
		case "PPN_63":
			importantFile = pdfImportantFileForInverse;
			break;
		case "PPN_64":
			importantFile = pdfImportantFileForSbn;
			break;
		case "PPN_65":
			importantFile = pdfImportantFileForLinear;
			break;
		case "FXN_01":
		case "FXN_02":
		case "FXN_03":
			importantFile = pdfImportantFileForFxn;
			break;
		case "FXN_04":
			importantFile = pdfImportantFileForFxwcn;
			break;
		case "BEN_ELI":
			importantFile = pdfImportantFileForBenEli;
			break;
		default:
			if(eliPayoffTypeCde.equals("FCELI")){
				importantFile = pdfImportantFileForFceli;
			}else {
				importantFile = pdfImportantFileForDcdc;
			}
			break;
		}
		return importantFile;
	}

	private String getImportantFileForApendix(Double distbrFee){
		String importantFile = null;
		if(0<distbrFee && distbrFee<=1){
			importantFile = pdfImportantFileForApendix1;
		}else if(1<distbrFee && distbrFee<=2){
			importantFile = pdfImportantFileForApendix2;
		}else if(2<distbrFee && distbrFee<=3){
			importantFile = pdfImportantFileForApendix3;
		}else if(3<distbrFee && distbrFee<=4){
			importantFile = pdfImportantFileForApendix4;
		}else if(4<distbrFee && distbrFee<=5){
			importantFile = pdfImportantFileForApendix5;
		}		return importantFile;
	}

	private void transferToFinServer(String ctry, String group, File file) {
		StringBuilder commandBuilder = new StringBuilder();
		commandBuilder.append(eliFindocScript).append(" ");
		commandBuilder.append(ctry).append(" ").append(group).append(" ").append(FinDocConstants.ELI).append(" ").append(file.getName());
		BatchCommonUtil.executeOperationSystemCommand(commandBuilder.toString());
	}

	private String getIssueInfoDoc(String eliIssuer){
		String importantFile = "";
		String sanitizedEliIssuer = eliIssuer.replace("-", "").toLowerCase();
		importantFile = pdfBasePath + "impotant_issuer_" + sanitizedEliIssuer + ".pdf";
		return importantFile;
	}

	public static void addFooter(String file, String message, String outfile) throws IOException {
		try (PDDocument doc = PDDocument.load(new File(file))) {
			PDFont font = PDType1Font.TIMES_ROMAN;
			float fontSize = 10.0f;
			for (PDPage page : doc.getPages()) {
				float centerX = 450f;
				float centerY = 41;
				PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
				contentStream.beginText();
				contentStream.setFont(font, fontSize);
				contentStream.setTextMatrix(Matrix.getTranslateInstance(centerX, centerY));
				contentStream.showText(message);
				contentStream.endText();
				contentStream.close();
			}
			doc.save(outfile);
		}
	}

}
