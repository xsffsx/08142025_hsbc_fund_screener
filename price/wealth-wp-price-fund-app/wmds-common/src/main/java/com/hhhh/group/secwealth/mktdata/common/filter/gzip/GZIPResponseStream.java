/*
 */
package com.hhhh.group.secwealth.mktdata.common.filter.gzip;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class GZIPResponseStream extends ServletOutputStream {

    private GZIPOutputStream gzipOutput;
    private final AtomicBoolean open = new AtomicBoolean(true);

    public GZIPResponseStream(final OutputStream output) throws IOException {
        this.gzipOutput = new GZIPOutputStream(output);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(final WriteListener arg0) {

    }

    @Override
    public void flush() throws IOException {
        this.gzipOutput.flush();
    }

    @Override
    public void close() throws IOException {
        if (this.open.compareAndSet(true, false)) {
            this.gzipOutput.close();
        }
    }

    @Override
    public void write(final int b) throws IOException {

    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (!this.open.get()) {
            throw new IOException("closed");
        }
        this.gzipOutput.write(b, off, len);
    }

    @Override
    public void write(final byte[] b) throws IOException {
        write(b, 0, b.length);
    }

}