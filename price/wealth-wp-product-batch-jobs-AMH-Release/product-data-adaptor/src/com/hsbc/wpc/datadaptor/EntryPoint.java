package com.dummy.wpc.datadaptor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.batch.core.BatchStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dummy.esf.batch.JobIdentifier;
import com.dummy.esf.batch.JobResult;
import com.dummy.esf.batch.launch.JobRunner;
import com.dummy.wpc.common.tng.TNGMessage;
import com.dummy.wpc.common.tng.TNGMsgConstants;
import com.dummy.wpc.datadaptor.bean.ReportBean;
import com.dummy.wpc.datadaptor.job.AfterJob;
import com.dummy.wpc.datadaptor.mandatorycheck.MandatoryCheck;
import com.dummy.wpc.datadaptor.mandatorycheck.MandatoryCheckFactory;
import com.dummy.wpc.datadaptor.util.CommonPropertiesHelper;
import com.dummy.wpc.datadaptor.util.ConfigLoader;
import com.dummy.wpc.datadaptor.util.ConfigMapHelper;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DataAdaptorFilenameFilter;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.FilePatternHandler;
import com.dummy.wpc.datadaptor.util.FileUtils;
import com.dummy.wpc.datadaptor.util.OrderedProperties;
import com.dummy.wpc.datadaptor.util.PropertiesHelper;

public class EntryPoint {
	private static Logger log = Logger.getLogger(EntryPoint.class);

