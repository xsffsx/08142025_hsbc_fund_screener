
package com.hhhh.group.secwealth.mktdata.fund.webservice.handler;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

//@Component("httpHeaderHandler" )
public class HttpHeaderHandler implements HandlerResolver {

    private boolean inbound;

    public boolean isInbound() {
        return this.inbound;
    }

    public void setInbound(final boolean inbound) {
        this.inbound = inbound;
    }

    private List<Handler> handlerList = null;

    public HttpHeaderHandler() {
        this.handlerList = new ArrayList<Handler>();
        this.handlerList.add(new HttpHeaderSOAPHandler(this.inbound));
    }

    public List<Handler> getHandlerChain(final PortInfo paramPortInfo) {
        return this.handlerList;
    }
}
