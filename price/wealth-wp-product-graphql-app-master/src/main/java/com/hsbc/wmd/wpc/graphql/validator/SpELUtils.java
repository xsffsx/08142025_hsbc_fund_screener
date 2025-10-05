package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.MongoDBService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

@SuppressWarnings("java:S1168")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpELUtils {
    /**
     * Sum up given attribute in a map list
     *
     * @param mapList
     * @param attrName
     * @return
     */
    public static double sum(List<Map<String, Object>> mapList, String attrName) {
        // double / float has to be changed into BigDecimal to calculate accurately, to avoid result in 100.00000000000001 which 100 is expected
        // see https://stackoverflow.com/questions/322749/retain-precision-with-double-in-java
        BigDecimal result = new BigDecimal(0);
        for (Map<String, Object> item : mapList) {
            Object attr = item.get(attrName);
            if (null != attr) {
                BigDecimal value = new BigDecimal(attr.toString());
                result = result.add(value);
            }
        }
        return result.doubleValue();
    }

    public static Map<String, Object> productById(Long prodId) {
        List<Map<String, Object>> list = MongoDBService.queryForMapList(CollectionName.product, eq(Field.prodId, prodId));
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Make sure return a LocalDate instance
     *
     * @param date
     * @return
     */
    public static LocalDate localDate(Object date) {
        if (date instanceof LocalDate) {
            return (LocalDate) date;
        } else if (date instanceof Date) {
            return ((Date) date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            throw new IllegalArgumentException("Expect Date or LocalDate as input type: " + date);
        }
    }

    public static boolean validatePrecisionScale(Object value, Integer targetPrecision, Integer targetScale) {
        if (value == null)
            return false;

        BigDecimal validateValue = new BigDecimal(value.toString()).stripTrailingZeros();
        return validateValue.scale() <= targetScale && validateValue.precision() <= targetPrecision;
    }

}



