package com.dummy.wpb.product.reader;

import com.dummy.wpb.product.model.ProductKey;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.xml.DebtInstm;
import com.dummy.wpb.product.model.xml.ProductKeySegment;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.util.DocumentUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.dummy.wpb.product.constant.ConfigConstants.PRODUCT_CODE_ALTERNATED_CLASS_CODE_PRIMARY;

@Component
@StepScope
public class WisdomBondItemReader extends ProductStreamItemByKeyItemReader<DebtInstm> {

    @Value("${batch.file.pattern}")
    private String filePattern;

    @Value("#{jobParameters['incomingPath']}")
    private String incomingPath;

    @Value("${batch.size}")
    private int batchSize;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Value("${batch.top-seller-num}")
    private int topSellerNum;

    @Override
    public void afterPropertiesSet() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(DebtInstm.class);
        StaxEventItemReader<DebtInstm> staxEventItemReader = new StaxEventItemReader<>();
        staxEventItemReader.setFragmentRootElementName("debtInstm");
        staxEventItemReader.setUnmarshaller(marshaller);
        filePattern = filePattern.replace("{ctryRecCde}", ctryRecCde).replace("{grpMembrRecCde}", grpMembrRecCde);

        this.setName("wisdomBondXmlFileReader");
        this.setProductService(productService);
        this.setPageSize(batchSize);
        this.setToProductKeyFunction(TO_PRODUCT_KEY);
        this.setToProductFunction(DocumentUtil::parseObject);
        this.setDelegate(new ImportFileItemReader<>(incomingPath, filePattern, staxEventItemReader));

        super.afterPropertiesSet();
    }

    @Override
    protected ProductStreamItem doRead() throws Exception {
        if (stepExecution.getWriteCount() >= topSellerNum) {
            // can stop job now
            return null;
        }

        return super.doRead();
    }

    @Override
    @Value("#{jobParameters['ctryRecCde']}")
    public void setCtryRecCde(String ctryRecCde) {
        super.setCtryRecCde(ctryRecCde);
    }

    @Override
    @Value("#{jobParameters['grpMembrRecCde']}")
    public void setGrpMembrRecCde(String grpMembrRecCde) {
        super.setGrpMembrRecCde(grpMembrRecCde);
    }

    @Override
    @Autowired
    public void setProductService(ProductService productService) {
        super.setProductService(productService);
    }

    private static final Function<DebtInstm, ProductKey> TO_PRODUCT_KEY = debitInstrument -> {
        ProductKeySegment prodKeySeg = debitInstrument.getProdKeySeg();
        ProductKey productKey = new ProductKey();
        BeanUtils.copyProperties(prodKeySeg, productKey);
        productKey.setCcyProdCde(prodKeySeg.getCcyProdCde());
        productKey.setProdAltNum(prodKeySeg.getProdCde());
        productKey.setCtryProdTradeCde(prodKeySeg.getCtryProdTradeCde());
        productKey.setProdTypeCde(prodKeySeg.getProdTypeCde());

        String prodCdeAltClassCde = StringUtils.defaultIfBlank(productKey.getProdCdeAltClassCde(), PRODUCT_CODE_ALTERNATED_CLASS_CODE_PRIMARY);
        productKey.setProdCdeAltClassCde(prodCdeAltClassCde);
        return productKey;
    };

}
