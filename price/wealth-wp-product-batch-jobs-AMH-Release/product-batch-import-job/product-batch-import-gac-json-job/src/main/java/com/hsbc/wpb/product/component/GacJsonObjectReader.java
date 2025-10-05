package com.dummy.wpb.product.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.json.JsonObjectReader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GacJsonObjectReader implements JsonObjectReader<Map<String,Object>> {
    ObjectMapper mapper = new ObjectMapper();
    private JsonParser jsonParser;
    private InputStream inputStream;

    @Override
    public void open(Resource resource) throws Exception {
        this.inputStream = resource.getInputStream();
        this.jsonParser = this.mapper.getFactory().createParser(this.inputStream);

        for (JsonToken token = jsonParser.nextToken(); token != null; token = jsonParser.nextToken()) {
            if (StringUtils.equals(jsonParser.currentName(), "SECURITY") && token == JsonToken.START_ARRAY) {
                break;
            }
        }

        Assert.state(jsonParser.nextToken() == JsonToken.START_OBJECT,
                String.format("Please make sure the %s has SECURITY node and the value is an array type", resource.getFilename()));
    }

    @Override
    public Map<String, Object> read() {
        Map<String, Object> map = null;

        try {
            map = mapper.readValue(jsonParser, new TypeReference<HashMap<String, Object>>() {});
        } catch (IOException e) {
            log.error("Cannot read this record: " + e.getMessage());
        }

        return map;
    }

    @Override
    public void close() throws Exception {
        this.inputStream.close();
        this.jsonParser.close();
    }
}
