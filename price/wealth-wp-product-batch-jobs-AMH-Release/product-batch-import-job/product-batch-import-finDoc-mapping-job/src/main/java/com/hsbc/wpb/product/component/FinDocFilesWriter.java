package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.FinDocConstants;
import com.dummy.wpb.product.exception.productBatchException;
import com.dummy.wpb.product.model.FinDocMapInput;
import com.dummy.wpb.product.validation.MappingValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@StepScope
@Component
@Slf4j
public class FinDocFilesWriter implements ItemWriter<FinDocMapInput> {

    @Autowired
    private MappingValidation fdv;

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Autowired
    public FinDocProdRLService finDocProdRLService;

    private final AtomicInteger addCnt;
    private final AtomicInteger delCnt;
    private final AtomicInteger chgCnt;

	public FinDocFilesWriter() {
		addCnt = new AtomicInteger(0);
		delCnt = new AtomicInteger(0);
		chgCnt = new AtomicInteger(0);
	}


    @Override
	public void write(List<? extends FinDocMapInput> items) throws Exception {
		log.info("Begin to write items to MongoDB");
		processBatch(items);
		log.info("End to write items to MongoDB");
	}

	private void processBatch(List<? extends FinDocMapInput> items) {
		try {
			List<FinDocMapInput> addItems = new ArrayList<>();
			List<FinDocMapInput> deleteItems = new ArrayList<>();
			List<FinDocMapInput> changeItems = new ArrayList<>();

			for (FinDocMapInput fdi : items) {
				switch (fdi.getActnCde()) {
					case FinDocConstants.ADD_ACTION:
							addItems.add(fdi);
						break;
					case FinDocConstants.DELETE_ACTION:
							deleteItems.add(fdi);
						break;
					case FinDocConstants.CHANGE_ACTION:
							changeItems.add(fdi);
						break;
					default:
						fdv.getRejectRec().add(fdv.rejectMail(fdi, FinDocConstants.REC_PROBLEM, log));
						break;
				}
			}

			if (!addItems.isEmpty()) {
				finDocProdRLService.processProdRLBatch(addItems, fdv, FinDocConstants.ADD_ACTION);
				addCnt.addAndGet(addItems.size());
			}
			if (!changeItems.isEmpty()) {
				finDocProdRLService.processProdRLBatch(changeItems, fdv, FinDocConstants.CHANGE_ACTION);
				chgCnt.addAndGet(changeItems.size());
			}
			if (!deleteItems.isEmpty()) {
				finDocProdRLService.processProdRLBatch(deleteItems, fdv, FinDocConstants.DELETE_ACTION);
				delCnt.addAndGet(deleteItems.size());
			}
		} catch (Exception hfe) {
			throw new productBatchException(hfe);
		} finally {
			stepExecution.getJobExecution().getExecutionContext().putInt("addCnt", addCnt.intValue());
			stepExecution.getJobExecution().getExecutionContext().putInt("delCnt", delCnt.intValue());
			stepExecution.getJobExecution().getExecutionContext().putInt("chgCnt", chgCnt.intValue());
		}
	}

}
