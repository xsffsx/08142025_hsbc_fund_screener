package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.bson.Document;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ProdNameSeqsLoader implements DataLoader {
    protected MongoCollection<Document> collection;
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProdNameSeqsLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate, MongoDatabase mongodb) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.collection = mongodb.getCollection(CollectionName.sys_parm);
    }


    @Override
    public void load() {
        log.info("Loading PROD_NAME_SEQS...");

        String sql = "select * from PROD_NAME_SEQS";

        List<Map<String, Object>> sequences = new LinkedList<>();
        namedParameterJdbcTemplate.query(sql, (RowCallbackHandler) rs ->
            sequences.add(CodeUtils.toCamelCase(DbUtils.getStringObjectMap(rs)))
        );

        Map<String, Integer> parmValueTexts = sequences.stream().collect(Collectors.toMap(
                seq -> ((LocalDateTime) seq.get("prodMatDt")).toLocalDate().toString(),
                seq -> MapUtils.getIntValue(seq, "seqNum"),
                Math::max
        ));

        collection.updateOne(
                Filters.eq("parmCde", "prodMturDt_seq"),
                Updates.combine(
                        Updates.set("parmValueTexts", parmValueTexts),
                        Updates.set(Field.recUpdtDtTm, new Date()),
                        Updates.set(Field.recCreatDtTm, new Date()),
                        Updates.setOnInsert(Field._id, UUID.randomUUID().toString())
                ),
                new UpdateOptions().upsert(true));
    }
}
