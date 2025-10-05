package com.hhhh.group.secwealth.mktdata.common.validator.validators;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;


/**
 * The Class XssValidator.
 */
public class XssValidator {

    private final String XSS_VALIDATOR = "system/common/validator/xssValidator.dict";
    /** The dictionary. */
    private ArrayList<String> dictionary = new ArrayList<String>();

    private static XssValidator instance = null;

    private XssValidator() {
        InputStream inputStream = null;
        DataInputStream dataInputStream = null;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(this.XSS_VALIDATOR);
            dataInputStream = new DataInputStream(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(dataInputStream));
            String strLine;

            // Read each vulnerability
            while ((strLine = br.readLine()) != null) {
                // Add only non-empty lines
                if (strLine.trim().length() > 0) {
                    // Add the line to dictionary only if the rule is not in
                    // dictionary
                    if (this.dictionary.contains(strLine) == false) {
                        this.dictionary.add(strLine);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.error(XssValidator.class, "XssValidator error: dictionary is " + this.dictionary == null ? null
                : this.dictionary.toArray().toString(), e);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    LogUtil.error(XssValidator.class, "close DataInputStream error", e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LogUtil.error(XssValidator.class, "close InputStream error", e);
                }
            }
        }
    }

    public static synchronized XssValidator getInstance() {
        if (XssValidator.instance == null) {
            XssValidator.instance = new XssValidator();
        }
        return XssValidator.instance;
    }

    /**
     * Checks if is safe.
     * 
     * @param string
     *            the string
     * 
     * @return true, if is safe
     */
    public boolean isSafe(final String string) {
        if (string == null || string.length() <= 0) {
            return true;
        }

        for (int idx = 0; idx < this.dictionary.size(); idx++) {
            String rule = (this.dictionary.get(idx).trim().toLowerCase());
            if (string.trim().toLowerCase().contains(rule)) {
                return false;
            }
        }
        return true;
    }

}
