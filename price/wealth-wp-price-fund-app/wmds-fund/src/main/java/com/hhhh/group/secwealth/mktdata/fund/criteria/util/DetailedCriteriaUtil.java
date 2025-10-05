
package com.hhhh.group.secwealth.mktdata.fund.criteria.util;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.criteria.Constants;


@Component("detailedCriteriaUtil")
public class DetailedCriteriaUtil extends CriteriaUtil {

    public final static String LOACTYPE_CRITERIA_MAPKEY = "LT";

    protected StringEscapeUtil stringEscapeUtil = new StringEscapeUtil();

    public boolean existsKey(final List<SearchCriteria> items, final String cirteriaKey) {
        String cirteriaItemKey = null;
        for (SearchCriteria detailCriteriaitem : items) {
            cirteriaItemKey = detailCriteriaitem.getCriteriaKey();
            if ((!StringUtils.isBlank(cirteriaItemKey)) && cirteriaItemKey.equals(cirteriaKey)) {
                return true;
            }
        }

        return false;
    }

    public String toString(final List<SearchCriteria> items, final String tableAlias, final String loadtypeSql,
        final String loadtypeColAlias, final String countryCode, final String groupMember) {

        SearchCriteria nextDealDate = new SearchCriteria();

        StringBuilder criteriaBuilder = new StringBuilder();
        String criteriaItemKey = null;
        String criteriaValue = null;
        for (SearchCriteria detailCriteriaitem : items) {
            LogUtil.debug(DetailedCriteriaUtil.class, "detailCriteriaitem are:-{}" + detailCriteriaitem);
            criteriaItemKey = detailCriteriaitem.getCriteriaKey();
            if (criteriaItemKey.equals(Constants.NEXT_DEAL_DATE)) {
                nextDealDate = detailCriteriaitem;
                continue;
            }
            criteriaValue = detailCriteriaitem.getCriteriaValue();
            String mappedCriteriaKey = this.getMappedCriteriaKey(criteriaItemKey, countryCode, groupMember);
            String mappedCriteriaValue = this.getMappedCriteriaValue(criteriaItemKey, criteriaValue);
            if (criteriaBuilder.length() > 0) {
                criteriaBuilder.append(" and ");
            }
            if (DetailedCriteriaUtil.LOACTYPE_CRITERIA_MAPKEY.equals(criteriaItemKey)) { // specical
                                                                                         // hanlding
                                                                                         // for
                                                                                         // loadType
                String criteriaSQL = this.prepareLTSql(detailCriteriaitem, loadtypeColAlias);
                criteriaBuilder.append(" (").append(loadtypeSql.replaceFirst("\\)", " and ")).append(criteriaSQL).append(")) ");
                continue;
            }
            if (detailCriteriaitem.getOperator().equals("in")) {

                // Handle query with criteria "in (null)";
                if (criteriaValue != null && StringUtil.isInvalid(criteriaValue)) {
                    criteriaBuilder.append(this.prefixTableAlias(tableAlias, mappedCriteriaKey)).append(" is NULL ");
                    continue;
                }

                StringBuilder inClauseCriteriaBuilder = new StringBuilder();
                inClauseCriteriaBuilder.append(this.prefixTableAlias(tableAlias, mappedCriteriaKey)).append(" ")
                    .append((Operator.valueOf(detailCriteriaitem.getOperator().toUpperCase()).getText())).append(" ").append("(");

                Object[] criteriaValues = criteriaValue.split(":", -1);
                // Check if there exist null values in the in clause
                boolean isNullInClause = false;
                for (Object item : criteriaValues) {
                    if (item != null && StringUtil.isInvalid(item.toString())) {
                        isNullInClause = true;
                    } else {
                        inClauseCriteriaBuilder
                            .append(Constants.APOSTROPHE)
                            .append(
                                this.getMappedCriteriaValue(detailCriteriaitem.getCriteriaKey(),
                                    item == null ? null : item.toString())).append(Constants.APOSTROPHE)
                            .append(Constants.CRITERIA_SEPARATOR);
                    }
                }


                inClauseCriteriaBuilder.replace(inClauseCriteriaBuilder.lastIndexOf(Constants.CRITERIA_SEPARATOR),
                    inClauseCriteriaBuilder.length(), ")");

                criteriaBuilder.append(!isNullInClause ? inClauseCriteriaBuilder : new StringBuilder("(")
                    .append(inClauseCriteriaBuilder).append(" or ").append(this.prefixTableAlias(tableAlias, mappedCriteriaKey))
                    .append(" is NULL )"));
            } else if (detailCriteriaitem.getOperator().equals("ni") || detailCriteriaitem.getOperator().equals("ne")) {

                // Handle query with criteria "in (null)";
                if (criteriaValue != null && StringUtil.isInvalid(criteriaValue)) {
                    criteriaBuilder.append(this.prefixTableAlias(tableAlias, mappedCriteriaKey)).append(" is NULL ");
                    continue;
                }

                StringBuilder inClauseCriteriaBuilder = new StringBuilder();
                inClauseCriteriaBuilder.append("NVL(").append(this.prefixTableAlias(tableAlias, mappedCriteriaKey))
                    .append(", ' ')").append(" ")
                    .append((Operator.valueOf(detailCriteriaitem.getOperator().toUpperCase()).getText())).append(" ").append("(");

                Object[] criteriaValues = criteriaValue.split(":", -1);
                // Check if there exist null values in the in clause
                boolean isNullInClause = false;
                for (Object item : criteriaValues) {
                    if (item != null && StringUtil.isInvalid(item.toString())) {
                        isNullInClause = true;
                    } else {
                        inClauseCriteriaBuilder
                            .append(Constants.APOSTROPHE)
                            .append(
                                this.getMappedCriteriaValue(detailCriteriaitem.getCriteriaKey(),
                                    item == null ? null : item.toString())).append(Constants.APOSTROPHE)
                            .append(Constants.CRITERIA_SEPARATOR);
                    }
                }


                inClauseCriteriaBuilder.replace(inClauseCriteriaBuilder.lastIndexOf(Constants.CRITERIA_SEPARATOR),
                    inClauseCriteriaBuilder.length(), ")");

                criteriaBuilder.append(!isNullInClause ? inClauseCriteriaBuilder : new StringBuilder("(")
                    .append(inClauseCriteriaBuilder).append(" or ").append(this.prefixTableAlias(tableAlias, mappedCriteriaKey))
                    .append(" is NULL )"));
            } else if (detailCriteriaitem.getOperator().equals("eq") && this.criteriaKeyMapper != null
                && this.criteriaKeyMapper.getTextTypeCriteriaItems().contains(detailCriteriaitem.getCriteriaKey())) {
                criteriaBuilder.append(this.prefixTableAlias(tableAlias, mappedCriteriaKey))
                    .append(Operator.valueOf(detailCriteriaitem.getOperator().toUpperCase()).getText())
                    .append(Constants.APOSTROPHE).append(mappedCriteriaValue).append(Constants.APOSTROPHE);
            } else {
                if (StringUtils.isBlank(mappedCriteriaValue) && "EQ".equalsIgnoreCase(detailCriteriaitem.getOperator())) {
                    criteriaBuilder.append(this.prefixTableAlias(tableAlias, mappedCriteriaKey)).append(" is NULL ");
                } else {
                    criteriaBuilder.append(this.prefixTableAlias(tableAlias, mappedCriteriaKey))
                        .append(Operator.valueOf(detailCriteriaitem.getOperator().toUpperCase()).getText())
                        .append(Constants.APOSTROPHE).append(mappedCriteriaValue).append(Constants.APOSTROPHE);
                }
            }
        }

        if (StringUtil.isValid(nextDealDate.getCriteriaKey())) {
            if (items.size() > 1) {
                criteriaBuilder.append(" and ");
            }
            String mappedCriteriaKey = this.getMappedCriteriaKey(nextDealDate.getCriteriaKey(), countryCode, groupMember);
            String mappedCriteriaValue = this
                .getMappedCriteriaValue(nextDealDate.getCriteriaKey(), nextDealDate.getCriteriaValue());
            String[] criteriaValues = mappedCriteriaValue.split(":", -1);
            criteriaBuilder.append(this.prefixTableAlias(tableAlias, mappedCriteriaKey)).append(" ")
                .append(Operator.valueOf(nextDealDate.getOperator().toUpperCase()).getText()).append(" (");
            for (String value : criteriaValues) {
                criteriaBuilder.append("to_date('").append(value).append("','yyyy-MM-dd'),");
            }
            criteriaBuilder.deleteCharAt(criteriaBuilder.length() - 1).append(")");
        }

        String generatedCriteria = criteriaBuilder.toString();
        LogUtil.debug(DetailedCriteriaUtil.class, "Final genereaetd detailed criteria is:{}" + generatedCriteria);

        return this.stringEscapeUtil.unescapeString(generatedCriteria);
    }

