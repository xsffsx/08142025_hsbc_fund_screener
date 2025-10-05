/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.validator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalInitializationException;
import lombok.Setter;

public class ContainerValidator {

    private static final Logger logger = LoggerFactory.getLogger(ContainerValidator.class);

    private ArrayList<String> dictionary = new ArrayList<>();

    @Setter
    private String basePath;

    @Setter
    private String filePath;

    public void init() {
        try (InputStream inputStream = this.getClass().getResourceAsStream(this.basePath + this.filePath);
             DataInputStream dataInputStream = new DataInputStream(inputStream);
             BufferedReader br = new BufferedReader(new InputStreamReader(dataInputStream))) {
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (strLine.trim().length() > 0 && !this.dictionary.contains(strLine)) {
                    this.dictionary.add(strLine);
                }
            }
        } catch (Exception e) {
            String errMessage = "Can't init ValidateAdvisor";
            ContainerValidator.logger.error(errMessage, e);
            throw new IllegalInitializationException(ExCodeConstant.EX_CODE_ILLEGAL_INITIALIZATION);
        }
    }

    public boolean isContain(final String str, final boolean isLower) {
        if (null == str) {
            return false;
        }
        boolean isContain;
        if (isLower) {
            isContain = this.dictionary.contains(str.trim().toLowerCase());
        } else {
            isContain = this.dictionary.contains(str.trim());
        }
        return isContain;
    }

}
