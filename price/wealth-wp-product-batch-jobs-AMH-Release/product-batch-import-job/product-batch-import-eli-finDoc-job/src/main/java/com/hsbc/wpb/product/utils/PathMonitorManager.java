package com.dummy.wpb.product.utils;

import java.io.FileFilter;
import java.nio.file.Path;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class PathMonitorManager {

	private FileAlterationMonitor monitor;

	private static final long DEFLT_INTVERL = 3000L;
	
	public PathMonitorManager(Path path, FileAlterationListener listener, FileFilter fileFilter) {
		this(DEFLT_INTVERL, path, listener, fileFilter);
	}
	
	public PathMonitorManager(long interval, Path path, FileAlterationListener listener, FileFilter fileFilter) {
		FileAlterationObserver observer = new FileAlterationObserver(path.toFile(), fileFilter);
		observer.addListener(listener);
		monitor = new FileAlterationMonitor(interval, observer);
	}

	public void start() throws Exception {
		monitor.start();
	}

	public void stop() throws Exception {
		monitor.stop();
	}

}
