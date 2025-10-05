package com.hhhh.group.secwealth.mktdata.elastic.controller;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.elastic.bean.ai.response.StockListResponse;
import com.hhhh.group.secwealth.mktdata.elastic.bean.ai.response.StockListV2Response;
import com.hhhh.group.secwealth.mktdata.elastic.bean.ai.stock.StockListRequest;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.*;
import com.hhhh.group.secwealth.mktdata.elastic.service.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.helper.InternalSearchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.util.InternalProductKeyUtil;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header.RequestHeader;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.parameter.JsonRequestParam;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@RestController
@RequestMapping("/wealth/api/v1/market-data/product")
public class PredsrchController {

	@Autowired
	StockListService stockListService;

	@Autowired
	StockListV2Service stockListV2Service;

	@Autowired
	private PredsrchService predsrchService;
	@Autowired
	private MultiPredsrchService multiPredsrchService;
	@Autowired
	private InternalProductKeyUtil internalProductKeyUtil;
	@Autowired
	private ScheduleDataLoadService scheduleDataLoadService;

	@ApiOperation(value = "multi Predsrch", notes = "multi Predsrch", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {@ApiResponse(code = 200, response = PredSrchResponse.class, message = "")})
	@RequestMapping(path = "/multi-predictive-search", method = {RequestMethod.GET, RequestMethod.POST})
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-App-Code", value = "The application code which identify the application of request.Sample value: \"STB\"", required = true, defaultValue = "STB"),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Chnl-CountryCode", value = "The country code of this entity request.Sample value: \"HK\"\n" + "取值范围: 2", required = true, defaultValue = "HK"),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Chnl-Group-Member", value = "The group member code of this entity request.Sample value: \"hhhh\"\n" + "取值范围: 4", required = true, defaultValue = "hhhh"),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Channel-Id", value = "The channel id which identify the source of request.Sample value: \"OHI\"", required = true, defaultValue = "OHI"),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Customer-Id", value = "The customer ID.", required = false, defaultValue = ""),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Locale", value = "The locale code of the request. Possible values - en/en_US(English), zh_HK (Traditional Chinese), zh_CN/zh(Simplified Chinese). 允许值: \"en\", \"en_US\", \"zh_HK\", \"zh_CN\", \"zh\"", required = true, defaultValue = "zh_HK"),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Session-Correlation-Id", value = "Session Correlation ID.", required = false, defaultValue = ""),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Saml", value = "The SAML end to end trust token.", required = false, defaultValue = ""),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Saml3", value = "The SAML end to end trust token.", required = true, defaultValue = "&ltsaml:Assertion xmlns:saml=\"http://www.hhhh.com/saas/assertion\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ID=\"id_3b02c13c-6d0f-4306-b65c-60cee8f66d58\" IssueInstant=\"2020-01-14T09:29:42.886Z\" Version=\"3.0\"&gt&ltsaml:Issuer&gthttps://www.hhhh.com/rbwm/dtp&lt/saml:Issuer&gt&ltds:Signature&gt&ltds:SignedInfo&gt&ltds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/&gt&ltds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/&gt&ltds:Reference URI=\"#id_3b02c13c-6d0f-4306-b65c-60cee8f66d58\"&gt&ltds:Transforms&gt&ltds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/&gt&ltds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"&gt&ltds:InclusiveNamespaces xmlns:ds=\"http://www.w3.org/2001/10/xml-exc-c14n#\" PrefixList=\"#default saml ds xs xsi\"/&gt&lt/ds:Transform&gt&lt/ds:Transforms&gt&ltds:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/&gt&ltds:DigestValue&gtmiH8Oii5TAV4HFy/CotCWSXEzDUoavZryV9rzKirdbk=&lt/ds:DigestValue&gt&lt/ds:Reference&gt&lt/ds:SignedInfo&gt&ltds:SignatureValue&gtTD29zb6exGWe+K2VVJDYPEn6Izb3/I9a1Q3ov4XtcGmRz0ZyBh/g8mSec3dIoiYA+dUOFWEX0etQ 9il8lFbp0EfbJrpVW+CAAe1KjQRfhIbzI2QRkdZBv49EyM5xl3EJ+5kzkmy65RvYivtPQfkDC1+W 1VTIKohZTasBQuCE80mmKhTAPYF/wnUwvJKuaoh/YGxkGDysYzjy/RJGohSE54z1yEoz3ZMeetEd 4Ou6FXbdkKbhaatknGDJIcMZue3CFQLOZcu1OZRgNJUbX8wDB2vktqE9zVApCvuREJ5bntr6Ec7E QfoKpqQ2ZEd2p2UEiN/LplTLZL1xHfLKrLwaaA==&lt/ds:SignatureValue&gt&lt/ds:Signature&gt&ltsaml:Subject&gt&ltsaml:NameID&gtHK00456417588801&lt/saml:NameID&gt&lt/saml:Subject&gt&ltsaml:Conditions NotBefore=\"2020-01-14T09:29:41.886Z\" NotOnOrAfter=\"2020-01-14T09:30:12.886Z\"/&gt&ltsaml:AttributeStatement&gt&ltsaml:Attribute Name=\"GUID\"&gt&ltsaml:AttributeValue&gt4061cb10-cdad-11dd-bfe4-000309040604&lt/saml:AttributeValue&gt&lt/saml:Attribute&gt&ltsaml:Attribute Name=\"CAM\"&gt&ltsaml:AttributeValue&gt40&lt/saml:AttributeValue&gt&lt/saml:Attribute&gt&lt/saml:AttributeStatement&gt&lt/saml:Assertion&gt"),

			@ApiImplicitParam(paramType = "query", dataType = "string", name = "param", required = true,
					defaultValue = "{\"keyword\": [\"000001\",\"60006\",\"00005\"],\"assetClasses\": [\"SEC\",\"WRTS\"]}",
					value = "1. market type=String [true] The investment market of the product.\n" + "\tCN - China\n" + "\tHK - Hong Kong\n" +
							"2. keyword type=String [true] The searching keyword as inputed by customer.\n" + " \tSample value: \"5\", \"hhhh\", \"滙豐\", \"汇丰\"\n" +
							"3. assetClasses type=String[] [true] The asset classification of the product.\n" + "\t允许值: \"SEC\", \"WRTS\"\n" +
							"4. filterCriterias type=Object[] [false] The criteria for filtering searched results." +
							"5. criteriaKey type=String [true] The key of filtering criteria. Possible values as below:\n" + "\tproductKey\n" + "\texchange\n" + "\n" +
							"6. criteriaValue type=String [true] The value of filtering criteria.\n" + "   Different value according to different criteriaKey defined.\n" + "\tproductKey sample usage: \"M:00005:HK:SEC\"\n" + "\texchange sample usage: \"SHAS:SZAS\"\n" +
							"7. operator type=String [true] The operator of filtering criteria.\n" + "\t允许值: \"gt\", \"ge\", \"eq\", \"lt\", \"le\", \"ne\", \"in\"\n" + "\n"
			)})
	public List<PredSrchResponse> multiPredsrch(String symbol,
			@JsonRequestParam("param|body") final MultiPredSrchRequest request,
			@RequestHeader final CommonRequestHeader header) throws Exception {
		return multiPredsrchService.doService(request, header);
	}

	@ApiOperation(value = "predictive search", notes = "predictive search", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {@ApiResponse(code = 200, response = PredSrchResponse.class, message = "")})
	@RequestMapping(path = "/predictive-search", method = {RequestMethod.GET, RequestMethod.POST})
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-App-Code", value = "The application code which identify the application of request.Sample value: \"STB\"", required = true, defaultValue = "STB"),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Chnl-CountryCode", value = "The country code of this entity request.Sample value: \"HK\"\n" + "取值范围: 2", required = true, defaultValue = "HK"),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Chnl-Group-Member", value = "The group member code of this entity request.Sample value: \"hhhh\"\n" + "取值范围: 4", required = true, defaultValue = "hhhh"),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Channel-Id", value = "The channel id which identify the source of request.Sample value: \"OHI\"", required = true, defaultValue = "OHI"),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Customer-Id", value = "The customer ID.", required = false, defaultValue = ""),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Locale", value = "The locale code of the request. Possible values - en/en_US(English), zh_HK (Traditional Chinese), zh_CN/zh(Simplified Chinese). 允许值: \"en\", \"en_US\", \"zh_HK\", \"zh_CN\", \"zh\"", required = true, defaultValue = "zh_HK"),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Session-Correlation-Id", value = "Session Correlation ID.", required = false, defaultValue = ""),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Saml", value = "The SAML end to end trust token.", required = false, defaultValue = ""),
			@ApiImplicitParam(paramType = "header", dataType = "string", name = "X-hhhh-Saml3", value = "The SAML end to end trust token.", required = true, defaultValue = "&ltsaml:Assertion xmlns:saml=\"http://www.hhhh.com/saas/assertion\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ID=\"id_3b02c13c-6d0f-4306-b65c-60cee8f66d58\" IssueInstant=\"2020-01-14T09:29:42.886Z\" Version=\"3.0\"&gt&ltsaml:Issuer&gthttps://www.hhhh.com/rbwm/dtp&lt/saml:Issuer&gt&ltds:Signature&gt&ltds:SignedInfo&gt&ltds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/&gt&ltds:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/&gt&ltds:Reference URI=\"#id_3b02c13c-6d0f-4306-b65c-60cee8f66d58\"&gt&ltds:Transforms&gt&ltds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/&gt&ltds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\"&gt&ltds:InclusiveNamespaces xmlns:ds=\"http://www.w3.org/2001/10/xml-exc-c14n#\" PrefixList=\"#default saml ds xs xsi\"/&gt&lt/ds:Transform&gt&lt/ds:Transforms&gt&ltds:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/&gt&ltds:DigestValue&gtmiH8Oii5TAV4HFy/CotCWSXEzDUoavZryV9rzKirdbk=&lt/ds:DigestValue&gt&lt/ds:Reference&gt&lt/ds:SignedInfo&gt&ltds:SignatureValue&gtTD29zb6exGWe+K2VVJDYPEn6Izb3/I9a1Q3ov4XtcGmRz0ZyBh/g8mSec3dIoiYA+dUOFWEX0etQ 9il8lFbp0EfbJrpVW+CAAe1KjQRfhIbzI2QRkdZBv49EyM5xl3EJ+5kzkmy65RvYivtPQfkDC1+W 1VTIKohZTasBQuCE80mmKhTAPYF/wnUwvJKuaoh/YGxkGDysYzjy/RJGohSE54z1yEoz3ZMeetEd 4Ou6FXbdkKbhaatknGDJIcMZue3CFQLOZcu1OZRgNJUbX8wDB2vktqE9zVApCvuREJ5bntr6Ec7E QfoKpqQ2ZEd2p2UEiN/LplTLZL1xHfLKrLwaaA==&lt/ds:SignatureValue&gt&lt/ds:Signature&gt&ltsaml:Subject&gt&ltsaml:NameID&gtHK00456417588801&lt/saml:NameID&gt&lt/saml:Subject&gt&ltsaml:Conditions NotBefore=\"2020-01-14T09:29:41.886Z\" NotOnOrAfter=\"2020-01-14T09:30:12.886Z\"/&gt&ltsaml:AttributeStatement&gt&ltsaml:Attribute Name=\"GUID\"&gt&ltsaml:AttributeValue&gt4061cb10-cdad-11dd-bfe4-000309040604&lt/saml:AttributeValue&gt&lt/saml:Attribute&gt&ltsaml:Attribute Name=\"CAM\"&gt&ltsaml:AttributeValue&gt40&lt/saml:AttributeValue&gt&lt/saml:Attribute&gt&lt/saml:AttributeStatement&gt&lt/saml:Assertion&gt"),
			@ApiImplicitParam(paramType = "query", dataType = "text", name = "param", required = true,
					defaultValue = "{\"assetClasses\":[\"SEC\"],\"market\":\"HK\",\"keyword\":\"00005\",\"topNum\":10}",
					value = "1. market type=String [true] The investment market of the product.\n" + "\tCN - China\n" + "\tHK - Hong Kong\n" + "\tUS - United States\n" +
							"2. keyword type=String [true] The searching keyword as inputed by customer.\n" + " \tSample value: \"5\", \"hhhh\", \"滙豐\", \"汇丰\"\n" +
							"3. assetClasses type=String[] [true] The asset classification of the product.\n" + "\t允许值: \"SEC\", \"WRTS\"\n" +
							"4. filterCriterias type=Object[] [false] The criteria for filtering searched results." +
							"5. criteriaKey type=String [true] The key of filtering criteria. Possible values as below:\n" + "\tproductKey\n" + "\texchange\n" + "\n" +
							"6. criteriaValue type=String [true] The value of filtering criteria.\n" + "   Different value according to different criteriaKey defined.\n" + "\tproductKey sample usage: \"M:00005:HK:SEC\"\n" + "\texchange sample usage: \"SHAS:SZAS\"\n" +
							"7. operator type=String [true] The operator of filtering criteria.\n" + "\t允许值: \"gt\", \"ge\", \"eq\", \"lt\", \"le\", \"ne\", \"in\"\n" + "\n" +
							"8. topNum type=Number [false] Numbers of records returned per request. Max is 100.\n" + "\tSample value: 10\n" +
							"9. channelRestrictCode type=String [false] Filter products using Channel Restriction Code.\n" +
							"10. restrOnlScribInd type=String [false] Filter products using Channel Restriction Indicator."
			)
	})
	public List<PredSrchResponse> predsrch(String symbol, @JsonRequestParam("param|body") final PredSrchRequest request,
			@RequestHeader final CommonRequestHeader header) throws Exception {
		return predsrchService.doService(request, header);
	}

	@GetMapping("/stocks")
	public StockListResponse getStockList(@JsonRequestParam("param|body") final StockListRequest stockListRequest, @RequestHeader final CommonRequestHeader header)
		throws Exception {
		return stockListService.doService(stockListRequest, header);
	}

	@GetMapping("/v2/stocks")
	public StockListV2Response getStockListV2(@RequestParam("countryTradableCode") String countryTradableCode,
											  @RequestParam("productType") String productType) {
		return this.stockListV2Service.findAllByMarketAndProductType(countryTradableCode, productType);
	}

	@RequestMapping("/loadData")
	@ResponseBody
	public String loadData() throws Exception {
		scheduleDataLoadService.loadData();
		return "success";
	}
	
	@RequestMapping("/internal-search")
	public SearchProduct getProductBySearchWithAltClassCde(@JsonRequestParam("param|body") final InternalSearchRequest request) throws Exception {
	    return internalProductKeyUtil.getProductBySearchWithAltClassCde(request);
	}
}
