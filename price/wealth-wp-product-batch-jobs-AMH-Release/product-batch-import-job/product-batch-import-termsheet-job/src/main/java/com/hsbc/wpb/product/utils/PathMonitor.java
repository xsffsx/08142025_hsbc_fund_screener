package com.dummy.wpb.product.utils;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.io.monitor.FileAlterationMonitor;

import java.io.FileFilter;
import java.nio.file.Path;

public class PathMonitor {

	private FileAlterationMonitor monitor;

	private static final long DEFLT_INTVERL = 3000L;

	public PathMonitor(Path path, FileAlterationListener listener, FileFilter fileFilter) {
		this(DEFLT_INTVERL, path, listener, fileFilter);
	}

	public PathMonitor(long interval, Path path, FileAlterationListener listener, FileFilter fileFilter) {
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
