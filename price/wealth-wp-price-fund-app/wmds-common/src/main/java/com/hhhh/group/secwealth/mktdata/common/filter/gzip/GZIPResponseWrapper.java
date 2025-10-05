/*
 */
package com.hhhh.group.secwealth.mktdata.common.filter.gzip;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class GZIPResponseWrapper extends HttpServletResponseWrapper {

    private GZIPResponseStream gzipResponse;
    private ServletOutputStream servletOuput;
    private PrintWriter printerWriter;

    public GZIPResponseWrapper(final HttpServletResponse response) {
        super(response);
        response.addHeader("Content-Encoding", "gzip");
    }

    public void finish() throws IOException {
        if (this.printerWriter != null) {
            this.printerWriter.close();
        }
        if (this.servletOuput != null) {
            this.servletOuput.close();
        }
        if (this.gzipResponse != null) {
            this.gzipResponse.close();
        }
    }

    @Override
    public void flushBuffer() throws IOException {
        if (this.printerWriter != null) {
            this.printerWriter.flush();
        }
        if (this.servletOuput != null) {
            this.servletOuput.flush();
        }
        super.flushBuffer();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.servletOuput == null) {
            this.gzipResponse = new GZIPResponseStream(getResponse().getOutputStream());
            this.servletOuput = this.gzipResponse;
        }
        return this.servletOuput;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (this.printerWriter == null) {
            this.gzipResponse = new GZIPResponseStream(getResponse().getOutputStream());
            this.printerWriter = new PrintWriter(new OutputStreamWriter(this.gzipResponse, getResponse().getCharacterEncoding()));
        }
        return this.printerWriter;
    }

}