/*
 */
package com.hhhh.group.secwealth.mktdata.starter.core.service;

public interface Service<Q, P, H> {

    P doService(final Q request, final H header) throws Exception;

}
