/**
 * 
 */
package com.dummy.wpb.product.component;


import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.error.ErrorLogger;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.script.ChmodScript;
import com.dummy.wpb.product.utils.FinDocUtils;
import com.dummy.wpb.product.utils.Namefilter;
import com.dummy.wpb.product.validation.MappingValidation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.ImportFinDocMappingJobApplication.JOB_NAME;
import static com.dummy.wpb.product.error.ErrorCode.OTPSERR_EBJ108;
import static com.dummy.wpb.product.error.ErrorCode.OTPSERR_ZBJ110;

/**
 * @author 44135148
 *
 */
@Component
@Slf4j
public class FinDocMappingUploadJoblistener {

    @Value("${finDoc.ftp}")
    private String finDocPath;

    @Value("${finDoc.doc.src}")
    private String finDocSrcPath;

    @Value("${finDoc.doc.chk}")
    private String finDocChkPath;

    @Value("${finDoc.map.inFile.prefix}")
    private String finDocMapInFilePrefix;

    @Value("${CHMOD.Script}")
    private String chmodScriptPath;
    
    private String extFileName;
    
    private List<String> mappingFilePaths;
    
    private List<String> mappingFileNames;
    
    @Autowired
    private MappingValidation fdv;

	public FinDocMappingUploadJoblistener() {
		extFileName = "";
		mappingFilePaths = new ArrayList<>();
		mappingFileNames = new ArrayList<>();
	}


	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		log.info("Begin Financial Document Mapping Upload job++++++++++++++++++++");
		
	    fdv.setVersion(FinDocConstants.VER_1_1);
	    fdv.setRejectRec(new ArrayList<>());
	    fdv.setSkipRec(new ArrayList<>());
		
