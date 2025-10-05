package com.dummy.wpb.product.utils;

import lombok.Getter;

import java.util.concurrent.TimeoutException;

public class Worker extends Thread{

	private final Process process;
	@Getter
    private volatile Integer exit;
	WatchThread wt;

	public Worker(Process process) {
		this.process = process;
	}

	@Override
	public void run() {
		try {
			wt = new WatchThread(process);
			wt.start();
			exit = process.waitFor();		
			wt.setOver(true);
		} catch (InterruptedException ignore) {
			Thread.currentThread().interrupt();
		}
	}
			
	public WatchThread getWatchThread(){
		return this.wt;
	}
	
	public  Worker processInLimitTime(Worker work,int limitTime) throws TimeoutException, InterruptedException {

		work.start();
		try {
			work.join(limitTime);
			if (work.getExit() != null) {
				return work;
			} else {
				throw new TimeoutException();
			}
		}  finally {
			work.process.destroy();
		}

	}


}
