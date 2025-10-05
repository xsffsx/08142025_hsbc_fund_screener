package com.dummy.wpb.product.utils;

import com.dummy.wpb.product.ImportTermsheetService;
import com.dummy.wpb.product.configuration.TermsheetConfiguration;
import lombok.SneakyThrows;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;
import java.util.concurrent.ExecutorService;

public class SidFinDocPathListener extends FileAlterationListenerAdaptor {

	private final File directory;
	private String ctry;
	private String group;
	private String prodType;
	private ExecutorService executorService;
	private ImportTermsheetService importTermsheetService;
	private TermsheetConfiguration termsheetConfiguration;

	public SidFinDocPathListener(File directory, String ctry, String group, String prodType, ImportTermsheetService importTermsheetService,
								 TermsheetConfiguration termsheetConfiguration) {
		this.directory = directory;
		this.ctry = ctry;
		this.group = group;
		this.prodType = prodType;
		this.importTermsheetService = importTermsheetService;
		this.termsheetConfiguration = termsheetConfiguration;
	}

	@Override
	public void onFileCreate(final File file) {
		executorService.execute(new Runnable() {
			@SneakyThrows
			@Override
			public void run() {SidTsFinDocUtil.postProcess(directory, ctry, group, prodType, importTermsheetService, termsheetConfiguration);}
		});
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

}
