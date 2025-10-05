package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.OperationInput;
import com.dummy.wmd.wpc.graphql.service.ConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProdCutoffTimeListener extends BaseChangeListener {

    private final Map<String, List<String>> interestedGroupMap;

    public ProdCutoffTimeListener(ConfigurationService configurationService) {
        this.interestedGroupMap = configurationService.getInterestedGroupFullMap();
    }

    @Override
    public void beforeUpdate(Document doc, List<OperationInput> operations) {
        if (!CollectionUtils.isEmpty(operations)) {
            updateProdCutoffTime(doc, operations);
        }
    }

    @Override
    public Collection<String> interestJsonPaths() {
        return allInterestJsonPaths;
    }

    @Override
    public void beforeInsert(Document doc) {
        List<Map<String, Object>> fieldGroupCtoffList = interestedGroupMap.values()
                .stream()
                .flatMap(Collection::stream)
                .distinct()
                .map(this::initFieldGroupCtoff)
                .collect(Collectors.toList());
        doc.put(Field.fieldGroupCtoff, fieldGroupCtoffList);
    }

    /**
     * Update product cutoff time
     *
     * @param doc        product data before updated
     * @param operations graphql operations
     * @implNote <a href="https://wpb-confluence.systems.uk.dummy/display/WWS/Product+Cut+Off+Time">Product Cut Off Time</a>
     */
    private void updateProdCutoffTime(Document doc, List<OperationInput> operations) {
        // get PW0 interested groups
        Set<String> interestedGroups = new HashSet<>();
        operations.forEach(op -> {
            String path = op.getPath();
            if (StringUtils.startsWithIgnoreCase(path, "$.")) {
                path = path.substring(2);
            }
            List<String> groups = interestedGroupMap.get(path);
            if (!CollectionUtils.isEmpty(groups)) {
                interestedGroups.addAll(groups);
            }
        });

        // update cutoff time for each group
        List<Map<String, Object>> fieldGroupCtoffList = (List<Map<String, Object>>) doc.compute(Field.fieldGroupCtoff,
                (k, list) -> ObjectUtils.defaultIfNull(list, new ArrayList<>()));

        interestedGroups.forEach(group -> {
            Map<String, Object> fieldGroupCtoff = fieldGroupCtoffList.stream()
                    .filter(item -> group.equals(item.get(Field.fieldGroupCde)))
                    .findFirst()
                    .orElse(null);

            if (null == fieldGroupCtoff) {
                fieldGroupCtoffList.add(initFieldGroupCtoff(group));
            } else {
                fieldGroupCtoff.put(Field.fieldGroupCtoffDtTm, new Date());
            }
        });
    }

    private Map<String, Object> initFieldGroupCtoff(String fieldGroupCde) {
        Map<String, Object> fieldGroupCtoff = new HashMap<>();
        fieldGroupCtoff.put(Field.rowid, UUID.randomUUID().toString());
        fieldGroupCtoff.put(Field.fieldGroupCde, fieldGroupCde);
        fieldGroupCtoff.put(Field.fieldGroupCtoffDtTm, new Date());
        return fieldGroupCtoff;
    }
}
