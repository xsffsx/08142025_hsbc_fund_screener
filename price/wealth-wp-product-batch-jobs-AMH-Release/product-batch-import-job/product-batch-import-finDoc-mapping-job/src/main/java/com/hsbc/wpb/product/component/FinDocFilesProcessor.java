package com.dummy.wpb.product.component;

import com.dummy.wpb.product.model.FinDocMapInput;
import com.dummy.wpb.product.validation.MappingValidation;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@StepScope
@Component
public class FinDocFilesProcessor implements ItemProcessor<FinDocMapInput,FinDocMapInput> {
    
    @Value("#{stepExecution}")
    private StepExecution stepExecution;
    
    @Autowired
    private MappingValidation fdv;

    @SuppressWarnings({ "unchecked" })
	@Override
    public FinDocMapInput process(FinDocMapInput item) throws Exception {
    	
    	List<String> mappingFileNames = (List<String>) stepExecution.getJobExecution().getExecutionContext().get("mappingFileNames");
        if (CollectionUtils.isEmpty(mappingFileNames)) return null;
    	String mappingFileName = mappingFileNames.stream().collect(Collectors.joining(","));

    	item.setInputFileName(mappingFileName);
        fdv.setFd(item);

        if (fdv.validation()) {
        	return item;
        } else {
        	return null;
        }

    }
}
