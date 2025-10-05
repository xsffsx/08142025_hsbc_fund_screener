package com.dummy.wpb.wpc.utils.service;

import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("java:S115")
public class AdminLogService {
    private static final String admin_log = CollectionName.admin_log;

    @Autowired
    private MongoDatabase mongodb;

    public void log(HttpServletRequest request) {
        Document doc = new Document();
        doc.put("requestUri", request.getRequestURI());
        doc.put("requestTime", new Date());
        doc.put("httpMethod", request.getMethod());
        Map<String, List<String>> headersMap = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(request.getHeaders(h))
                ));
        doc.put("httpHeaders", headersMap);
        mongodb.getCollection(admin_log).insertOne(doc);
    }
}
