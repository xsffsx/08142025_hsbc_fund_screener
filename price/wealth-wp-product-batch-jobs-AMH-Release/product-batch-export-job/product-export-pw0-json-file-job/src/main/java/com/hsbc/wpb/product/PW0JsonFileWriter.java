package com.dummy.wpb.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.model.RowData;
import com.dummy.wpb.product.model.TableObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


@Slf4j
public class PW0JsonFileWriter extends AbstractItemStreamItemWriter<List<TableObject>> {
    private final String ctryRecCde;
    private final String grpMembrRecCde;
    private final String outputPath;
    private final String prodTypCde;
    private final String targetName;
    private Map<String, Long> lastRowNumMap = new ConcurrentHashMap<>();
    private Map<String, TableObject> writeObjectMap = new ConcurrentHashMap<>();

    private static final ObjectMapper mapper = new ObjectMapper();

    public PW0JsonFileWriter(String ctryRecCde, String grpMembrRecCde, String prodTypCde, String targetName, String outputPath) {
        setName("PW0JsonFileWriter");
        this.ctryRecCde = ctryRecCde;
        this.grpMembrRecCde = grpMembrRecCde;
        this.prodTypCde = prodTypCde;
        this.targetName = targetName;
        this.outputPath = outputPath;
    }

    @Override
    public void write(List<? extends List<TableObject>> items) {
        List<TableObject> tableObjectList = items.stream().flatMap(Collection::stream).collect(Collectors.toList());

        for (TableObject tableObject : tableObjectList) {
            lastRowNumMap.compute(tableObject.getTableName(), (tableName, rowNum) -> {
                rowNum = (Long) ObjectUtils.defaultIfNull(rowNum, 0L);

                for (RowData rowData : tableObject.getRowData()) {
                    rowData.setRowNum(++rowNum);
                }

                TableObject allTableObject = writeObjectMap.computeIfAbsent(tableName, v -> new TableObject(tableObject.getColumnDefinition(),  new CopyOnWriteArrayList<>(), tableName));
                allTableObject.getRowData().addAll(tableObject.getRowData());
                return rowNum;
            });
        }
    }


    @Override
    public void close() {
        if (CollectionUtils.isEmpty(writeObjectMap.values())) return;
        lastRowNumMap.forEach((tableName, romNum) -> log.info("{}: {}", tableName, romNum));
        Iterator<TableObject> iterator = writeObjectMap.values().iterator();
        String fileNameFormat = "%s_%s_%s_%s_%s.json";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(BatchConstants.FILENAME_DATETIME_PATTERN));
        String fileName = String.format(fileNameFormat, ctryRecCde, grpMembrRecCde, prodTypCde, targetName, timestamp);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + fileName))) {
            writer.write("[");
            while (iterator.hasNext()) {
                Object item = iterator.next();
                writer.write(mapper.writeValueAsString(item));
                if (iterator.hasNext()) {
                    writer.write(",");
                }
            }
            writer.write("]");
        } catch (IOException e) {
            log.error("export PW0 file fail");
        }

        writeObjectMap.clear();

    }


}
