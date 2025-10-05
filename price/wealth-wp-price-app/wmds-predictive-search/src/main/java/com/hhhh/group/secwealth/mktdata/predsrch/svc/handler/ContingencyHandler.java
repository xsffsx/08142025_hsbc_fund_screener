/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.manager.PredictiveSearchManager;

/**
 * <p>
 * <b> Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Component("contingencyHandler")
public class ContingencyHandler {

    @Autowired
    @Qualifier("predictiveSearchManager")
    private PredictiveSearchManager predictiveSearchManager;

    @Value("#{systemConfig['predsrch.port']}")
    private int port = 8888;

    private ServerSocket serverSocket;

    private Socket socket;

    private BufferedReader in;

    private PrintWriter out;

    @Value("#{systemConfig['predsrch.EnableSocket']}")
    private boolean on_off = true;

    private String localhostName = "localhost";

    @PostConstruct
    public void init() throws Exception {
        LogUtil.debug(ContingencyHandler.class, "Socket port=" + this.port);
        if (ContingencyHandler.this.on_off) {
            this.serverSocket = new ServerSocket(this.port);
            Listener listener = new Listener();
            Thread t = new Thread(listener);
            try {
                t.start();
            } catch (Exception e) {
                LogUtil.error(ContingencyHandler.class, e.getMessage(), e);
                t.interrupt();
                stopThread();
            }
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        stopThread();
        if (null != this.serverSocket && !this.serverSocket.isClosed()) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                LogUtil.error(ContingencyHandler.class, "serverSocket close error", e);
            }
        }
    }

    /**
     * <p>
     * <b> Check the token and source host of socket request. </b>
     * </p>
     */
    public class Listener implements Runnable {
        public void run() {
            try {
                while (!ContingencyHandler.this.serverSocket.isClosed()) {
                    try {
                        ContingencyHandler.this.socket = ContingencyHandler.this.serverSocket.accept();
                        LogUtil.debug(ContingencyHandler.class,
                            "Client socket IP=" + ContingencyHandler.this.socket.getInetAddress().getHostAddress() + "|"
                                + ContingencyHandler.this.socket.getInetAddress().getHostName());
                        ContingencyHandler.this.in =
                            new BufferedReader(new InputStreamReader(ContingencyHandler.this.socket.getInputStream()));
                        ContingencyHandler.this.out = new PrintWriter(ContingencyHandler.this.socket.getOutputStream(), true);
                        if (!ContingencyHandler.this.localhostName
                            .equalsIgnoreCase(ContingencyHandler.this.socket.getInetAddress().getHostName())) {
                            ContingencyHandler.this.out.println("Failure|The request isn't from localhost");
                        } else {
                            ContingencyHandler.this.in =
                                new BufferedReader(new InputStreamReader(ContingencyHandler.this.socket.getInputStream()));
                            String line = null;
                            while ((line = ContingencyHandler.this.in.readLine()) != null) {
                                String[] input = line.split(CommonConstants.SYMBOL_SEPARATOR);
                                if (input.length > 0) {
                                    refreshData(input[0]);
                                } else {
                                    ContingencyHandler.this.out.println("Failure|The input parameter is wrong");
                                }
                            }
                        }
                    } catch (Exception e) {
                        ContingencyHandler.this.out.println("Failure|" + e.getMessage());
                        LogUtil.error(ContingencyHandler.class, "ContingencyHandler error: ", e);
                    } finally {
                        try {
                            if (null != ContingencyHandler.this.out) {
                                ContingencyHandler.this.out.close();
                            }
                            if (null != ContingencyHandler.this.in) {
                                ContingencyHandler.this.in.close();
                            }
                            if (null != null && !ContingencyHandler.this.socket.isClosed()) {
                                ContingencyHandler.this.socket.close();
                            }
                        } catch (Exception e) {
                            LogUtil.error(ContingencyHandler.class, "ContingencyHandler error: ", e);
                        }
                    }

                }
            } catch (Exception e) {
                LogUtil.error(ContingencyHandler.class, "ContingencyHandler error: ", e);
            } finally {
                if (!ContingencyHandler.this.serverSocket.isClosed()) {
                    try {
                        ContingencyHandler.this.serverSocket.close();
                    } catch (IOException e) {
                        LogUtil.error(ContingencyHandler.class, "ContingencyHandler error: ", e);
                    }
                }
            }
        }
    }

    public void refreshData(final String encryptToken) throws Exception {
        try {
            this.predictiveSearchManager.loadData();
            this.out.println("Done");
        } catch (Exception e) {
            this.out.println("Fail");
            if (e instanceof CommonException) {
                CommonException commonException = (CommonException) e;
                LogUtil.error(ContingencyHandler.class, "Failed to init search data:" + commonException.getMessage()
                    + ", error message: " + commonException.getErrMessage(), e);
            } else {
                LogUtil.error(ContingencyHandler.class, "Failed to init search data:" + e.getMessage(), e);
            }
        }
    }

    public synchronized void stopThread() {
        this.on_off = false;
    }

}