	public static void main( String[] args) {
		// load config file
		String propPath = System.getProperty(Constants.CONFIG_PATH);
		if (StringUtils.isEmpty(propPath)) {
			String msg = "Please specify the config path with property \"-DconfigPath\"!";
			System.err.println(msg);
			TNGMessage.logTNGMsgExInfo("001", "E",
					TNGMsgConstants.SERVICE_NAME, msg);
			System.exit(1);
		}

		/*
		 * handle the file path
		 */
		// propPath = FilePathHandler.handle(propPath);
		/*
		 * config the log4j
		 */
		// if (propPath.endsWith(File.separator)) {
		if (propPath.endsWith("/") || propPath.endsWith("\\")) {
			PropertyConfigurator.configure(propPath
					+ Constants.LOG4J_PROPERTIES);
		} else {
			PropertyConfigurator.configure(propPath + File.separator
					+ Constants.LOG4J_PROPERTIES);
		}
		/*
		 * check the parameters
		 */
		if (args.length < 3) {
			logError4ArgsAndExit();
		}
		/*
		 * check the support_multi_files argument
		 */
		if (args.length > 3) {
			if (StringUtils.isNotEmpty(args[3])) {
				if (!Constants.Y.equalsIgnoreCase(args[3])
						&& !Constants.N.equalsIgnoreCase(args[3])) {
					logError4ArgsAndExit();
				}
			}
		}
		/*
		 * Check the check_ack argument
		 */
		if (args.length > 4) {
			if (StringUtils.isNotEmpty(args[4])) {
				if (!Constants.Y.equalsIgnoreCase(args[4])
						&& !Constants.N.equalsIgnoreCase(args[4])) {
					logError4ArgsAndExit();
				}
			}
		}
		/*
		 * Check the wait_time argument
		 */
		Integer waitTimeArg = 0;
		if (args.length > 5) {
			waitTimeArg = parseIntegerAndLogErr(args[5]);
		}
		/*
		 * Check the wait_repeats
		 */
		Integer waitRepeatsArg = 1;
		if (args.length > 6) {
			waitRepeatsArg = parseIntegerAndLogErr(args[6]);
		}
		
		if (args.length > 7) {
			if (StringUtils.isNotEmpty(args[7])) {
				if (!Constants.Y.equalsIgnoreCase(args[7])
						&& !Constants.N.equalsIgnoreCase(args[7])) {
					logError4ArgsAndExit();
				}
			}
		}
		/*
		 * get country code,organization code and job code from command line
		 */
		String ctryCode = args[0];
		String orgnCode = args[1];
		String jobCde = args[2];

		EntryPoint.log.info("ctry code  : " + ctryCode + ", orgn code : "
				+ orgnCode + ", job code : " + jobCde);

		/*
		 * init the job code
		 */
		if (jobCde == null) {
			String msg = "Please check the job code!";
			EntryPoint.log.error(msg);
			TNGMessage.logTNGMsgExInfo("003", "E",
					TNGMsgConstants.SERVICE_NAME, msg);
			System.exit(3);
		}

		/*
		 * a ordered properties to store the value of ctryCode_orgnCode
		 * properties file
		 */
		OrderedProperties prop = null;
		/*
		 * prepare the properties file path
		 */
		String filePath = propPath + File.separator + ctryCode + "_" + orgnCode
				+ Constants.PROPERTIES_FILE_EXTENSION;
		try {
			/*
			 * load properties file
			 */
			prop = PropertiesHelper.loadProperties(filePath);
			/*
			 * handle the file path
			 */
			// prop = PropertiesHelper.handleFilePathOfProperties(prop);
			if (prop == null) {
				String msg = "Load config file failed, please check file path: \""
						+ filePath + "\"";
				EntryPoint.log.error(msg);
				TNGMessage.logTNGMsgExInfo("004", "E",
						TNGMsgConstants.SERVICE_NAME, msg);
				System.exit(4);
			}
		} catch (RuntimeException ex) {
			String msg = "Load config file failed, please check file path: \""
					+ filePath + "\"";
			EntryPoint.log.error(msg);
			TNGMessage.logTNGMsgExInfo("004", "E",
					TNGMsgConstants.SERVICE_NAME, msg);
			System.exit(4);
		}

		/**
		 * init spring beans
		 */
		ApplicationContext appContext = initApplicationContext();

		ReportBean reportBean = new ReportBean(ctryCode, orgnCode);
		/*
		 * for formating the date/time
		 */

		EntryPoint.log.info("Start to handle [" + ctryCode + "," + orgnCode
				+ "," + jobCde + "].");
		reportBean.log("Start to handle [" + ctryCode + "," + orgnCode + ","
				+ jobCde + "].");
		Map<String, String> configMap = new TreeMap<String, String>();
		// put ctryCode,orgnCode,jobCode into configMap, but they will not
		// output to log file
		configMap.put(Constants.CTRY_CODE, ctryCode);
		configMap.put(Constants.ORGN_CODE, orgnCode);
		configMap.put(Constants.JOB_CODE, jobCde);
		/*
		 * init the common config parameters
		 */
		ConfigLoader.initConfigMap(configMap, prop);
		/*
		 * init the related job code configurations
		 */
		ConfigLoader.refreshConfigMap(jobCde, configMap, prop);
		/*
		 * set the default value for support_multi_files argument
		 */
		if (args.length > 3) {
			configMap.put(Constants.SUPPORT_MULTI_FILES, args[3]);
		}
		/*
		 * set default value for support_multi_files
		 */
		if (StringUtils.isEmpty(configMap.get(Constants.SUPPORT_MULTI_FILES))) {
			configMap.put(Constants.SUPPORT_MULTI_FILES, Constants.N);
		}

		/*
		 * override the value for wait_ack
		 */
		if (args.length > 4) {
			configMap.put(Constants.CHECK_ACK, args[4]);
		}

		/*
		 * override the value for wait_time
		 */
		Integer waitTime = 0;
		if (args.length > 5) {
			configMap.put(Constants.WAIT_TIME, waitTimeArg.toString());
			waitTime = waitTimeArg;
		} else {
			try {
				waitTime = Integer.valueOf(configMap.get(Constants.WAIT_TIME));
			} catch (NumberFormatException ex) {
				waitTime = 0;
			}
		}
		if (waitTime < 0) {
			waitTime = 0;
		}
		/*
		 * override the value for wait_repeats
		 */
		Integer waitRepeats = 1;
		if (args.length > 6) {
			configMap.put(Constants.WAIT_REPEATS, waitRepeatsArg.toString());
			waitRepeats = waitRepeatsArg;
		} else {
			try {
				waitRepeats = Integer.valueOf(configMap
						.get(Constants.WAIT_REPEATS));
			} catch (NumberFormatException ex) {
				waitRepeats = 1;
			}
		}
		if (waitRepeats < 1) {
			waitRepeats = 1;
		}
		
		/**
		 * Add validate only flag
		 */
		if (args.length > 7) {
			configMap.put(Constants.VALIDATE, args[7]);
		}
		/*
		 * prepare the ctry code,orgn code, job code and current date,time
		 */
		String replaceJobCode = jobCde.replace('.', '_');

		/*
		 * init replace map
		 */
		Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put(Constants.CTRY_CODE_VAR, ctryCode);
		replaceMap.put(Constants.ORGN_CODE_VAR, orgnCode);
		replaceMap.put(Constants.JOB_CODE_VAR, replaceJobCode);

		/*
		 * begin to fill the parameters
		 */
		ConfigLoader.replaceConfigMap(configMap, replaceMap);

		/*
		 * check if the data_file_path, output_file_path, log_file_path are a
		 * directory
		 */
		if (!checkMandatory(configMap, ctryCode, orgnCode, jobCde, reportBean)) {
			String msg = "Missing mandatory configure.";
			EntryPoint.log.error(msg);
			TNGMessage.logTNGMsgExInfo("010", "E",
					TNGMsgConstants.SERVICE_NAME, msg);
			System.exit(3);
		}

		boolean isMultiReader = ConfigMapHelper.isMultiReader(configMap);
		boolean success = true;
		if (isMultiReader) {
			success = handleMutliReader(configMap, appContext, waitRepeats,
					waitTime, ctryCode, orgnCode, jobCde, reportBean);
		} else {
			success = handleSingleReader(configMap, appContext, waitRepeats,
					waitTime, ctryCode, orgnCode, jobCde, reportBean);
		}

		reportBean.log("----DONE!");

		reportBean.writeToFileAccordingToDate();

		if (success == false) {
			System.exit(4);
		}

	}

