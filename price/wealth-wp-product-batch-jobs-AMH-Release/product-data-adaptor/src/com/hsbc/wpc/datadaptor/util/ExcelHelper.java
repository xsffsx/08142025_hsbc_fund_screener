/*
 */
package com.dummy.wpc.datadaptor.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * <p>
 * <b> Insert description of the class's responsibility/role. </b>
 * </p>
 */
public class ExcelHelper {

    private static final Logger LOGGER = Logger.getLogger(ExcelHelper.class);
    // a>>#@@#<<b##@@##cde##@@##f means all title named as b,cde,f will be renamed to a
    private static final String TITLE_SEPARATOR_NEW = ">>#@@#<<";
    private static final String TITLE_SEPARATOR_ALIAS = "##@@##";

    public static boolean isCellEmpty(HSSFCell cell) {
        if ((cell == null) || (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK)
            || (cell.getCellType() == HSSFCell.CELL_TYPE_STRING && cell.getStringCellValue().length() == 0)) {
            return true;
        } else {
            return false;
        }
    }

    public static Map processTitleRow(HSSFRow row) {
        Map map = new HashMap();
        Short index = null;
        HSSFCell cell;
        String str = null;

        for (short i = 0; row != null && i <= row.getLastCellNum(); i++) {
            cell = row.getCell(i);
            if (cell != null) {
                str = StringUtils.deleteWhitespace(cell.getStringCellValue());
                str = StringUtils.lowerCase(str);

                index = new Short(i);
                if ((str != null) && (!map.containsKey(str))) {
                    map.put(str, index);
                }
            }
        }
        return map;
    }

    public static Map processTitleRow(HSSFRow row, Map specialTitleMap) {
        Map map = new HashMap();
        Short index = null;
        HSSFCell cell;
        String str = null;

        for (short i = 0; row != null && i <= row.getLastCellNum(); i++) {
            cell = row.getCell(i);
            if (cell != null) {
                str = StringUtils.deleteWhitespace(cell.getStringCellValue());
                str = StringUtils.lowerCase(str);

                index = new Short(i);
                if ((str != null) && (!map.containsKey(str))) {
                    if (specialTitleMap.get(str) != null) {
                        str = (String) specialTitleMap.get(str);
                    }
                    map.put(str, index);

                }
            }
        }
        return map;
    }

