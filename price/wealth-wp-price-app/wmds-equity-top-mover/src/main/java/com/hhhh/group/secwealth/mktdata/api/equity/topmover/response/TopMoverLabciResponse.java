/**
 * @Title TopMoverLabciResponse.java
 * @description TODO
 * @author OJim
 * @time Jun 28, 2019 5:23:24 PM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.topmover.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
/**
 * @Title TopMoverLabciResponse.java
 * @description TODO
 * @author OJim
 * @time Jun 28, 2019 5:23:24 PM
 */
@Getter
@Setter
public class TopMoverLabciResponse implements Serializable {

    private static final long serialVersionUID = 1817215332757514880L;

    private List<TopMoverLabciTable> topMovers;
}