	private static Integer parseIntegerAndLogErr(final String value) {
		Integer result = 0;
		try {
			result = Integer.valueOf(value);
		} catch (NumberFormatException ex) {
			logError4ArgsAndExit();
		}
		return result;
	}

	private static boolean handleMutliReader(
			final Map<String, String> configMap,
			final ApplicationContext appContext, Integer waitRepeats,
			final Integer waitTime, final String ctryCode,
			final String orgnCode, final String jobCode,
			final ReportBean reportBean) {
		String fileNamePattern = configMap.get(Constants.FILE_NAME_PATTERN);
		SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern(fileNamePattern);

		int readerCount = Integer.parseInt(configMap
				.get(Constants.READER_COUNT));
		String[][] files = new String[readerCount][];
		Map<String, Map<String, String>> valueMapView = new HashMap<String, Map<String, String>>();
		List<String>[] missingACKDataFileListArr = new ArrayList[readerCount];
		do {
			waitRepeats--;
			for (int i = 1; i <= readerCount; i++) {
				String keyPreifx = "reader." + i + ".";
				String dataFilePath = configMap.get(keyPreifx
						+ Constants.DATA_FILE_PATH);
				// String tempFiles[] = null;

				File dirs = new File(dataFilePath);
				files[i - 1] = dirs.list(new DataAdaptorFilenameFilter(
						configMap, i, valueMapView));
				if (files[i - 1] == null) {
					files[i - 1] = new String[0];
				}
				// if check_ack is N, then ignore this action, otherwise check
				// ack file, and filter the data files which don't have related
				// ack file
				missingACKDataFileListArr[i - 1] = new ArrayList();
				files[i - 1] = checkACKAndFilter(files[i - 1],
						missingACKDataFileListArr[i - 1], configMap, keyPreifx);
			}
			boolean cannotFind = false;
			for (String[] tempFiles : files) {
				if (tempFiles == null || tempFiles.length == 0) {
					cannotFind = true;
					break;
				}
			}
			if (cannotFind && waitTime > 0 && waitRepeats > 0) {
				EntryPoint.log
						.error("Cannot find any available data file for ["
								+ ctryCode + "," + orgnCode + "," + jobCode
								+ "], start to wait " + waitTime
								+ " second(s).");
				try {
					//Thread.currentThread().sleep(waitTime * 1000);
					checkSleep(waitTime);
				} catch (InterruptedException e) {
					EntryPoint.log.warn("Thread sleep error.", e);
				}
			} else {
				break;
			}
			// sort the tempFiles

		} while (waitRepeats > 0);
		boolean cannotFind = false;
		boolean fileCountNotEqual = false;
		for (String[] tempFiles : files) {
			if (tempFiles == null || tempFiles.length == 0) {
				cannotFind = true;
				break;
			}
			if (tempFiles.length != 1) {
				fileCountNotEqual = true;
				break;
			}
		}
		// put the data files names to a string
		String missingACKDataFileNames = "";
		for (List<String> list : missingACKDataFileListArr) {
			if (list.size() > 0) {
				if (missingACKDataFileNames.length() > 0) {
					missingACKDataFileNames += "," + list.toString();
				} else {
					missingACKDataFileNames += list.toString();
				}
			}
		}

		// if the missingACKDataFileNames is not empty, then should send TNG msg
		// and print the logger
		if (missingACKDataFileNames.length() > 0) {
			String msg = "ACK file(s) is(are) missing for [" + ctryCode + ","
					+ orgnCode + "," + jobCode + "].";
			TNGMessage.logTNGMsgExInfo("008", "E",
					TNGMsgConstants.SERVICE_NAME, msg);
			EntryPoint.log.error(msg + " The name of data file(s) is(are):"
					+ missingACKDataFileNames);
			reportBean.log("----Failed, Result: Missing ACK file(s).");
			reportBean.increaseFailed();
			return false;
		}

		if (cannotFind) {
			String msg = "Cannot find any available data file for [" + ctryCode
					+ "," + orgnCode + "," + jobCode
					+ "], this job is skipped.";
			EntryPoint.log.error(msg);
			reportBean
					.log("----Failed, Result: Cannot find any available data file.");
			reportBean.increaseFailed();
			if ("Y".equalsIgnoreCase(configMap
					.get(Constants.SENDTNG_FOR_FILECHECKING))) {
				String exInfo = ctryCode + "," + orgnCode + "," + jobCode;
				TNGMessage.logTNGMsgExInfo("007", "E",
						TNGMsgConstants.SERVICE_NAME, exInfo);
			}
			return false;
		}
		if (fileCountNotEqual) {
			EntryPoint.log
					.error("Some data files are found for ["
							+ ctryCode
							+ ","
							+ orgnCode
							+ ","
							+ jobCode
							+ "], but the file count must equal to 1 for each file pattern,this job is skipped.");
			reportBean
					.log("----Failed, Result: The file count must equal to 1 for each file pattern.");
			reportBean.increaseFailed();
			return false;
		}

		// for (int i = 1; i <= readerCount; i++) {
		// // sort the tempFiles
		// Arrays.sort(files[i-1]);
		// }

		if (configMap.get(Constants.SUPPORT_MULTI_FILES).equals("Y")) {
			EntryPoint.log
					.error("This job of ["
							+ ctryCode
							+ ","
							+ orgnCode
							+ ","
							+ jobCode
							+ "] cannot support \"support_multi_files\" function, this job is skipped.");
			reportBean
					.log("----Failed, Result: cannot support \"support_multi_files\" function.");
			reportBean.increaseFailed();
			return false;
		} else {
			// get the lastest file, after sorting, the lastest file is the
			// last file of file array
			String[] filesNeedToHandle = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				String tempFileName = files[i][files[i].length - 1];
				// if (StringUtils.isEmpty(tempFileName)) {
				// log.error("Cannot find any available data file for [" +
				// ctryCode + "," + orgnCode + "," + jobCode +
				// "], this job is skipped.");
				// reportBean.log("----Failed, Result: Cannot find any available data file.");
				// reportBean.increaseFailed();
				// return false;
				// }
				filesNeedToHandle[i] = tempFileName;
			}

			// replace input/outputfile path to specified file
			if (!replaceFilePaths4MultiReader(configMap, filesNeedToHandle,
					reportBean, valueMapView)) {
				return false;
			}

			replaceBeforeRunJob(format, jobCode, configMap);

			boolean result = beginJob(configMap, appContext);

			reportBean.log("--------DONE! Please check log file : "
					+ configMap.get(Constants.LOG_FILE_PATH));
			return result;
		}
	}

	private static boolean handleSingleReader(
			final Map<String, String> configMap,
			final ApplicationContext appContext, Integer waitRepeats,
			final Integer waitTime, final String ctryCode,
			final String orgnCode, final String jobCode,
			final ReportBean reportBean) {

		String fileNamePattern = configMap.get(Constants.FILE_NAME_PATTERN);
		SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern(fileNamePattern);

		String dataFilePath = configMap.get(Constants.DATA_FILE_PATH);
		Map<String, Map<String, String>> valueMapView = new HashMap<String, Map<String, String>>();
		/*
		 * begin to filtering
		 */
		/*
		 * list all the file that match the rules
		 */
		String files[] = null;
		List<String> missingACKDataFileList = new ArrayList<String>();
		do {
			File dirs = new File(dataFilePath);
			files = dirs.list(new DataAdaptorFilenameFilter(configMap, 0,
					valueMapView));
			if (files == null) {
				files = new String[0];
			}
			// if check_ack is N, then ignore this action, otherwise check ack
			// file, and filter the data files which don't have related ack file
			files = checkACKAndFilter(files, missingACKDataFileList, configMap,
					"");

			waitRepeats--;
			if ((files == null || files.length == 0) && waitTime > 0
					&& waitRepeats > 0) {
				EntryPoint.log
						.error("Cannot find any available data file for ["
								+ ctryCode + "," + orgnCode + "," + jobCode
								+ "], start to wait " + waitTime
								+ " second(s).");
				try {
					//Thread.currentThread();
					checkSleep(waitTime);
				} catch (InterruptedException e) {
					EntryPoint.log.warn("Thread sleep error.", e);
				}
			} else {
				break;
			}

		} while (waitRepeats > 0);

		// if any data file without ack file, then send TNG MSG and print
		// logger.
		if (missingACKDataFileList.size() > 0) {
			String msg = "" + missingACKDataFileList.size()
					+ " ACK file(s) is(are) missing for [" + ctryCode + ","
					+ orgnCode + "," + jobCode + "].";
			TNGMessage.logTNGMsgExInfo("008", "E",
					TNGMsgConstants.SERVICE_NAME, msg);
			EntryPoint.log.error(msg + " The name of data file(s) is(are):"
					+ missingACKDataFileList);
			reportBean.log("----Failed, Result: Missing "
					+ missingACKDataFileList.size() + " ACK file(s).");
			reportBean.increaseFailed();
			return false;
		}

		if (files == null || files.length == 0) {
			String msg = "Cannot find any available data file for [" + ctryCode
					+ "," + orgnCode + "," + jobCode
					+ "], this job is skipped.";
			EntryPoint.log.error(msg);
			reportBean
					.log("----Failed, Result: Cannot find any available data file.");
			reportBean.increaseFailed();
            if ("Y".equalsIgnoreCase(configMap.get(Constants.SENDTNG_FOR_FILECHECKING))) {
                String exInfo = ctryCode + "," + orgnCode + "," + jobCode;
                TNGMessage.logTNGMsgExInfo("007", "E", TNGMsgConstants.SERVICE_NAME, exInfo);
            } else{  //if sendTNG_for_filechecking=N , then exist directly,without log error 
            	 return true;
            }
			return false;
		}

		// sort the files
		Arrays.sort(files);

		// multiple files handle logic
		if (configMap.get(Constants.SUPPORT_MULTI_FILES).equals("Y")) {
			reportBean
					.log("----" + files.length + " file(s) will be processed");
			boolean result = true;
			for (int i = 0; i < files.length; i++) {
				String file = files[i];
				Map<String, String> tempConfigMap = new TreeMap<String, String>(
						configMap);
				// replace input/output file path to specified file
				if (!replaceFilePaths4SingleReader(tempConfigMap, file,
						reportBean, valueMapView)) {
					return false;
				}
				// replace currentDateTime
				replaceBeforeRunJob(format, jobCode, tempConfigMap);

				if (i > 0) {
					((ClassPathXmlApplicationContext) appContext).refresh();
				}

				boolean tempResult = beginJob(tempConfigMap, appContext);

				result = result && tempResult;

                // if Y, rename after convert
                if ("Y".equalsIgnoreCase(configMap.get(Constants.RENAME_AFTER_PROCEESS))) {
                    String path = tempConfigMap.get("data_file_path");
                    File realFile = new File(path);
                    realFile.renameTo(new File(path + "_bak"));
                }


                reportBean.log("--------DONE! Please check log file : " + tempConfigMap.get(Constants.LOG_FILE_PATH));
            }
			return result;
		} else {
			// get the lastest file, after sorting, the lastest file is the
			// last file of file array
			String file = files[files.length - 1];
			if (StringUtils.isEmpty(file)) {
				EntryPoint.log
						.error("Cannot find any available data file for ["
								+ ctryCode + "," + orgnCode + "," + jobCode
								+ "], this job is skipped.");
				reportBean
						.log("----Failed, Result: Cannot find any available data file.");
				reportBean.increaseFailed();
				return false;
			}
			// replace input/output file path to specified file
			if (!replaceFilePaths4SingleReader(configMap, file, reportBean,
					valueMapView)) {
				return false;
			}
			// replace currentDateTime
			replaceBeforeRunJob(format, jobCode, configMap);

			if("AMHGSOPS.CP".equals(jobCode)) {
				String pricePath = configMap.get(Constants.SPECIAL_PATH) + "/" + file;
				if(StringUtils.isNotEmpty(pricePath)) {
					String sourceFile = configMap.get(Constants.DATA_FILE_PATH);
					FileUtils.copyFile(sourceFile, pricePath);
				}
			}
			boolean result = beginJob(configMap, appContext);

			reportBean.log("--------DONE! Please check log file : "
					+ configMap.get(Constants.LOG_FILE_PATH));
			return result;
		}
	}

	private static String[] checkACKAndFilter(String[] files,
			final List<String> missingACKDataFileList,
			final Map<String, String> configMap, final String keyPrefix) {
		boolean existACK = true;
		String checkACK = configMap.get(Constants.CHECK_ACK);
		if (StringUtils.isEmpty(checkACK) || Constants.N.equals(checkACK)) {
			return files;
		}
		if (files == null) {
			files = new String[0];
		}
		if (missingACKDataFileList == null) {
			throw new NullPointerException(
					"the missingACKDataFileList cannot be null");
		}
		missingACKDataFileList.clear();

		String dataFilePath = configMap.get(keyPrefix
				+ Constants.DATA_FILE_PATH);
		if (!dataFilePath.endsWith(File.separator)) {
			dataFilePath = dataFilePath + File.separator;
		}
		List<String> filteredList = new ArrayList();
		for (String fileName : files) {
			String tempDataFilePath = dataFilePath
					+ FilenameUtils.getBaseName(fileName);
			File file1 = new File(tempDataFilePath + ".ack");
			File file2 = new File(tempDataFilePath + ".ACK");
			if (file1.exists() || file2.exists()) {
				filteredList.add(fileName);
			} else {
				missingACKDataFileList.add(fileName);
			}
		}

		String[] resultArr = new String[filteredList.size()];
		filteredList.toArray(resultArr);
		return resultArr;
	}

	private static void logError4ArgsAndExit() {
		String msg = "Please specify the parameters! The format should be: \n <ctry_code> <org_code> <job_code> [<support_multi_files>=Y/N]"
				+ " [<check_ack>=Y/N] [<wait_time>=ns] [<wait_repeats>=n] [<validate_only>=Y/N]";
		EntryPoint.log.error(msg);
		System.err.println(msg);
		TNGMessage.logTNGMsgExInfo("002", "E", TNGMsgConstants.SERVICE_NAME,
				msg);
		System.exit(2);
	}

	private static boolean replaceFilePaths4SingleReader(
			final Map<String, String> configMap, final String fileName,
			final ReportBean reportBean,
			final Map<String, Map<String, String>> valueMapView) {
		String readerKeyPrefix = "";
		replaceDataFilePath(configMap, fileName, reportBean, readerKeyPrefix);
		return replaceOutputFilePaths(configMap, fileName, reportBean,
				readerKeyPrefix, valueMapView);
	}

	private static boolean replaceFilePaths4MultiReader(
			final Map<String, String> configMap, final String[] fileNames,
			final ReportBean reportBean,
			final Map<String, Map<String, String>> valueMapView) {
		// boolean success = true;
		for (int i = 0; i < fileNames.length; i++) {
			String readerKeyPrefix = "reader." + (i + 1) + ".";
			replaceDataFilePath(configMap, fileNames[i], reportBean,
					readerKeyPrefix);

		}
		return replaceOutputFilePaths(configMap, fileNames[0], reportBean,
				"reader.1.", valueMapView);

	}

	/*
	 * handle output file path
	 */
	private static void replaceDataFilePath(
			final Map<String, String> configMap, final String fileName,
			final ReportBean reportBean, final String readerKeyPrefix) {
		// handle data_file_path
		String dataFilePath = configMap.get(readerKeyPrefix
				+ Constants.DATA_FILE_PATH);
		if (dataFilePath.endsWith(File.separator)) {
			configMap.put(readerKeyPrefix + Constants.DATA_FILE_PATH,
					dataFilePath + fileName);
		} else {
			configMap.put(readerKeyPrefix + Constants.DATA_FILE_PATH,
					dataFilePath + File.separator + fileName);
		}

		reportBean.log("--------File : "
				+ configMap.get(readerKeyPrefix + Constants.DATA_FILE_PATH));
		EntryPoint.log.debug("[" + DateHelper.getCurrentDateDefaultStr() + "] Need to handle file : " + fileName);
		
		String validate = configMap.get(Constants.VALIDATE);
		if (Constants.Y.equals(validate)) {
			validateFileChar(fileName);
		}
	}

	private static void validateFileChar(String fileName) {
		String cronPath = CommonPropertiesHelper.getValue(Constants.CRON_FILE_PATH);
		String adaptorIncomingPath = CommonPropertiesHelper.getValue(Constants.ADAPTOR_INCOMING_PATH);
		String cmd = cronPath + Constants.CRON_VALIDATE_CHAR_FILE + " " + adaptorIncomingPath + " " + fileName;
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			proc.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				EntryPoint.log.info(line);
			}
			
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String line2;
			while ((line2 = reader2.readLine()) != null) {
				EntryPoint.log.error(line2);
			}
			EntryPoint.log.info("cmd -> " + cmd);
			
		} catch (IOException e) {
			EntryPoint.log.error("Error validate GSOPS file " + fileName, e);
		} catch (InterruptedException e) {
			EntryPoint.log.error("Error InterruptedException " + fileName, e);
		}
	}
	
	private static boolean replaceOutputFilePaths(
			final Map<String, String> configMap, final String fileName,
			final ReportBean reportBean, final String readerKeyPrefix,
			final Map<String, Map<String, String>> valueMapView) {
		Map<String, String> valueMap = valueMapView.get(readerKeyPrefix
				+ fileName);
		boolean multiWriter = ConfigMapHelper.isMultiWriter(configMap);
		if (multiWriter) {
			// replace output_file_pattern
			int writerCount = Integer.valueOf(configMap
					.get(Constants.WRITER_COUNT));
			for (int i = 1; i <= writerCount; i++) {
				String writerKeyPrefix = "writer." + i + ".";
				String outputFilePattern = configMap.get(writerKeyPrefix
						+ Constants.OUTPUT_FILE_PATTERN);
				String replacedOutputFilePattern = replaceOutputFilePattern(
						valueMap, outputFilePattern);
				if (!checkOutputFilePattern(replacedOutputFilePattern,
						reportBean, writerKeyPrefix)) {
					return false;
				}
				repalceOutputPath(configMap, replacedOutputFilePattern,
						writerKeyPrefix);
			}
			// replace bad_file_pattern and log_file_pattern
			String outputFilePattern = configMap
					.get(Constants.OUTPUT_FILE_PATTERN);
			String replacedOutputFilePattern = replaceOutputFilePattern(
					valueMap, outputFilePattern);
			if (!checkOutputFilePattern(replacedOutputFilePattern, reportBean,
					"")) {
				return false;
			}
			replaceLogPath(configMap, replacedOutputFilePattern);
		} else {
			// replace output_file_pattern, bad_file_pattern and
			// log_file_pattern
			String outputFilePattern = configMap
					.get(Constants.OUTPUT_FILE_PATTERN);
			String replacedOutputFilePattern = replaceOutputFilePattern(
					valueMap, outputFilePattern);
			if (!checkOutputFilePattern(replacedOutputFilePattern, reportBean,
					"")) {
				return false;
			}
			repalceOutputPath(configMap, replacedOutputFilePattern, "");
			replaceLogPath(configMap, replacedOutputFilePattern);
			return true;
		}
		return true;
	}

	private static void repalceOutputPath(final Map<String, String> configMap,
			final String outputFileName, final String itemPrefix) {
		// String outputFilePathName =
		// outputFileName;//FilePatternHandler.list2String(outputFilePatternList);
		String oldOutputFilePath = configMap.get(itemPrefix
				+ Constants.OUTPUT_FILE_PATH);

		if (!oldOutputFilePath.endsWith(File.separator)) {
			oldOutputFilePath += File.separator;
		}
		String outputFilePath = oldOutputFilePath
				+ genTempOutputFileName(outputFileName);
		String realOutputFilePath = oldOutputFilePath + outputFileName;
		configMap.put(itemPrefix + Constants.OUTPUT_FILE_PATH, outputFilePath);
		configMap.put(itemPrefix + Constants.REAL_PREFIX
				+ Constants.OUTPUT_FILE_PATH, realOutputFilePath);
	}

	private static String genTempOutputFileName(final String outputFileName) {
		String newFileName = UUID.randomUUID().toString();
		return newFileName;
	}

	private static void replaceLogPath(final Map<String, String> configMap,
			final String outputFileName) {
		final String TIMESTAMP_REG_EXP = "([12]\\d{3})(0\\d|1[0-2])([0-2]\\d|3[01])([0-1]\\d|2[0-3])[0-5][0-9][0-5][0-9]";

		// log file path
		String oldLogFilePath = configMap.get(Constants.LOG_FILE_PATH);
		if (!oldLogFilePath.endsWith(File.separator)) {
			oldLogFilePath += File.separator;
		}

		String logFilePath = null;
		// String badFilePath = null;
		int lastIndexOf = outputFileName.lastIndexOf(".");
		if (lastIndexOf != -1) {
			String tempFileName = outputFileName.substring(0, lastIndexOf);

			// if suffix of file name is timestamp, change to date for log
			int lastIndOfFileName = tempFileName.lastIndexOf("_");
			if (lastIndOfFileName != -1) {
				String tempFileNamePrefix = tempFileName.substring(0,
						lastIndOfFileName);
				String tempFileNameSuffix = tempFileName
						.substring(lastIndOfFileName + 1);

				if (Pattern.matches(TIMESTAMP_REG_EXP, tempFileNameSuffix)) {
					// it is timestamp, change to date
					String curDate = DateHelper.formatDate2String(
							new java.sql.Date(System.currentTimeMillis()),
							"yyyyMMdd");
					tempFileName = tempFileNamePrefix + "ADP_" + curDate;

					// remove all separator
					tempFileName = StringUtils.remove(tempFileName, "_");
				}

			}

			logFilePath = oldLogFilePath + tempFileName + ".txt";
		} else {
			logFilePath = oldLogFilePath + outputFileName + ".txt";
		}


		//format batch log from "yyyyMMddHHmmss to  yyyyMMdd"
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String currentDate = dateFormat.format(System.currentTimeMillis());
		logFilePath = logFilePath.replace(Constants.CURR_DT_TM_VAR,currentDate);



		configMap.put(Constants.LOG_FILE_PATH, logFilePath);

	}

	private static boolean checkOutputFilePattern(
			final String replacedOutputFilePattern,
			final ReportBean reportBean, final String itemPrefix) {
		List<String> outputFilePatternList = FilePatternHandler
				.splitPattern(replacedOutputFilePattern);
		for (String pattern : outputFilePatternList) {
			if (pattern.startsWith(FilePatternHandler.LEFT_ANGLE_BRACKET_STR)
					&& pattern
							.endsWith(FilePatternHandler.RIGHT_ANGLE_BRACKET_STR)) {
				String msg = "The property \""
						+ itemPrefix
						+ Constants.OUTPUT_FILE_PATTERN
						+ "\" still contains the argument \""
						+ pattern
						+ "\" which cannot be replaced by specified value. This job is skipped.";
				EntryPoint.log.error(msg);
				reportBean.log("----Failed, Result: " + msg);
				reportBean.increaseFailed();
				return false;
			}
		}
		return true;
	}

	private static String replaceOutputFilePattern(
			final Map<String, String> valueMap, String outputFilePattern) {
		// String replacedOutputFilePattern = outputFilePattern;
		if (valueMap != null && valueMap.size() > 0) {
			Iterator<String> it = valueMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = valueMap.get(key);
				outputFilePattern = outputFilePattern.replaceAll(key, value);
			}
		}
		return outputFilePattern;
	}

	/*
	 * data_file_path, output_file_path, log_file_path, bad_file_path must be a
	 * directory
	 */
	private static boolean checkMandatory(final Map<String, String> configMap,
			final String ctryCode, final String orgnCode, final String jobCode,
			final ReportBean reportBean) {
		MandatoryCheck mandatoryCheck = MandatoryCheckFactory.instance(
				configMap, ctryCode, orgnCode, jobCode, reportBean);
		boolean checkSuccess = false;
		if (mandatoryCheck != null) {
			checkSuccess = mandatoryCheck.check();
		}
		return checkSuccess;

	}

	private static void replaceBeforeRunJob(final SimpleDateFormat format,
			final String jobCode, final Map<String, String> configMap) {
		// replace current Date Time:
		// wait 1 second so that can generate different log file for different
		// data file.
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			EntryPoint.log.warn("Thread sleep error.", e);
		}
		String currentDateTime = format.format(new Date());
		replaceCurrentDateTimevar(configMap, currentDateTime);
	}

	private static void replaceCurrentDateTimevar(
			final Map<String, String> configMap, final String currentDateTime) {
		// String[] params = new String[] {currentDateTime };
		Map<String, String> replaceMap = new HashMap<String, String>();
		replaceMap.put(Constants.CURR_DT_TM_VAR, currentDateTime);
		ConfigLoader.replaceConfigMap(configMap, replaceMap);
	}

	/**
	 * init the application context
	 * 
	 * @return
	 */
	private static ApplicationContext initApplicationContext() {

		String[] jobFileArr = new String[] { "jobs/fileadaptor-job-registry.xml" };

		ApplicationContext context = new ClassPathXmlApplicationContext(
				jobFileArr);
		return context;
	}

	private static OrderedProperties refreshProp(
			final Map<String, String> configMap) {
		OrderedProperties prop = new OrderedProperties();
		Set<String> keys = configMap.keySet();
		Iterator<String> ite = keys.iterator();
		while (ite.hasNext()) {
			String key = ite.next();
			prop.setProperty(key, configMap.get(key));
		}
		return prop;
	}

	/**
	 * begin to run the specified job
	 * 
	 * @param context
	 * @param configMap
	 */
	private static boolean beginJob(final Map<String, String> configMap,
			final ApplicationContext context) {

		String constantsFilePath = configMap.get(Constants.CONSTANTS_FILE_PATH);
		String jobCode = configMap.get(Constants.JOB_CODE);
		ConstantsPropertiesHelper.init(jobCode, constantsFilePath);

		String jobBeanId = configMap.get(Constants.JOB_BEAN_ID);

		EntryPoint.log.info("[" + DateHelper.getCurrentDateDefaultStr() + "] Will execute : " + jobBeanId);

		JobRunner runner = (JobRunner) context.getBean("jobRunner");

		JobIdentifier id = new JobIdentifier();
		id.setJobName(jobBeanId);

		id.setParameters(ConfigLoader.convertMaptoProperties(configMap));

		try {
			EntryPoint.log.info("[" + DateHelper.getCurrentDateDefaultStr() + "] Begin to execute the job");
			JobResult result = runner.run(id);
			BatchStatus status = result.getStatus();
			EntryPoint.log.info("[" + DateHelper.getCurrentDateDefaultStr() + "] Execute the job " + status);
			ConfigMapHelper.renameOutputFileName(configMap);
			handleAfterJob(configMap);
		} catch (Exception e) {
			EntryPoint.log.error("Run job failed:" + e.getMessage());
			TNGMessage.logTNGMsgExInfo("006", "E",
					TNGMsgConstants.SERVICE_NAME, "Run job failed:"
							+ e.getMessage());
			return false;
		}

		return true;
	}

	private static void handleAfterJob(final Map<String, String> configMap)
			throws Exception {
		String afterJobHandling = configMap.get(Constants.AFTER_JOB_HANDLING);
		if (StringUtils.isEmpty(afterJobHandling)) {
			return;
		}
		Object obj = null;
		try {
			Class clz = Class.forName(afterJobHandling);
			obj = clz.newInstance();

		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Cannot instance the afer_job_handling \""
							+ afterJobHandling + "\"");
		}
		if (obj == null) {
			throw new IllegalArgumentException(
					"Cannot instance the afer_job_handling \""
							+ afterJobHandling + "\"");
		}
		if (!(obj instanceof AfterJob)) {
			throw new IllegalArgumentException(
					"The afer_job_handling \""
							+ afterJobHandling
							+ "\" should be a implementation of interface com.dummy.wpc.datadaptor.job.AfterJob.");
		}
		AfterJob aj = (AfterJob) obj;
		aj.afterJob(configMap);

	}
	
	public static void checkSleep(int waitTime) throws InterruptedException{
		int total = waitTime*1000 ;
		if(total > 7200000){			
			try {				
				throw new InterruptedException();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				EntryPoint.log.warn("Thread sleep time can't exceeds 2 hours!", e);
			} 			
		} else {
			Thread.sleep(total);
		}
		
	}

	// private static void renameOutputFileName(Map<String, String> configMap) {
	// boolean multiWriter = ConfigMapHelper.isMultiWriter(configMap);
	// if (multiWriter) {
	// int writerCount = Integer.valueOf(configMap.get(Constants.WRITER_COUNT));
	// for (int i = 1; i <= writerCount; i++) {
	// String itemPrefix = "writer." + i + ".";
	// renameOutputFileName(configMap, itemPrefix);
	// }
	// }
	// else{
	// renameOutputFileName(configMap, "");
	// }
	// }

	// private static void renameOutputFileName(Map<String, String> configMap,
	// String itemPrefix) {
	// String outputFilePath = configMap.get(itemPrefix +
	// Constants.OUTPUT_FILE_PATH);
	// String realOutputFilePath = configMap.get(itemPrefix +
	// Constants.REAL_PREFIX + Constants.OUTPUT_FILE_PATH);
	// File outputFile = new File(outputFilePath);
	// File realOutputFile = new File(realOutputFilePath);
	// if(realOutputFile.exists()){
	// realOutputFile.delete();
	// }
	// outputFile.renameTo(realOutputFile);
	//		
	// configMap.put(itemPrefix +
	// Constants.OUTPUT_FILE_PATH,realOutputFilePath);
	// configMap.remove(itemPrefix + Constants.REAL_PREFIX +
	// Constants.OUTPUT_FILE_PATH);
	// }
}
