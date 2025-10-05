package com.dummy.wpb.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.*;
import com.dummy.wpb.product.util.XmlUtils;
import com.dummy.wpb.product.utils.CommonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Slf4j
public class PW0JsonFileReader extends AbstractItemCountingItemStreamItemReader<List<TableObject>> {
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Value("${batch.soap}")
    private String soapHost;

    public static final String REQ_PRC_EFF_DT = "<valueList xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xsi:type=\"xs:string\">%s</valueList>";
    private List<CompletableFuture<List<TableObject>>> completableFutures;
    private static final ObjectMapper mapper = new ObjectMapper();
    private final String prodTypCde;
    private final String targetName;
    private final String ctryRecCde;
    private final String grpMembrRecCde;

    public PW0JsonFileReader(String ctryRecCde, String grpMembrRecCde, String prodTypCde, String targetName) {
        setName("PW0JsonFileReader");
        this.ctryRecCde = ctryRecCde;
        this.grpMembrRecCde = grpMembrRecCde;
        this.prodTypCde = prodTypCde;
        this.targetName = targetName;
    }

    @Override
    public List<TableObject> doRead(){
        try {
            return completableFutures.get(getCurrentItemCount() - 1).get();
        } catch (InterruptedException e) {
            log.error("The thread calling pw0 request is interrupted.", e);
            Thread.currentThread().interrupt();
            throw new productBatchException(e);
        } catch (Exception e) {
            log.error("The thread calling pw0 request encountered an exception.", e);
            throw new productBatchException(e);
        }
    }

    @Override
    public void doOpen() {
        List<List<LocalDate>> prcEffDtChunkList = this.getPrcEffDtList(ctryRecCde, grpMembrRecCde, prodTypCde);
        completableFutures = new ArrayList<>();
        for (List<LocalDate> dates : prcEffDtChunkList) {
            StringBuilder prcEffDtStr = new StringBuilder();
            for (LocalDate date : dates) {
                prcEffDtStr.append(String.format(REQ_PRC_EFF_DT, date.toString()));
            }
            completableFutures.add(
                    CompletableFuture.supplyAsync(() -> this.getPW0Response(
                            StringUtils.replaceEach(CommonUtils.readResource("/request/pw0-request.xml"),
                            new String[]{"${ctryRecCde}", "${grpMembrRecCde}", "${prodTypCde}", "${prcEffDt}", "${targetName}"},
                            new String[]{ctryRecCde, grpMembrRecCde, prodTypCde, prcEffDtStr.toString(), targetName})),
                            threadPoolTaskExecutor)
            );
        }

        completableFutures.add(
                CompletableFuture.supplyAsync(() -> this.getPW0Response(
                        StringUtils.replaceEach(CommonUtils.readResource("/request/pw0-nullPrcEffDt-request.xml"),
                        new String[]{"${ctryRecCde}", "${grpMembrRecCde}", "${prodTypCde}", "${targetName}"},
                        new String[]{ctryRecCde, grpMembrRecCde, prodTypCde, targetName})),
                        threadPoolTaskExecutor));


        setMaxItemCount(completableFutures.size());
    }

    @Override
    public void doClose() {
        if (CollectionUtils.isNotEmpty(completableFutures)) {
            completableFutures.clear();
        }
    }

