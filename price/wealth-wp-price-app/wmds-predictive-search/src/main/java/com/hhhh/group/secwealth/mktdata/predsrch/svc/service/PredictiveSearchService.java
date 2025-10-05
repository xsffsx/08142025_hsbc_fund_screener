package com.hhhh.group.secwealth.mktdata.predsrch.svc.service;


import com.hhhh.group.secwealth.mktdata.common.dto.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.common.service.Service;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;

public interface PredictiveSearchService extends Service {

    public SearchableObject getBySymbolMarket(final InternalProductKey ipk, final String locale) throws Exception;

    public SearchableObject getByRicCode(final String countryCode, final String groupMember, final String ric, final String locale)
        throws Exception;

    public SearchableObject getByWCode(final String countryCode, final String groupMember, final String wCode, final String locale)
        throws Exception;

    public SearchableObject getBySymbolCountryTradableCode(String site, String countryCode, String symbol,
        String countryTradableCode, String productType, String locale) throws Exception;

    public SearchableObject getBySymbolMarket(String countryCode, String groupMember, String prodAltNum,
        String countryTradableCode, String productType, String locale) throws Exception;
}
