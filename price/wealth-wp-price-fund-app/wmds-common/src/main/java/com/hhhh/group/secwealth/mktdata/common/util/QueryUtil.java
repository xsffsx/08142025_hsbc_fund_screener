package com.hhhh.group.secwealth.mktdata.common.util;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;

import java.util.Map;

@SuppressWarnings("java:S1192")
public final class QueryUtil {

    private QueryUtil() {
        // prevent instantiating util class
    }

    public static StringBuilder buildCmbSearchHql(final StringBuilder hql, final Map<String, String> headers, final String name){
        if(headers!=null) {
            String channel = headers.get(CommonConstants.REQUEST_HEADER_CHANNELID);
            String cmbInd = headers.get(CommonConstants.REQUEST_HEADER_GBGF);

            if ("CMB".equals(cmbInd)) {
                if (CommonConstants.CHANNEL_I.equals(channel)) {
                    hql.append(" and " + name + ".cmbProductInd = 'Y' and " + name + ".restrCmbOnlSrchInd != 'Y' ");
                } else if (CommonConstants.CHANNEL_B.equals(channel)) {
                    hql.append(" and " + name + ".cmbProductInd = 'Y' ");
                }
            }else {
                // for WPB
                hql.append(" and " + name + ".wpbProductInd = 'Y' ");
            }
        }
        return hql;
    }

    public static StringBuilder buildCmbSearchHql2(final StringBuilder hql, final Map<String, String> headers){
        if(headers!=null) {
            String channel = headers.get(CommonConstants.REQUEST_HEADER_CHANNELID);
            String cmbInd = headers.get(CommonConstants.REQUEST_HEADER_GBGF);

            if ("CMB".equals(cmbInd)) {
                if (CommonConstants.CHANNEL_I.equals(channel)) {
                    hql.append(" and cmbProductInd = 'Y' and restrCmbOnlSrchInd != 'Y' ");
                } else if (CommonConstants.CHANNEL_B.equals(channel)) {
                    hql.append(" and cmbProductInd = 'Y' ");
                }
            }else {
                // for WPB
                hql.append(" and wpbProductInd = 'Y' ");
            }
        }
        return hql;
    }

    public static StringBuilder buildCmbSearchSql(final StringBuilder sql, final Map<String, String> headers, final String name){
        if(headers!=null){
            String channel = headers.get(CommonConstants.REQUEST_HEADER_CHANNELID);
            String cmbInd = headers.get(CommonConstants.REQUEST_HEADER_GBGF);

            if(StringUtil.isValid(channel) && StringUtil.isValid(cmbInd)){
                if("CMB".equals(cmbInd)){
                    if(CommonConstants.CHANNEL_I.equals(channel)){
                        sql.append(" and " +name+ ".CMB_PROD_IND = 'Y' and " +name+ ".RESTR_CMB_ONLN_SRCH_IND != 'Y' ");
                    }else if(CommonConstants.CHANNEL_B.equals(channel)){
                        sql.append(" and " +name+ ".CMB_PROD_IND = 'Y' ");
                    }
                }else{
                    // for WPB
                    sql.append(" and " +name+ ".WPB_PROD_IND = 'Y' ");
                }
            }
        }
        return sql;
    }

    public static StringBuilder buildCmbSearchSql2(final StringBuilder sql, final Map<String, String> headers){
        if(headers!=null) {
            String channel = headers.get(CommonConstants.REQUEST_HEADER_CHANNELID);
            String cmbInd = headers.get(CommonConstants.REQUEST_HEADER_GBGF);

            if (StringUtil.isValid(channel) && StringUtil.isValid(cmbInd)) {
                if ("CMB".equals(cmbInd)) {
                    if (CommonConstants.CHANNEL_I.equals(channel)) {
                        sql.append(" and CMB_PROD_IND = 'Y' and RESTR_CMB_ONLN_SRCH_IND != 'Y' ");
                    } else if (CommonConstants.CHANNEL_B.equals(channel)) {
                        sql.append(" and CMB_PROD_IND = 'Y' ");
                    }
                }else {
                    // for WPB
                    sql.append(" and WPB_PROD_IND = 'Y' ");
                }
            }
        }
        return sql;
    }
}