		dispatchProcessor(jobExecution, finDocPath);
	}

	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		int addCnt=jobExecution.getExecutionContext().get("addCnt")!=null ? jobExecution.getExecutionContext().getInt("addCnt") : 0;
		int delCnt=jobExecution.getExecutionContext().get("delCnt")!=null ? jobExecution.getExecutionContext().getInt("delCnt") : 0;
		int chgCnt=jobExecution.getExecutionContext().get("chgCnt")!=null ? jobExecution.getExecutionContext().getInt("chgCnt") : 0;
		
		log.info("Number of records added   : " + addCnt);
		log.info("Number of records deleted : " + delCnt);
		log.info("Number of records changed : " + chgCnt);
		log.info("Number of records rejected: " + fdv.getRejectRec().size());
		log.info("Number of records skiped: " + fdv.getSkipRec().size());

		if (CollectionUtils.isNotEmpty(fdv.getRejectRec())) {
			String mappingFileName = mappingFileNames.stream().collect(Collectors.joining(","));
			log.info("Totally " + fdv.getRejectRec().size() + " Records are rejected in Fin Doc Mapping file - "
					+ mappingFileName);
			ErrorLogger.logErrorMsg(
					OTPSERR_ZBJ110,
					JOB_NAME,
					"Totally " + fdv.getRejectRec().size() + " Records are rejected in Fin Doc Mapping file - " + mappingFileName);
		}

		// move File
		for(String mappingFilePath: mappingFilePaths) {
			moveForm(new File(mappingFilePath));
		}
		
		log.info("End Financial Document Mapping Upload job++++++++++++++++++++");
	}
	
	private void dispatchProcessor(JobExecution jobExecution, String path) {
		
		String ctryCde = jobExecution.getJobParameters().getString(Field.ctryRecCde);
        String orgnCde = jobExecution.getJobParameters().getString(Field.grpMembrRecCde);
		
		log.info(" < ------------- beforeJob Listener of Fin Doc Mapping File of " + ctryCde + orgnCde + " begin ---------------->");
		
        long start = System.currentTimeMillis();
        
        String [] mapPrefix = StringUtils.split(finDocMapInFilePrefix, ",");

		String[] siteMapPrefix = new String[mapPrefix.length];
		siteMapPrefix[0] = ctryCde + orgnCde + "_" + mapPrefix[0];
		siteMapPrefix[1] = mapPrefix[1];

		FilenameFilter fnf = new Namefilter(siteMapPrefix,
            true,
            true);

        //Read the file folder - "path" 
        File srcFile = new File(path);
        File processFile = null;

        File[] lsFile = srcFile.listFiles(fnf);

        for (int i = 0; i < lsFile.length; i++) {
            File curFile = lsFile[i];

            if (curFile.exists()) {

                try {
                    processFile = FinDocUtils.chkAndCreate(curFile);
                    //get extension name of the file 
                	extFileName=getExtFileName(processFile);
                    if (processFile.exists() && checkAck(processFile)) {
                        matchFmt(processFile, siteMapPrefix);
                    }
                } catch (IOException e) {
					throw new productBatchException("IOException occurred while processing the file: " + curFile.getName(), e);
                }
            }
        }
        jobExecution.getExecutionContext().put("mappingFilePaths", mappingFilePaths);
        jobExecution.getExecutionContext().put("mappingFileNames", mappingFileNames);
        long end = System.currentTimeMillis();
        log.info("beforeJob Listener Processing Time: " + (end - start) + "ms");
        log.info(" < ------------- beforeJob Listener of Fin Doc Mapping File of " + ctryCde + orgnCde + " end ---------------->");
    }
	
	//get extension name of the Excel file
	private String getExtFileName(File file){
		String fileName=file.getName().toLowerCase();
		if(fileName.endsWith(FinDocConstants.SUFFIX_XLS)){
			return FinDocConstants.SUFFIX_XLS;
		}else if(fileName.endsWith(FinDocConstants.SUFFIX_XLSX)){
			return FinDocConstants.SUFFIX_XLSX;
		}
		return null;
	}
	
	private void matchFmt(File file, String[] mapPrefix) {
		Boolean fileNameStartsWith1 = file.getName().toUpperCase().startsWith(mapPrefix[0]);
		Boolean fileNameStartsWith2 = file.getName().toUpperCase().startsWith(mapPrefix[1]);
		if ((fileNameStartsWith1 || fileNameStartsWith2) && StringUtils.isNotBlank(extFileName)) {
			file = movefile(file);
			mappingFilePaths.add(file.getAbsolutePath());
			mappingFileNames.add(file.getName());
		}
	}
	
	public boolean checkAck(File file) throws IOException {
	    String name = "";
	    if (StringUtils.isNotBlank(extFileName)) {
	        name = replaceName(file.getName(),
	        	extFileName,
	            FinDocConstants.SUFFIX_ACK);
	    } else {
	        return false;
	    }
	    File ackfile = new File(file.getParent(), name);
	    if (ackfile.exists()) {
			Files.delete(Paths.get(ackfile.getPath()));
	        return true;
	    }
	    return false;
	}
	
	public File movefile(File dir) {
	    boolean success = dir.renameTo(new File(finDocSrcPath,
	        dir.getName()));
	    if (!success) {
			ErrorLogger.logErrorMsg(OTPSERR_EBJ108, JOB_NAME, finDocChkPath);
	    	log.info("Finanical Input Document file cannot be moved "
	            + dir + " to " + finDocSrcPath);
	        // return the old file 
	        return new File("");
	    }
		return new File(finDocSrcPath,
	        dir.getName());
	}
	
	public void moveForm(File file) {
	    Calendar cal = new GregorianCalendar();
	    SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
	    String time = "." + df.format(cal.getTime());
	    String name = "";
	    if (StringUtils.isNotBlank(extFileName)) {
	        name = replaceName(file.getName(),
	        	extFileName,
	            time)
	            + extFileName;
	    }
	
	    File curFile = new File(file.getParent(), file.getName());
	
	    File newFile = new File(finDocChkPath, name);
	    // Move file to new directory
	    boolean success = curFile.renameTo(newFile);
	
	    if (newFile.exists()) {
	        ChmodScript.chmodScript(newFile.getPath(), "755", chmodScriptPath);
	    }
	
	    if (!success) {
			ErrorLogger.logErrorMsg(OTPSERR_EBJ108, JOB_NAME, finDocChkPath);
	    	log.info("File can not Move " + file.getPath() + " to " + finDocChkPath);
	    }
	}
	
	private String replaceName(String name,
		    String oPat,
		    String nPat) {
		    String newName = null;
		    int loc = name.toLowerCase().lastIndexOf(oPat);
		    if (loc >= 0) {
		        newName = name.substring(0, loc) + nPat;
		    } else {
		        newName = name;
		    }
		    return newName;
		}

}
