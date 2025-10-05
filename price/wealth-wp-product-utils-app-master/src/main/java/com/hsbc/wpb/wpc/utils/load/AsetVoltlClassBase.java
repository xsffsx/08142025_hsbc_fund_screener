package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CodeUtils;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.Field;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class AsetVoltlClassBase {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AsetVoltlClassBase(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Load all records and group by entity
     *
     * @return
     */
    protected Map<Map<String, Object>, List<Document>> loadAndGroupDocuments(String table) {
        String sql = String.format("select ROWID, t.* from %s t", table);
        Map<Map<String, Object>, List<Document>> map = new LinkedHashMap<>();
        namedParameterJdbcTemplate.query(sql, rs -> {
            Map<String, Object> row = DbUtils.getStringObjectMap(rs);
            row = CodeUtils.toCamelCase(row);

            Map<String, Object> key = new LinkedHashMap<>();
            key.put(Field.ctryRecCde, row.get(Field.ctryRecCde));
            key.put(Field.grpMembrRecCde, row.get(Field.grpMembrRecCde));

            List<Document> list = map.getOrDefault(key, new ArrayList<>());
            list.add(new Document(row));
            map.put(key, list);
        });
        return map;
    }

    protected Document toDocument(List<Document> list) {
        Document first = list.get(0);
        Document doc = new Document();
        doc.put(Field.ctryRecCde, first.get(Field.ctryRecCde));
        doc.put(Field.grpMembrRecCde, first.get(Field.grpMembrRecCde));
        doc.put(Field.recCreatDtTm, first.get(Field.recCreatDtTm));
        doc.put(Field.recUpdtDtTm, first.get(Field.recUpdtDtTm));
        doc.put(Field.revision, 1L);
        doc.put(Field.createdBy, "load");
        list.forEach(document -> {
            document.remove(Field.ctryRecCde);
            document.remove(Field.grpMembrRecCde);
            document.remove(Field.recCreatDtTm);
            document.remove(Field.recUpdtDtTm);
        });
        doc.put(Field.list, list);
        return doc;
    }
}
