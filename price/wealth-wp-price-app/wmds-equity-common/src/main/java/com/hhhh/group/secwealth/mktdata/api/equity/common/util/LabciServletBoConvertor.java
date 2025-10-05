/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.ListUtils;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Fid;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.LabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Ric;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;

@Component
public class LabciServletBoConvertor {

    private final static String SYMBOL = "SYMBOL";

    private final static String PARENT = "PARENT";

    private final static String REC_STATUS = "REC_STATUS";

    private final static String REALTIME_SERVICE = "IDN_RDF";

    private final static String DELAY_SERVICE = "dIDN_RDF";

    private static final String DOT = ".";
    
    private static final String KEY_D = "d";

    public static String genSymbol(final String ric, final String service) {
        String symbol = null;
        if (LabciServletBoConvertor.REALTIME_SERVICE.equals(service)) {
            symbol = ric;
        } else if (LabciServletBoConvertor.DELAY_SERVICE.equals(service)) {
            StringBuffer sb = new StringBuffer();
            sb.append("dIDN_RDF.ANY.");
            boolean noWithExchange = (ric.indexOf(".", 1) == -1);
            boolean withEquals = ric.contains("=");
            boolean startWithDot = ric.startsWith(".");
            String s = ric;
            if (startWithDot) {
                s = ric.replaceFirst(".", "^");
            }
            if (noWithExchange) {
                s = s + ".NaE";
            }
            if (withEquals) {
                s = s + ".NaE";
            }
            symbol = sb.append(s).toString();
        }
        return symbol;
    }

    public static String genSymbols(final List<String> ricArray, final String service) {
        String symbol = null;
        if (null != ricArray) {
            List<String> symbolList = new ArrayList<String>();
            for (String ric : ricArray) {
                symbolList.add(genSymbol(ric, service));
            }
            if (!ListUtils.isEmpty(symbolList)) {
                symbol = StringUtils.join(symbolList.toArray(), SymbolConstant.SYMBOL_SEMISOLON);
            }
        }
        return symbol;
    }

    public static String genSymbolsForQuote(final List<String> ricArray, final String service) {
        String symbol = null;
        if (null != ricArray) {
            List<String> symbolList = new ArrayList<String>();
            for (String ric : ricArray) {
                symbolList.add(genSymbol(ric, service) + LabciServletBoConvertor.KEY_D);
            }
            if (!ListUtils.isEmpty(symbolList)) {
                symbol = StringUtils.join(symbolList.toArray(), SymbolConstant.SYMBOL_SEMISOLON);
            }
        }
        return symbol;
    }

    public static Map<String, LabciResponse> getResponseMap(final List<Ric> items) {
        Map<String, LabciResponse> response = new LinkedHashMap<String, LabciResponse>();
        if (!ListUtils.isEmpty(items)) {
            for (Ric i : items) {
                List<Fid> fields = i.getFid();
                if (!ListUtils.isEmpty(fields)) {
                    LabciResponse item = new LabciResponse();
                    String symbol = null;
                    for (Fid f : fields) {
                        String name = f.getId();
                        String value = f.getValue();
                        if (LabciServletBoConvertor.SYMBOL.equals(name)) {
                            item.setSymbol(value);
                            symbol = value;
                        } else if (LabciServletBoConvertor.PARENT.equals(name)) {
                            if (null != value) {
                                item.setParent(value);
                            }
                        } else if (LabciServletBoConvertor.REC_STATUS.equals(name)) {
                            if (null != value) {
                                item.setStatus(Integer.valueOf(value));
                            }
                        } else {
                            item.getFields().put(name, value);
                        }
                    }
                    if (null != symbol) {
                        if (item.getStatus() == 0) {
                            response.put(symbol, item);
                        }
                    }
                }
            }
        }
        return response;
    }

    public static Map<String, LabciResponse> IndexGetResponseMap(final List<Ric> items) {
        Map<String, LabciResponse> response = new LinkedHashMap<String, LabciResponse>();
        if (!ListUtils.isEmpty(items)) {
            for (Ric i : items) {
                List<Fid> fields = i.getFid();
                if (!ListUtils.isEmpty(fields)) {
                    LabciResponse item = new LabciResponse();
                    String symbol = null;
                    for (Fid f : fields) {
                        String name = f.getId();
                        String value = f.getValue();
                        if (LabciServletBoConvertor.SYMBOL.equals(name)) {
                            item.setSymbol(value);
                            symbol = value;
                        } else if (LabciServletBoConvertor.PARENT.equals(name)) {
                            if (null != value) {
                                item.setParent(value);
                            }
                        } else if (LabciServletBoConvertor.REC_STATUS.equals(name)) {
                            if (null != value) {
                                item.setStatus(Integer.valueOf(value));
                            }
                        } else {
                            item.getFields().put(name, value);
                        }
                    }
                    if (null != symbol) {
                        if (item.getStatus() == 0) {
                            response.put(symbol.replace(SymbolConstant.SLASH, "").replace(LabciServletBoConvertor.DOT, ""), item);
                        }
                    }
                }
            }
        }
        return response;
    }
}