    private String prepareLTSql(final SearchCriteria detailCriteriaitem, final String loadtypeColAlias) {
        String criteriaKey = detailCriteriaitem.getCriteriaKey();
        String criteriaValue = detailCriteriaitem.getCriteriaValue();

        StringBuilder criteriaBuilder = new StringBuilder();
        String criteriaOperator = detailCriteriaitem.getOperator();
        String mapCriteriaValue = null;
        String itemValue = null;
        String criterSQL = null;
        String operatorValue = Operator.valueOf(criteriaOperator.toUpperCase()).getText();
        if ("in".equals(criteriaOperator) || "ni".equals(criteriaOperator) || "ne".equals(criteriaOperator)) {
            // Handle query with criteria "in (null)";
            if (criteriaValue != null && StringUtils.isBlank(criteriaValue)) {
                criteriaBuilder.append(loadtypeColAlias).append(" is NULL and ");
            } else {
                StringTokenizer strStringTokenizer = new StringTokenizer(criteriaValue, ":");

                // Check if there exist null values in the in clause
                boolean isNullInClause = false;
                StringBuffer sb = new StringBuffer();
                sb.append("(");
                while (strStringTokenizer.hasMoreElements()) {
                    Object inValue = strStringTokenizer.nextElement();
                    // handle query with criteria "in (null,...)"
                    if (inValue != null && StringUtils.isBlank(inValue.toString())) {
                        isNullInClause = true;
                    } else if (inValue != null) {
                        itemValue = inValue.toString();
                        if (sb.length() > 1) {
                            sb.append(",");
                        }
                        mapCriteriaValue = this.getMappedCriteriaValue(criteriaKey, itemValue);
                        sb.append(Constants.APOSTROPHE).append(mapCriteriaValue).append(Constants.APOSTROPHE);
                    }
                }
                sb.append(")");
                if (isNullInClause) {
                    criterSQL = this.genStr("({} {} {} OR {} IS NULL)", loadtypeColAlias, operatorValue, sb.toString(),
                        loadtypeColAlias);
                } else {
                    criterSQL = this.genStr("({} {} {})", loadtypeColAlias, operatorValue, sb.toString());
                }
                criteriaBuilder.append(criterSQL);
            }
        } else {// handle eq|gt|ge|lt|le
            mapCriteriaValue = this.getMappedCriteriaValue(criteriaKey, criteriaValue);
            if (!StringUtils.isBlank(mapCriteriaValue)) {
                mapCriteriaValue = (Constants.APOSTROPHE + mapCriteriaValue + Constants.APOSTROPHE);
                criteriaBuilder.append(this.genStr("({} {} {})", loadtypeColAlias, operatorValue, mapCriteriaValue));
            } else {
                if ("eq".equals(criteriaOperator)) {
                    criteriaBuilder.append(this.genStr("({} IS NULL)", loadtypeColAlias));
                }
            }
        }
        return criteriaBuilder.toString();
    }
}