    private List<List<LocalDate>> getPrcEffDtList(String ctryRecCde, String grpMembrRecCde, String prodTypCde) {
        LocalDate localDate = LocalDate.of(1970, 1, 1);
        List<List<LocalDate>> prcEffDtChunkList = new ArrayList<>();
        //query prcEffDt before 1970
        Query query = new Query();
        query.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde));
        query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde));
        query.addCriteria(Criteria.where(Field.prodTypeCde).is(prodTypCde));
        query.addCriteria(new Criteria().andOperator(Criteria.where(Field.prcEffDt).ne(null), Criteria.where(Field.prcEffDt).lt(localDate)));
        query.fields().include(Field.prcEffDt);
        query.with(Sort.by(Sort.Direction.ASC, Field.prcEffDt));
        List<LocalDate> localDateList = mongoTemplate.findDistinct(query, Field.prcEffDt, Product.class, LocalDate.class);
        if (CollectionUtils.isNotEmpty(localDateList)) {
            prcEffDtChunkList.add(localDateList);
        }

        //query min prcEffDt after 1970
        Query minPrcEffDtQuery = new Query();
        minPrcEffDtQuery.addCriteria(Criteria.where(Field.ctryRecCde).is(ctryRecCde));
        minPrcEffDtQuery.addCriteria(Criteria.where(Field.grpMembrRecCde).is(grpMembrRecCde));
        minPrcEffDtQuery.addCriteria(Criteria.where(Field.prodTypeCde).is(prodTypCde));
        minPrcEffDtQuery.addCriteria(new Criteria().andOperator(Criteria.where(Field.prcEffDt).ne(null), Criteria.where(Field.prcEffDt).gte(localDate)));
        minPrcEffDtQuery.fields().include(Field.prcEffDt);
        minPrcEffDtQuery.with(Sort.by(Sort.Direction.ASC, Field.prcEffDt));
        Product product = mongoTemplate.findOne(minPrcEffDtQuery, Product.class);
        if (Objects.isNull(product)) {
            return Collections.emptyList();
        }

        LocalDate minPrcEffDt = product.getPrcEffDt();
        List<LocalDate> totalDates = new ArrayList<>();
        while (!minPrcEffDt.isAfter(LocalDate.now())) {
            totalDates.add(minPrcEffDt);
            minPrcEffDt = minPrcEffDt.plusDays(1);
        }

        Map<Integer, List<LocalDate>> groupedByYear = totalDates.stream().collect(Collectors.groupingBy(LocalDate::getYear));
        groupedByYear.forEach((year, datesInYear) -> {
            if (datesInYear.size() < 182) {
                prcEffDtChunkList.add(datesInYear);
                return;
            }
            int middleIndex = datesInYear.size() / 2;
            List<LocalDate> firstHalf = datesInYear.subList(0, middleIndex);
            List<LocalDate> secondHalf = datesInYear.subList(middleIndex, datesInYear.size());
            prcEffDtChunkList.add(firstHalf);
            prcEffDtChunkList.add(secondHalf);
        });
        return prcEffDtChunkList;
    }

    @SneakyThrows
    private List<TableObject> getPW0Response(String request) {
        RequestEntity<String> requestEntity = RequestEntity
                .post(soapHost + "/wpcwebservice/WpcDataEnquiryWebService")
                .contentType(MediaType.APPLICATION_XML)
                .body(request);

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        if (hasSuccessBody(responseEntity)) {
            EnquireJsonResponse enquireJsonResponse = XmlUtils.unmarshal(responseEntity.getBody(), EnquireJsonResponse.class);
            WpcDataEnquiryJsonResponse wpcDataEnquiryJsonResponse = enquireJsonResponse.getReturn();
            WpcDataEnquiryResponse dataEnquiryResponse = new WpcDataEnquiryResponse();
            List<TableObject> tableObjects = mapper.readValue(wpcDataEnquiryJsonResponse.getTableObjectJson(),
                    new TypeReference<List<TableObject>>() {});
            dataEnquiryResponse.getTableObject().addAll(tableObjects);
            return tableObjects;
        } else {
            log.error("No normal response when calling pw0 request. Status code: {}, Response: {}",
                    responseEntity.getStatusCodeValue(), responseEntity.getBody());
            return Collections.emptyList();
        }
    }

    private boolean hasSuccessBody(ResponseEntity<?> responseEntity) {
        return responseEntity.getStatusCodeValue() == HttpStatus.SC_OK && responseEntity.getBody() != null;
    }

}
