/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.component.encryptor;

import lombok.Data;

@Data
public class EncryptedResponse {

    private String status;

    private String encryptedId;

    public EncryptedResponse(final String status) {
        this.status = status;
    }

    public EncryptedResponse(final String status, final String encryptedId) {
        this.status = status;
        this.encryptedId = encryptedId;
    }

}
