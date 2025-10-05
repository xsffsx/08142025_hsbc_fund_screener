package com.dummy.wpb.wpc.utils;

import com.dummy.wpb.wpc.utils.constant.Field;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.sql.ROWID;
import oracle.sql.TIMESTAMP;
import oracle.xdb.XMLType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.time.*;

import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.swing.text.DateFormatter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DbUtils {
    private static List<String> extDateFields = Arrays.asList("accrPrdEndDt", "accrPrdStartDt", "barrierValnDt",
            "coupnPayDt", "earlyRdmptPayDt", "earlyValnDt", "firstValuationDt");
    public static void removeFields(Map<String, Object> map, String idField) {
        map.remove(idField);
        map.remove("CTRY_REC_CDE");
        map.remove("GRP_MEMBR_REC_CDE");
    }
    public static DbUtils getInstance(){
        return new DbUtils();
    }

    /**
     * Handle data type conversion, like BigDecimal to Long / Double, Timestamp to UTC
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public static Map<String, Object> getStringObjectMap(ResultSet rs) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount= metadata.getColumnCount();
        Map<String, Object> row = new LinkedHashMap<>(columnCount);
        for(int i=1; i<=columnCount; i++){
            String columnLabel =  metadata.getColumnLabel(i);
            int columnType = metadata.getColumnType(i);
            Object value = rs.getObject(columnLabel);
            int scale = metadata.getScale(i);
            value = handleValueByType(value, columnType, scale);
            row.put(columnLabel, value);
        }
        return row;
    }

    private static Object handleValueByType(Object value, int columnType, int scale) throws SQLException {
        // Handling char type values with spaces in Oracle DB
        if (columnType == java.sql.Types.CHAR && value instanceof String) {
            return ((String) value).trim();
        }
        if (value instanceof TIMESTAMP) {
            return ((TIMESTAMP) value).timestampValue();
        }
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime().atOffset(ZoneOffset.UTC).toLocalDateTime();
        }
        if  (value instanceof BigDecimal) {
             if(0 == scale) {
                 return ((BigDecimal)value).longValue();
             } else {
                 return ((BigDecimal)value).doubleValue();
             }
        }
        if (value instanceof ROWID) {
            return ((ROWID) value).stringValue();
        }
        if (value instanceof XMLType) {
            return ((XMLType) value).getStringVal();
        }
        return value;
    }

    public static Map<String, Object> extFieldToMap(List<Map<String, Object>> list, List<String> listFields) {
        Map<String, List<Object>> listMap = new LinkedHashMap<>();
        list.forEach(m -> {
            String key = (String)m.get(Field.FIELD_CDE);
            String dataType = (String) m.get(Field.FIELD_DATA_TYPE_TEXT);
            Object value = null;
            if("String".equalsIgnoreCase(dataType)) {
                value = m.get(Field.FIELD_STRNG_VALUE_TEXT);
            } else if("Char".equalsIgnoreCase(dataType)){
                value = m.get(Field.FIELD_CHAR_VALUE_TEXT);
            } else if("Integer".equalsIgnoreCase(dataType)){
                value = m.get(Field.FIELD_INTG_VALUE_NUM);
            } else if("Decimal".equalsIgnoreCase(dataType)){
                value = m.get(Field.FIELD_DCML_VALUE_NUM);
            } else if("Date".equalsIgnoreCase(dataType)){
                value = m.get(Field.FIELD_DT_VALUE_DT);
            } else if("Timestamp".equalsIgnoreCase(dataType)){
                value = m.get(Field.FIELD_TS_VALUE_DT_TM);
            }

            // rectify value type, some DATE field like coupnPayDt is stored as String
            if(extDateFields.contains(key) && value instanceof String){
                value = toDate((String) value);
            }

            List<Object> valueList = listMap.getOrDefault(key, new LinkedList<>());
            listMap.putIfAbsent(key, valueList);
            valueList.add(value);
        });
        Map<String, Object> result = new LinkedHashMap<>();
        // handle array value / non-array value
        listMap.forEach((k, v) -> {
            if(!listFields.contains(k)){
                result.put(k, v.get(0));
            } else {
                result.put(k, v);
            }
        });
        return result;
    }

    private static LocalDate toDate(String str) {
        String format = null;
        if (str.matches("\\d{2} [A-Z][a-z]{2} \\d{4}")) {   // eg. "08 Jan 2021"
            format = "dd MMM yyyy";
        } else if (str.matches("\\d{4}-\\d{2}-\\d{2}")) {  // eg. "2020-08-10"
            format = "yyyy-MM-dd";
        } else if (str.matches("\\d{2} {2}\\d{2} \\d{4}")) {  // eg. "01  01 2020"
            format = "dd  MM yyyy";
        } else if (str.matches("\\d{2} [A-Z][a-z]{2} \\d{2}")) {  // eg. "31 Jul 20"
            format = "dd MMM yy";
        } else {
            log.warn("Not support date pattern: {}", str);
            return null;
        }

        DateTime date = DateTimeFormat.forPattern(format).withZone(DateTimeZone.UTC).parseDateTime(str);
        return Instant.ofEpochMilli(date.getMillis())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date toUTCDate(Date date) {
        ZonedDateTime zdt = date.toInstant().atZone(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String dateStr = zdt.format(formatter);
        try {
            return format.parse(dateStr);
        } catch(ParseException e){
            throw new IllegalArgumentException(e);
        }
    }
}