    public static Map getSpecialTitleMap(String specialTitleConfigFileName) {
        Map specialTitleMap = new HashMap();
        BufferedReader reader = null;

        try {
            if (StringUtils.isNotEmpty(specialTitleConfigFileName) && (new File(specialTitleConfigFileName)).exists()) {
                reader = new WPCBufferReader(new InputStreamReader(new FileInputStream(specialTitleConfigFileName)),1048576);	                
                String lineValue = null;
                String[] titleArr = null;
                String[] titleAlias = null;
                String tmpOldTitles = null;
                while ((lineValue = reader.readLine()) != null) {
                    titleArr = lineValue.split(TITLE_SEPARATOR_NEW);
                    if (titleArr != null) {
                        if (titleArr.length == 2) {
                            tmpOldTitles = titleArr[1];
                            titleAlias = tmpOldTitles.split(TITLE_SEPARATOR_ALIAS);
                            for (int i = 0; titleAlias != null && i < titleAlias.length; i++) {
                                specialTitleMap.put(titleAlias[i], titleArr[0]);
                            }
                        } else {
                            throw new Exception("Special title config error!ConfigPath=" + specialTitleConfigFileName
                                + "    Config:" + lineValue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("error occured when get special title map." + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                LOGGER.error("error occured when gen excel.");
            }
        }

        return specialTitleMap;
    }

    public static HSSFCell getCell(HSSFRow row, Map map, String key) {
        Short value = (Short) map.get(key);
        if (value == null) {
            return null;
        } else {
            return row.getCell(value.shortValue());
        }
    }

    public static String convDoubleToString(double in) {
        if (in < 0) {
            return null;
        } else {
            return Double.toString(in).trim();
        }
    }

    public static String processString(String in) {
        if (in == null) {
            return null;
        } else {
            return StringUtils.stripToNull(in);
        }
    }

    public static java.sql.Date processDate(java.util.Date in, SimpleDateFormat dateFormat) {
        if (in == null) {
            return null;
        } else {
            return new java.sql.Date(in.getTime());
        }
    }

    public static Date convStringToDate(String in, SimpleDateFormat dateFormat) throws Exception {
        try {
            if ((in == null) || (in.equals("00000000"))) {
                return null;
            } else {
                return new Date(dateFormat.parse(in).getTime());
            }
        } catch (Exception e) {
            throw new Exception("Cannot convert String'" + in + "' to Date");
        }
    }

    public static boolean isNumeric(String in) {
        if (StringUtils.isBlank(in)) {
            return false;
        } else {
            if (in.matches("[\\p{Digit}\\p{Punct}]+")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean hasLowerCase(String in) {

        if (in != null) {
            String str = in.trim();
            if (str.length() > 0) {
                String aux = in.toUpperCase().trim();
                if (str.compareTo(aux) == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean hasNumeric(String in) {
        if (in != null) {
            for (int i = 0; i < in.length(); i++) {
                if (Character.isDigit(in.charAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static BigDecimal convDoubleToBD(double in) {
        if (in < 0) {
            return null;
        } else {
            return new BigDecimal(String.valueOf(in));
        }
    }
    
   //change for AMH WPC deployment
    public static BigDecimal convDoubleToBDSupportNegativeValue(double in) {

          return new BigDecimal(String.valueOf(in));

    }

    public static BigDecimal convStringToBD(String in) {
        BigDecimal bd;
        if ((in == null) || (in.equals(""))) {
            return null;
        } else {
            try {
                bd = new BigDecimal(in);
            } catch (NumberFormatException nfe) {
                throw new NumberFormatException("Cannot convert String'" + in + "' to BigDecimal");
            }
        }
        return bd;
    }

    public static BigDecimal convDoubleToBD(double in, int scale, int decimal) {
        if (in < 0) {
            return null;
        } else {
            if (scale > 0) {
                String pattern = "#0.";
                for (int i = 0; i < decimal; i++) {
                    pattern = pattern.concat("0");
                }

                DecimalFormat df = new DecimalFormat(pattern);
                Double d = new Double(in * scale);

                return new BigDecimal(df.format(d));
            } else {
                throw new NumberFormatException(in + " scale cannot <0.");
            }
        }
    }
    
   //change for AMH WPC deployment
    public static BigDecimal convDoubleToBDSupportNegativeValue(double in, int scale, int decimal) {

        if (scale > 0) {
            String pattern = "#0.";
            for (int i = 0; i < decimal; i++) {
                pattern = pattern.concat("0");
            }

            DecimalFormat df = new DecimalFormat(pattern);
            Double d = new Double(in * scale);

            return new BigDecimal(df.format(d));
        } else {
            throw new NumberFormatException(in + " scale cannot <0.");
        }
    
    }

    public static String convDoubleToString(double in, int scale, int decimal) {
        if (in < 0) {
            return null;
        } else {
            if (scale > 0) {
                String pattern = "#0.";
                for (int i = 0; i < decimal; i++) {
                    pattern = pattern.concat("0");
                }

                DecimalFormat df = new DecimalFormat(pattern);
                Double d = new Double(in * scale);

                return df.format(d);
            } else {
                throw new NumberFormatException(in + " scale cannot <0.");
            }
        }
    }

    public static BigDecimal convStringToBD(String in, int scale, int decimal) {

        if ((in == null) || (in.equals("")) || ((in.length() >= 1) && (in.substring(0, 1).equals("-")))) {
            return null;
        } else {
            try {
                if (scale > 0) {
                    String str = StringUtils.replace(in, "%", "");

                    String pattern = "#0.";
                    for (int i = 0; i < decimal; i++) {
                        pattern = pattern.concat("0");
                    }

                    DecimalFormat df = new DecimalFormat(pattern);
                    double d = Double.valueOf(str).doubleValue() * scale;

                    return new BigDecimal(df.format(d));
                } else {
                    throw new NumberFormatException(in + " scale cannot <0.");
                }
            } catch (NumberFormatException nfe) {
                throw new NumberFormatException("Cannot convert String'" + in + "' to BigDecimal");
            }
        }
    }

    public static String formatNumericString(String in, int scale, int decimal) {

        if (StringUtils.isBlank(in)) {
            return null;
        } else {
            if (scale > 0) {
                try {
                    String str = StringUtils.replace(in, "%", "");
                    str = StringUtils.stripToNull(str);

                    String pattern = "#0.";
                    for (int i = 0; i < decimal; i++) {
                        pattern = pattern.concat("0");
                    }

                    DecimalFormat df = new DecimalFormat(pattern);
                    double d = Double.valueOf(str).doubleValue() * scale;

                    return df.format(d);
                } catch (NumberFormatException nfe) {
                    throw new NumberFormatException("Cannot convert String'" + in + "' to BigDecimal");
                }
            } else {
                throw new NumberFormatException(in + " scale cannot <0.");
            }
        }
    }

    public static BigDecimal convDoubleToBD2(double in, int scale, int decimal) {

        if (scale > 0) {
            String pattern = "#0.";
            for (int i = 0; i < decimal; i++) {
                pattern = pattern.concat("0");
            }

            DecimalFormat df = new DecimalFormat(pattern);
            Double d = new Double(in * scale);

            return new BigDecimal(df.format(d));

        } else {
            throw new NumberFormatException(in + " scale cannot <0.");
        }
    }

    public static BigDecimal convStringToBD2(String in, int scale, int decimal) {

        if ((in == null) || (in.equals(""))) {
            return null;
        } else {
            try {
                if (scale > 0) {
                    String str = StringUtils.replace(in, "%", "");

                    String pattern = "#0.";
                    for (int i = 0; i < decimal; i++) {
                        pattern = pattern.concat("0");
                    }

                    DecimalFormat df = new DecimalFormat(pattern);
                    double d = Double.valueOf(str).doubleValue() * scale;

                    if (((in.length() >= 1) && (in.substring(0, 1).equals("-")))) {
                        return new BigDecimal(df.format(d)).negate();
                    } else {
                        return new BigDecimal(df.format(d));
                    }
                } else {
                    throw new NumberFormatException(in + " scale cannot <0.");
                }
            } catch (NumberFormatException nfe) {
                throw new NumberFormatException("Cannot convert String'" + in + "' to BigDecimal");
            }
        }
    }

    public static BigDecimal formatDecimal(BigDecimal number, int decimal) {
        BigDecimal result = null;

        String pattern = "#0.";
        for (int i = 0; i < decimal; i++) {
            pattern = pattern.concat("0");
        }

        DecimalFormat df = new DecimalFormat(pattern);
        if (number != null) {
            double n = number.doubleValue();
            result = new BigDecimal(df.format(n));
            result = result.movePointRight(decimal);
        }
        return result;
    }

    public static Integer processInt(int in) {
        try {
            return new Integer(in);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("Cannot convert in'" + in + "' to Integer");
        }
    }

    public static String convBDToString(BigDecimal in) throws Exception {
        if (in == null) {
            return null;
        } else {
            try {
                return in.toString();
            } catch (Exception e) {
                throw new Exception("Cannot convert String'" + in + "' to BigDecimal");
            }
        }
    }

    // This logic is copied from HFI old system.
    public static boolean isExtRow(HSSFCell cell1, HSSFCell cell2) {
        if (((cell1 == null) || (cell1.getCellType() == HSSFCell.CELL_TYPE_BLANK) || (cell1.getCellType() == HSSFCell.CELL_TYPE_STRING && cell1
            .getStringCellValue().length() == 0))
            && ((cell2 != null) && ((cell2.getCellType() != HSSFCell.CELL_TYPE_BLANK) || (cell2.getCellType() == HSSFCell.CELL_TYPE_STRING && cell2
                .getStringCellValue().length() > 0)))) {
            return true;
        } else {
            return false;
        }
    }

}
