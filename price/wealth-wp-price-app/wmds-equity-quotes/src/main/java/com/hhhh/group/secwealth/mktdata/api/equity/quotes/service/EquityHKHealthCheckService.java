/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Watchlist;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.vendor.midfs.RequestPattern;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EquityHKLabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.MIDFSProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuoteHistoryRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties.EquityHKHealthcheckProperties;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.bean.Status;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.impl.ApplicationHealthcheckService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalConfigurationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@Component
@ConditionalOnProperty(value = "service.quotes.hkEquity.injectEnabled", havingValue = "true")
public class EquityHKHealthCheckService extends ApplicationHealthcheckService {

	private static final Logger logger = LoggerFactory.getLogger(EquityHKHealthCheckService.class);

	private static final String HEALTHCHECK_MIDFS = "MIDFS";

	private static final String HEALTHCHECK_LABCI_CACHING = "LABCI-CACHING";

	private static final String HEALTHCHECK_PREDICTIVE_SEARCH = "PREDICTIVE-SEARCH";

	private static final String VENDOR_API = "StockPortfolio";

	private static final String HEALTHCHECK_DB = "DB_CONNECTION";

	private static final String FILE_SUFFIX = "XML";

	private static final String URL_FORMAT = "UTF-8";

	@Autowired
	private HttpClientHelper httpClientHelper;

	@Autowired
	private EquityHKLabciProperties labciProperties;

	@Autowired
	private ApplicationProperties appProps;

	@Autowired
	private QuoteHistoryRepository quoteHistoryDao;

	@Autowired
	private MIDFSProperties midfsProperties;

	@Autowired
	private PredSrchService predSrchService;

	@Autowired
	private EquityHKHealthcheckProperties hKHealthcheckProperties;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.Healthcheck
	 * #getStatus()
	 */
	@Override
	public List<Status> getStatus() {
		List<Status> statusList = new ArrayList<>();
		statusList.add(MidfsCheck());
		// statusList.add(DbCheck());
		statusList.add(LabciCachingCheck());
		statusList.add(PredictiveSearchCheck());
		return statusList;
	}

	public Status MidfsCheck() {
		long startTime = System.currentTimeMillis();
		try {
			String commandId = this.midfsProperties.getCommandId(EquityHKHealthCheckService.VENDOR_API,
					hKHealthcheckProperties.getHeader().getLocale(), false);
			RequestPattern requestPattern = this.midfsProperties
					.getRequestPattern(EquityHKHealthCheckService.VENDOR_API);
			String dummyParameter = requestPattern.getDummyParameter();
			String requestParameter = dummyParameter.replace(Constant.REQUEST_PARAMETER_COMMAND_ID, commandId)
					.replace(Constant.REQUEST_PARAMETER_QUOTE_STOCK, hKHealthcheckProperties.getKeyWord());
			String serviceResponse = this.httpClientHelper.doGet(requestPattern.getUrl(), requestParameter, null);
			JsonNode jsonNode = JsonUtil.readTree(String.valueOf(serviceResponse));
			String status = jsonNode.findPath(this.midfsProperties.getResponseStatusKey()).asText();
			if (!this.midfsProperties.isCorrectResponseStatus(status)) {
				String errorMessage = this.midfsProperties.getResponseMessage(status);
				EquityHKHealthCheckService.logger.error(errorMessage);
				throw new Exception(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
			}
			long endTime = System.currentTimeMillis();
			return new Status(Status.SUCCESS, EquityHKHealthCheckService.HEALTHCHECK_MIDFS, (endTime - startTime));
		} catch (Exception e) {
			long endTime = System.currentTimeMillis();
			return new Status(Status.FAILURE, EquityHKHealthCheckService.HEALTHCHECK_MIDFS, (endTime - startTime));
		}
	}

	private Status DbCheck() {
		long startTime = System.currentTimeMillis();
		try {
			Integer num = this.quoteHistoryDao.dbCheck();
			logger.warn("EquityHaseStmaHealthCheckService", "dbHealthCheckMsg: " + num);
			long endTime = System.currentTimeMillis();
			return new Status(Status.SUCCESS, HEALTHCHECK_DB, (endTime - startTime));
		} catch (Exception e) {
			long endTime = System.currentTimeMillis();
			logger.error("Error: Failed to database health check.", e);
			return new Status(Status.FAILURE, HEALTHCHECK_DB, (endTime - startTime));
		}
	}

	private Status LabciCachingCheck() {
		long startTime = System.currentTimeMillis();
		try {
			String[] keys = hKHealthcheckProperties.getProductKeys().split(",");
			String tCodeSymbol = hKHealthcheckProperties.getTCodeSymbol();
			String fieldList = StringUtils.join(
					this.labciProperties.getLabciFields(this.appProps.getQuotesResponseLabciFields(keys), keys),
					SymbolConstant.SYMBOL_SEMISOLON);
			List<NameValuePair> labciParams = new ArrayList<NameValuePair>();
			labciParams.add(new BasicNameValuePair("SymbolList", tCodeSymbol + "," + tCodeSymbol + "d"));
			labciParams.add(new BasicNameValuePair("FieldList", fieldList));
			labciParams.add(new BasicNameValuePair("DataRepresentation", EquityHKHealthCheckService.FILE_SUFFIX));
			String labciReqParams = URLEncodedUtils.format(labciParams, EquityHKHealthCheckService.URL_FORMAT);
			String labciResponse = this.httpClientHelper.doGet(this.labciProperties.getLabciUrl(), labciReqParams,
					null);

			JAXBContext jaxbContext = JAXBContext.newInstance(Watchlist.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Watchlist labciResp = (Watchlist) unmarshaller.unmarshal(new StringReader(labciResponse));

			long endTime = System.currentTimeMillis();
			if (labciResp != null) {
				return new Status(Status.SUCCESS, EquityHKHealthCheckService.HEALTHCHECK_LABCI_CACHING,
						(endTime - startTime));
			} else {
				return new Status(Status.FAILURE, EquityHKHealthCheckService.HEALTHCHECK_LABCI_CACHING,
						(endTime - startTime));
			}

		} catch (Exception e) {
			long endTime = System.currentTimeMillis();
			return new Status(Status.FAILURE, EquityHKHealthCheckService.HEALTHCHECK_LABCI_CACHING,
					(endTime - startTime));
		}
	}

	private Status PredictiveSearchCheck() {
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		try {
			CommonRequestHeader commonHeader = this.hKHealthcheckProperties.getHeader();
			if (null == this.hKHealthcheckProperties.getKeyWord()) {
				EquityHKHealthCheckService.logger.error("Please check your configuration \"healthcheck: key-word\"");
				throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION);
			}
			List<String> symbols = Arrays.asList(this.hKHealthcheckProperties.getKeyWord());
			List<PredSrchResponse> responses = (List<PredSrchResponse>) this.predSrchService.precSrch(symbols,
					commonHeader);
			if (responses.isEmpty()) {
				EquityHKHealthCheckService.logger.error("Invalid response, result array is empty: " + responses);
				throw new VendorException(ExCodeConstant.EX_CODE_PREDSRCH_INVALID_RESPONSE);
			}
			endTime = System.currentTimeMillis();
			return new Status(Status.SUCCESS, EquityHKHealthCheckService.HEALTHCHECK_PREDICTIVE_SEARCH,
					(endTime - startTime));
		} catch (Exception e) {
			endTime = System.currentTimeMillis();
			return new Status(Status.FAILURE, EquityHKHealthCheckService.HEALTHCHECK_PREDICTIVE_SEARCH,
					(endTime - startTime));
		}
	}
}
