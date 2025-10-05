package com.dummy.wpb.wpc.utils.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.wpc.utils.CommonUtils;
import com.dummy.wpb.wpc.utils.ProductCompareUtils;
import com.dummy.wpb.wpc.utils.load.ProductCompareLoader;
import com.dummy.wpb.wpc.utils.load.ProductDataReLoader;
import com.dummy.wpb.wpc.utils.model.Difference;
import com.dummy.wpb.wpc.utils.service.AdminLogService;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/product")
public class ProductDataController {
    @Autowired
    private AdminLogService adminLogService;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private MongoDatabase mongodb;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProductCompareLoader productCompareLoader;

    /**
     * Reload products by given prodId list, like 111,222,333.
     * For given products, data will be retrieved from Oracle and remodel to store in MongoDB.
     *
     */
    @GetMapping(value = "/reload")
    public ResponseEntity<Object> productReload(@RequestParam("productIdList") String productIdStr) {
        try {
            String[] productIdStrlist = productIdStr.split(",");
            List<Long> productIdList = Arrays.stream(productIdStrlist).map(Long::parseLong).collect(Collectors.toList());
            ProductDataReLoader productLoader = new ProductDataReLoader(productIdList, namedParameterJdbcTemplate, mongodb);
            productLoader.load();
            return new ResponseEntity<>("Product data reloaded: " + productIdList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(CommonUtils.exceptionInfo(e), HttpStatus.PARTIAL_CONTENT);
        }
    }

    /**
     * Compare result,
     * oracle - data from Oracle
     * mongoId - data from MongoDB
     *
     */
    @GetMapping(value = "/compare")
    public ResponseEntity<Object> productCompare(@RequestParam("productIdList") String productIdStr) {
        log.info("productCompare: {}", productIdStr);
        try {
            String[] productIdStrlist = productIdStr.split(",");
            List<Long> productIdList = Arrays.stream(productIdStrlist).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
            return new ResponseEntity<>(productCompareLoader.compare(productIdList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(CommonUtils.exceptionInfo(e), HttpStatus.PARTIAL_CONTENT);
        }
    }

    /**
     * Compare product data by oracleId and mongoId,
     * oracle - data from Oracle
     * mongo - data from MongoDB
     */
    @GetMapping(value = "/compareById")
    public ResponseEntity<Object> productCompare(@RequestParam("oracleId") Long oracleId, @RequestParam("mongoId") Long mongoId) {
        log.info("productCompare: oracleId:{}, mongoId:{}", oracleId, mongoId);
        try {
            return new ResponseEntity<>(productCompareLoader.compare(oracleId,mongoId),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(CommonUtils.exceptionInfo(e), HttpStatus.PARTIAL_CONTENT);
        }
    }

    /**
     * auto compare result,
     * left - data from Oracle
     * right - data from MongoDB
     *
     */
    @GetMapping(value = "/autoCompare")
    public ResponseEntity<Object> autoCompare(HttpServletRequest request, @RequestParam("startIndex") Integer startIndex) {
        try {
            adminLogService.log(request);
            List<Long> productIdList = productCompareLoader.retrieveProdIds(startIndex);
            return new ResponseEntity<>(productCompareLoader.compare(productIdList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(CommonUtils.exceptionInfo(e), HttpStatus.PARTIAL_CONTENT);
        }
    }

    /**
     * Compare product data which input by user. It's for testing purpose.
     *
     */
    @RequestMapping(value = "/compareJson", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Object> productCompareJson(@RequestBody(required = false) Map<String, String> params) {
        try {
            Map<String, Object> map1 = objectMapper.readValue(params.get("jsonStr1"), Map.class);
            Map<String, Object> map2 = objectMapper.readValue(params.get("jsonStr2"), Map.class);
            Set<Difference> differences = ProductCompareUtils.compare(map1, map2);
            Set<Map<String, Object>> diffMap = differences.stream().map(difference -> difference.toMap("product1", "product2")).collect(Collectors.toSet());
            return new ResponseEntity<>(diffMap, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(CommonUtils.exceptionInfo(e), HttpStatus.PARTIAL_CONTENT);
        }
    }

}
