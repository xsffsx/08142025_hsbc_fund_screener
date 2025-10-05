package com.dummy.wpb.product.utils;

import com.dummy.wpb.product.component.ImportEliFinDocService;
import lombok.SneakyThrows;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;
import java.util.concurrent.ExecutorService;

public class EliFinDocPathListener extends FileAlterationListenerAdaptor {

	private String ctry;
	private String group;
	private String prodType;
	private ImportEliFinDocService importEliFinDocService;
	private ExecutorService executorService;
	private EliTsFinDocUtil eliTsFinDocUtil;

	public EliFinDocPathListener(String ctry, String group, String prodType, ImportEliFinDocService importEliFinDocService, EliTsFinDocUtil eliTsFinDocUtil) {
		this.ctry = ctry;
		this.group = group;
		this.prodType = prodType;
		this.importEliFinDocService = importEliFinDocService;
		this.eliTsFinDocUtil = eliTsFinDocUtil;
	}

	@Override
	public void onFileCreate(final File file) {
		executorService.execute(new Runnable() {
			@SneakyThrows
			@Override
			public void run()  {
				eliTsFinDocUtil.handleFinDocFile(file, ctry, group, prodType, importEliFinDocService, false);
			}
		});
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

}
