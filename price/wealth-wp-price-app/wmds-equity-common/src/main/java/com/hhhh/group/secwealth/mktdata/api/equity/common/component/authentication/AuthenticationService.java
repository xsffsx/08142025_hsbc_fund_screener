/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication;

public interface AuthenticationService {

    String encrypt(String token);

    String decrypt(String token);

}
