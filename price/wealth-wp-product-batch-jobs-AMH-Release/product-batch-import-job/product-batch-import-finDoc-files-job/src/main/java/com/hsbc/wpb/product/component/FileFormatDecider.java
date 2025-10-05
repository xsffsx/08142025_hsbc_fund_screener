package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.FinDocConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FileFormatDecider implements JobExecutionDecider {

    private int count;

    public FileFormatDecider() {
        this.count = 0;
    }

    //set file in jobContext
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        List<String> filePathLst = (ArrayList<String>)jobContext.get("fileLst");

        count++;
        if (!CollectionUtils.isEmpty(filePathLst) && count <= filePathLst.size()) {
            String curFilePath = filePathLst.get(count-1);
            File curFile = new File(curFilePath);
            jobContext.put("curFilePath", curFilePath);
            if (curFile.getName().toLowerCase().endsWith(FinDocConstants.SUFFIX_XLS)) {
                return new FlowExecutionStatus("Excel");
            }else if (curFile.getName().toLowerCase().endsWith(FinDocConstants.SUFFIX_CSV)) {
                return new FlowExecutionStatus("CSV");
            }else {
                return FlowExecutionStatus.COMPLETED;
            }
        }else {
            return FlowExecutionStatus.COMPLETED;
        }
    }
}
