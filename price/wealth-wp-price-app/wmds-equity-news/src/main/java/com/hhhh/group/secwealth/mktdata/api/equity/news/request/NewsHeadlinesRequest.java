/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.news.request;


import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.request.EquityCommonRequest;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Getter
@Setter
public class NewsHeadlinesRequest extends EquityCommonRequest {
    @NotEmpty(message = "{validator.notEmpty.trisNewsHeadlinesRequest.market.message}")
    private String market;

//    @NotEmpty(message = "{validator.notEmpty.trisNewsHeadlinesRequest.category.message}")
    private String category;

    private String productCodeIndicator;

    private List<String> symbol;

    private String headline;//CN market only

    private String content;//CN market only

    private Integer recordsPerPage = 10;

    private Integer pageId = 1;

    private boolean translate;

    private String keyword;//US market only

    private String location;//US market only

    private String startDate;//US market only

    private String endDate;//US market only

    private String exchange;//US market only

}



