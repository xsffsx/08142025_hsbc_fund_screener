package com.dummy.wpc.datadaptor.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.MarkFailedException;
import org.springframework.batch.item.ResetFailedException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.separator.LineReader;
import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;
import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class FlatFileLineReader implements LineReader, ItemReader {

	private static final Collection DEFAULT_COMMENTS = Collections.singleton("#");

	private static final String DEFAULT_ENCODING = "ISO-8859-1";

	private static final int READ_AHEAD_LIMIT = 100000;

	private final Resource resource;
	
	private final int lineLength;

	private final String encoding;

	private Collection comments = DEFAULT_COMMENTS;

	// Encapsulates the state of the reader.
	private State state = null;

	private RecordSeparatorPolicy recordSeparatorPolicy = new SimpleRecordSeparatorPolicy();

	public FlatFileLineReader(Resource resource, int lineLength) throws IOException {
		this(resource, lineLength, DEFAULT_ENCODING);
	}

	public FlatFileLineReader(Resource resource, int lineLength, String encoding) {
		Assert.notNull(resource, "'resource' cannot be null.");
		Assert.notNull(lineLength, "'lineLength' cannot be null.");
		Assert.notNull(encoding, "'encoding' cannot be null.");
		this.lineLength = lineLength;
		this.resource = resource;
		this.encoding = encoding;
	}

	/**
	 * Setter for the {@link RecordSeparatorPolicy}. Default value is a {@link DefaultRecordSeparatorPolicy}. Ideally
	 * should not be changed once a reader is in use, but it would not be fatal if it was.
	 * 
	 * @param recordSeparatorPolicy the new {@link RecordSeparatorPolicy}
	 */
	public void setRecordSeparatorPolicy(RecordSeparatorPolicy recordSeparatorPolicy) {
		/*
		 * The rest of the code accesses the policy in synchronized blocks, copying the reference before using it. So in
		 * principle it can be changed in flight - the results might not be what the user expected!
		 */
		this.recordSeparatorPolicy = recordSeparatorPolicy;
	}

	/**
	 * Setter for comment prefixes. Can be used to ignore header lines as well by using e.g. the first couple of column
	 * names as a prefix.
	 * 
	 * @param comments an array of comment line prefixes.
	 */
	public void setComments(String[] comments) {
		this.comments = new HashSet(Arrays.asList(comments));
	}

	/**
	 * Read the next line from the input resource, ignoring comments, and according to the {@link RecordSeparatorPolicy}.
	 * 
	 * @return a String.
	 * 
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	public synchronized Object read() {
		// Make a copy of the recordSeparatorPolicy reference, in case it is
		// changed during a read operation (unlikely, but you never know)...
		RecordSeparatorPolicy recordSeparatorPolicy = this.recordSeparatorPolicy;
		String line = readLine();
		String record = line;
		if (line != null) {
			while (line != null && !recordSeparatorPolicy.isEndOfRecord(record)) {
				record = recordSeparatorPolicy.preProcess(record) + (line = readLine());
			}
		}
		return recordSeparatorPolicy.postProcess(record);
	}

	/**
	 * @return the next non-comment line
	 */
	private String readLine() {
		return getState().readLine();
	}

	/**
	 * @return
	 */
	private State getState() {
		if (state == null) {
			open();
		}
		return state;
	}

	/**
	 * Creates internal state object.
	 */
	public synchronized void open() {
		state = new State();
		state.open();
	}

	/**
	 * Close the reader associated with this input source.
	 */
	public synchronized void close() {
		if (state == null) {
			return;
		}
		try {
			state.close();
		} finally {
			state = null;
		}
	}

	/**
	 * Getter for current line count (not the current number of lines returned).
	 * 
	 * @return the current line count.
	 */
	public int getPosition() {
		return getState().getCurrentLineCount();
	}

	/**
	 * Mark the state for return later with reset. Uses the read-ahead limit from an underlying {@link BufferedReader},
	 * which means that there is a limit to how much data can be recovered if the mark needs to be reset.<br/>
	 * 
	 * Mark is supported as long as this {@link ItemStream} is used in a single-threaded environment. The state backing
	 * the mark is a single counter, keeping track of the current position, so multiple threads cannot be accommodated.
	 * 
	 * @see #reset()
	 * 
	 * @throws MarkFailedException if the mark could not be set.
	 */
	public synchronized void mark() throws MarkFailedException {
		getState().mark();
	}

	/**
	 * Reset the reader to the last mark.
	 * 
	 * @see #mark()
	 * 
	 * @throws ResetFailedException if the reset is unsuccessful, e.g. if the read-ahead limit was breached.
	 */
	public synchronized void reset() throws ResetFailedException {
		getState().reset();
	}

	private boolean isComment(String line) {
		for (Iterator iter = comments.iterator(); iter.hasNext();) {
			String prefix = (String) iter.next();
			if (line.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	private class State {
		private BufferedReader reader;

		private int currentLineCount = 0;

		private int markedLineCount = -1;

		public String readLine() {
			String line = null;

			char[] charLine = new char[lineLength];
			try {
				if (this.reader.read(charLine) == -1) {
					return null;
				}
				line = String.valueOf(charLine);
				currentLineCount++;
				while (isComment(line)) {
					if (reader.read(charLine) == -1) {
						return null;
					}
					line = String.valueOf(charLine);
					currentLineCount++;
				}
			} catch (IOException e) {
				throw new UnexpectedInputException("Unable to read from resource '" + resource + "' at line "
				        + currentLineCount, e);
			}
			return line;
		}

		/**
		 * 
		 */
		public void open() {
			try {
				reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), encoding));
				mark();
			} catch (IOException e) {
				throw new ItemStreamException("Could not open resource", e);
			}
		}

		/**
		 * Close the reader and reset the counters.
		 */
		public void close() {

			if (reader == null) {
				return;
			}
			try {
				reader.close();
			} catch (IOException e) {
				throw new ItemStreamException("Could not close reader", e);
			} finally {
				currentLineCount = 0;
				markedLineCount = -1;
			}

		}

		/**
		 * @return the current line count
		 */
		public int getCurrentLineCount() {
			return currentLineCount;
		}

		/**
		 * Mark the underlying reader and set the line counters.
		 */
		public void mark() throws MarkFailedException {
			try {
				reader.mark(READ_AHEAD_LIMIT);
				markedLineCount = currentLineCount;
			} catch (IOException e) {
				throw new MarkFailedException("Could not mark reader", e);
			}
		}

		/**
		 * Reset the reader and line counters to the last marked position if possible.
		 */
		public void reset() throws ResetFailedException {

			if (markedLineCount < 0) {
				return;
			}
			try {
				this.reader.reset();
				currentLineCount = markedLineCount;
			} catch (IOException e) {
				throw new ResetFailedException("Could not reset reader", e);
			}

		}

	}

	
}
