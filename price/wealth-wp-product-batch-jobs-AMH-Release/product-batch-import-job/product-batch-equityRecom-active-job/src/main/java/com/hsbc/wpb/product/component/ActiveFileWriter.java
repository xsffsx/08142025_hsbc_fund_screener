package com.dummy.wpb.product.component;

import com.dummy.wpb.product.jpo.EquityRecommendationsPo;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@StepScope
public class ActiveFileWriter implements ItemWriter<Map<EquityRecommendationsPo, List<Document>>> {

    @Autowired
    public EquityRecomService equityRecomService;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Override
    public void write(List<? extends Map<EquityRecommendationsPo, List<Document>>> items) {
        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
        for (Map<EquityRecommendationsPo, List<Document>> itemMap : items) {
            for (Map.Entry<EquityRecommendationsPo, List<Document>> entry : itemMap.entrySet()) {
                try {
                    //update products with the same EquityRecommendations
                    equityRecomService.updateProdByFilters(entry.getValue(), entry.getKey());
                } catch (Exception e) {
                    jobContext.put("failUpdate", jobContext.getInt("failUpdate") + 1);
                    log.error("Update product failed. Reuters: " + entry.getKey().getReuters());
                }
            }
        }
    }
}
