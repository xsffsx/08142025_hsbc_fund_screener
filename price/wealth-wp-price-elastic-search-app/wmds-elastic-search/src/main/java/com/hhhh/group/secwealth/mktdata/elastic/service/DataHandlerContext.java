package com.hhhh.group.secwealth.mktdata.elastic.service;

import com.hhhh.group.secwealth.mktdata.elastic.processor.ProductProcessor;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataHandlerContext implements ApplicationContextAware {

    @Autowired
    private Map<String, ProductProcessor> processorContext = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ProductProcessor> processorMap = applicationContext.getBeansOfType(ProductProcessor.class);
        for (Map.Entry<String, ProductProcessor> entries : processorMap.entrySet()) {
            String processorName = entries.getKey();
            if (StringUtil.isValid(processorName)) {
                int index = processorName.indexOf("ProductProcessor");
                if (index != -1) {
                    String nodeName = processorName.substring(0, index).toLowerCase();
                    this.processorContext.put(nodeName, entries.getValue());
                }
            }
        }
    }

    public ProductProcessor getProcessorByName(String nodeName) {
        return this.processorContext.get(nodeName);
    }
}
