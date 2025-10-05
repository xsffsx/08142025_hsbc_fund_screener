/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.service.AbstractService;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.VolumeService;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.util.VolumeServiceConfig;

/**
 * <p>
 * <b> Volume Service Dashboard. </b>
 * </p>
 */
@Service("volumeServiceDashboard")
public class VolumeServiceDashboard extends AbstractService {

    @Autowired
    @Qualifier("volumeService")
    private VolumeService volumeService;

    @Autowired
    @Qualifier("volumeServiceConfig")
    private VolumeServiceConfig volumeServiceConfig;

    public Object execute(final Object object) throws Exception {
        String site = ((JSONObject) object).getString("site");

        String volumeServiceEnabled = this.volumeServiceConfig.getValue(site, VolumeServiceConfig.VOLUME_SERVICE_ENABLED);
        boolean isVolumeServiceEnabled = volumeServiceEnabled != null ? Boolean.parseBoolean(volumeServiceEnabled) : false;

        LogUtil.info(VolumeServiceDashboard.class, "VolumeServiceDashboard, Site: {}, volumeService is {}", site,
            isVolumeServiceEnabled == true ? "Enabled" : "Disabled");
        if (!isVolumeServiceEnabled) {
            throw new CommonException("VolumeService is disabled!");
        }

        this.volumeService.isConnectBySite(site);
        return null;
    }
}
