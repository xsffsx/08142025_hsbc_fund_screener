package com.dummy.wpc.datadaptor.writer;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;

import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ExecutionContextHelper;

public class CSVDefaultWriter extends FlatFileItemWriter {
	
	private TrailerLineGenerator trailerLineGen;
	private HeaderLineGenerator headerLineGen;
	
	private LineAggregator lineAggregator = new CSVLineAggregator();
	
	public void setLineAggregator(LineAggregator lineAggregator) {
		this.lineAggregator = lineAggregator;
		super.setLineAggregator(lineAggregator);
	}
	
	private int totalLine = 0;
	private Map configMap;
	
	public TrailerLineGenerator getTrailerLineGen() {
		return trailerLineGen;
	}



	public void setTrailerLineGen(TrailerLineGenerator trailerLineGen) {
		this.trailerLineGen = trailerLineGen;
	}



	public HeaderLineGenerator getHeaderLineGen() {
		return headerLineGen;
	}



	public void setHeaderLineGen(HeaderLineGenerator headerLineGen) {
		this.headerLineGen = headerLineGen;
	}

	private void writeLines(String[] lines) throws Exception {
		if(lines != null){
			CSVLineAggregator csvLA = null;
			if(lineAggregator instanceof CSVLineAggregator){
				csvLA = (CSVLineAggregator) lineAggregator;
				csvLA.setHandleTextGualifier(false);
			}
			for(String line : lines){
				super.write(line);
			}
			if(csvLA != null){
				csvLA.setHandleTextGualifier(true);
			}
		}
		super.flush();
	}


	public void open(ExecutionContext executionContext) throws ItemStreamException {
		this.configMap = ExecutionContextHelper.copyProperties(executionContext);
		super.open(executionContext);
		try {
			writeLines(genHeaderLines());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close(ExecutionContext executionContext) {
		try {
			writeLines(genTrailerLines());
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.close(executionContext);
	}
	
	private String[] genHeaderLines() {
		if(headerLineGen != null){
			return headerLineGen.gen(this.configMap);
		}
		else{
			String headerLineGenStr = (String)this.configMap.get(Constants.HEADER_LINE_GENERATOR);
			if(!StringUtils.isEmpty(headerLineGenStr)){
				headerLineGen = (HeaderLineGenerator)constructInstance(headerLineGenStr);
				if(headerLineGen != null){
					return headerLineGen.gen(this.configMap);
				}
			}
		}
		return null;
	}
	
	private String[] genTrailerLines() {
		if(trailerLineGen != null){
			return trailerLineGen.gen(this.configMap,totalLine);
		}
		else{
			String trailerLineGenStr = (String)this.configMap.get(Constants.TRAILER_LINE_GENERATOR);
			if(!StringUtils.isEmpty(trailerLineGenStr)){
				trailerLineGen = (TrailerLineGenerator)constructInstance(trailerLineGenStr);
				if(trailerLineGen != null){
					return trailerLineGen.gen(this.configMap,this.totalLine);
				}
			}
		}
		return null;
	}
	
	private Object constructInstance(String headerLineGenStr) {
		try {
			Class clz = Class.forName(headerLineGenStr);
			if(clz != null){
				return clz.newInstance();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void write(Object data) throws Exception {
		super.write(data);
		totalLine ++;
	}
}
