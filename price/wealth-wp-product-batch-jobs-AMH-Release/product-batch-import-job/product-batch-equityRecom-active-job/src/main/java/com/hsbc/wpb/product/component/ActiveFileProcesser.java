package com.dummy.wpb.product.component;

import com.dummy.wpb.product.jpo.EquityRecommendationsPo;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@StepScope
public class ActiveFileProcesser implements ItemProcessor<EquityRecommendationsPo,Map<EquityRecommendationsPo, List<Document>>> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public EquityRecomService equityRecomService;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Value("#{jobParameters['ctryRecCde']}")
    private String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    //get other EquityRecommendations fields from collection base on Reuters. If not match any records, will not update to product.
    @Override
    public Map<EquityRecommendationsPo, List<Document>> process(EquityRecommendationsPo item) throws Exception {
        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();

        Query query = new Query();
        query.addCriteria(Criteria.where("reuters").is(item.getReuters()));
        EquityRecommendationsPo equityRecCov = mongoTemplate.findOne(query, EquityRecommendationsPo.class);
        if (equityRecCov != null) {
            item.setIsin(equityRecCov.getIsin());
            item.setRating(equityRecCov.getRating());
            item.setTargetPrice(equityRecCov.getTargetPrice());
            item.setUpside(equityRecCov.getUpside());
            item.setUrl(equityRecCov.getUrl());
            item.setSector(equityRecCov.getSector());
            item.setIndustryGroup(equityRecCov.getIndustryGroup());
            item.setForwardPe(equityRecCov.getForwardPe());
            item.setForwardPb(equityRecCov.getForwardPb());
            item.setForwardDividendYield(equityRecCov.getForwardDividendYield());
            item.setHistoricDividendYield(equityRecCov.getHistoricDividendYield());
            item.setRecommended("Y");

            //search if match any product
            List<Document> prodDocs = equityRecomService.productByFilters(item, ctryRecCde, grpMembrRecCde);
            if (CollectionUtils.isEmpty(prodDocs)) {
                log.error(String.format("Record with isin= %S or reuters= %S cannot match any product.",
                        item.getReuters(), item.getIsin()));
                jobContext.put("prodNotFound", jobContext.getInt("prodNotFound") + 1);
                return null;
            }

            Map<EquityRecommendationsPo, List<Document>> returnMap = new HashMap<>();
            returnMap.put(item, prodDocs);
            return returnMap;
        }
        log.error(String.format("Record with reuters= %S cannot match any coverage record.", item.getReuters()));
        return null;
    }
}
