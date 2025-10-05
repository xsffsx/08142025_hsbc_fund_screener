/*
 */
package com.hhhh.group.secwealth.mktdata.starter.healthcheck.service;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.starter.healthcheck.bean.Status;

public interface Healthcheck {

    List<Status> getStatus();

}
