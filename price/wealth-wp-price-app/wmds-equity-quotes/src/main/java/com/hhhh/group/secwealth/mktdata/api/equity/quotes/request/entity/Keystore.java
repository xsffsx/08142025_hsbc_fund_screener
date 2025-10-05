/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Keystore {

    private String keystoreFilePath;

    private String keystorePasswordFilePath;

    private String initialisationVector;

}
