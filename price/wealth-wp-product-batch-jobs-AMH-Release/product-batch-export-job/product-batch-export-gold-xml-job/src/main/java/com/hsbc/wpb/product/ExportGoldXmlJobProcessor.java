package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.constant.ProductType;
import com.dummy.wpb.product.model.xml.DigitalAssetCurrency;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@Slf4j
@StepScope
public class ExportGoldXmlJobProcessor implements ItemProcessor<Document, DigitalAssetCurrency>, StepExecutionListener {

    @Autowired
    private ExportGoldXmlJobService exportGoldXmlJobService;

    @Value("${batch.outgoing.path}")
    private String outgoingPath;

    @Value("#{jobParameters['ctryRecCde']}")
    String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    String grpMembrRecCde;

    @Value("#{jobParameters['systemCde']}")
    String systemCde;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("=========================================================");
        log.info("Generate Digital Asset Currency Tokenized Gold Product XML begins");
    }

    @Override
    @SneakyThrows
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExitStatus exitStatus = ExitStatus.FAILED;
        // file path = /appvol/product-spring-batch/data/outgoing/HKHBAP/GSOPSD/
        String filePath = String.format("%s/%s/%s/", outgoingPath, ctryRecCde + grpMembrRecCde, systemCde);
        // create *.out header file
        String timestamp = StringUtils.substring(System.getProperty(BatchConstants.EGRESS_GSOPSD_LAST_SUCCESSFUL_DT_TM), 0, 12);
        String sequence = System.getProperty(BatchConstants.EGRESS_GSOPSD_SEQ);
        String fileName = String.format("%s_%s_%s_%s_%s_%s.out",
                ctryRecCde, grpMembrRecCde, systemCde, ProductType.DAC.name(), timestamp, sequence);
        // create *.out header file
        if (createHeaderFile(filePath, fileName)) {
            exitStatus = ExitStatus.COMPLETED;
        }
        log.info("Generate Digital Asset Currency Tokenized Gold Product XML ends");
        return exitStatus;
    }

    @Override
    public DigitalAssetCurrency process(Document product) {
        log.info("Exporting product (ctryRecCde: {}, grpMembrRecCde: {}, prodTypeCde: {}, prodAltPrimNum: {})",
                product.getString(Field.ctryRecCde),
                product.getString(Field.grpMembrRecCde),
                product.getString(Field.prodTypeCde),
                product.getString(Field.prodAltPrimNum));

        return exportGoldXmlJobService.getDigitalAssetCurrency(product);
    }

    /**
     * Crate *.out header file
     * @return true - succeeded, false - failed
     */
    private boolean createHeaderFile(String filePath, String fileName) throws IOException {
        boolean result = false;
        File file = new File(filePath + fileName);
        log.info("Creating {}", fileName);
        if (!file.exists()) {
            if (file.createNewFile()) {
                result = true;
            } else {
                log.error("Failed to create {}", fileName);
            }
        } else {
            result = true;
        }
        return result;
    }
}
