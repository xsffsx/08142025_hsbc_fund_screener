package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LogFileService {
    @Value("${product.log.path}")
    private String logPath;

    public Path getPath(String filename) {
        if(filename.contains("/") || filename.contains("\\")) {
            throw new IllegalArgumentException("Invalid filename: " + filename);
        }
        return CommonUtils.toCanonicalPath(Paths.get(logPath, filename));
    }

    /**
     * Map a filename to a log filename
     * Reference: product-common/src/main/java/com/dummy/wpb/product/utils/BatchLoggerUtils.java
     * and keep only the part for excel form upload
     *
     * @param fileName
     * @return
     */
    public static String mapLogFileByFileName(String fileName) {
        String log = null;
        Map<String, String> rulesMap = new HashMap<>();

        // Excel form upload
        rulesMap.put("(.*_UT_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_UT.log");
        rulesMap.put("(.*_SEC_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_SEC.log");
        rulesMap.put("(.*_BOND_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_BOND.log");
        rulesMap.put("(.*_SID_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_SID.log");
        rulesMap.put("(.*_INS_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_GENERIC.log");
        rulesMap.put("(.*_PRODALTID_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_PRODALTID.log");
        rulesMap.put("(.*_REFDATA_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_REFDATA.log");
        rulesMap.put("(.*_CUSTELIG_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_CUSTELIG.log");
        rulesMap.put("(.*_PRODAVC_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_PRODAVC.log");
        rulesMap.put("(.*_ASETGEOAL_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_ASETGEOAL.log");
        rulesMap.put("(.*_ASETSICAL_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_ASETSICAL.log");
        rulesMap.put("(.*_AVCCHAR_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_AVCCHAR.log");
        rulesMap.put("(.*_AVCCORL_)(\\d{8,})(.xlsx|.xls)$", "wpc_upload_excel_AVCCORL.log");
        rulesMap.put("(.*_MAP.WPCE)(\\d{2,})(.D)(\\d{6})(.T)(\\d{6,})(.xlsx|.xls)$", "wpc_upload_excel_FINDOCMAP.log");

        // CSV form upload
        rulesMap.put("(.*_MANLPRCBOND_)(\\d{8,})(.csv)$", "wpc_upload_csv_MANLPRCBOND.log");
        rulesMap.put("(.*_MANLPRCSEC_)(\\d{8,})(.csv)$", "wpc_upload_csv_MANLPRCSEC.log");

        for (Map.Entry<String, String> entry : rulesMap.entrySet()){
            if(fileName.matches(entry.getKey())){
                log = entry.getValue();
                break;
            }
        }
        return log;
    }
}
