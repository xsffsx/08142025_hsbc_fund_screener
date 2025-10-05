package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import org.apache.commons.collections.MapUtils;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BondYieldHistoryListener extends BaseChangeListener {

    public static final String YIELD_TYPE_CODE_DAILY = "D";

    private static final List<String> interestFields = Stream.of(Field.yieldEffDt,
                    Field.yieldDt,
                    Field.yieldBidPct,
                    Field.yieldToCallBidPct,
                    Field.yieldToMturBidPct,
                    Field.yieldBidClosePct,
                    Field.yieldOfferPct,
                    Field.yieldToCallOfferPct,
                    Field.yieldToMturOfferText,
                    Field.yieldOfferClosePct)
            .map(field -> "debtInstm" + "." + field)
            .collect(Collectors.toList());

    @Override
    public void beforeValidation(Map<String, Object> oldProd, Map<String, Object> newProd) {
        Document oldDebtInstm = new Document((Map<String, Object>) MapUtils.getObject(oldProd, Field.debtInstm, Collections.emptyMap()));
        Document newDebtInstm = new Document((Map<String, Object>) MapUtils.getObject(newProd, Field.debtInstm, Collections.emptyMap()));

        if (MapUtils.isEmpty(newDebtInstm)) {
            return;
        }

        LocalDate yieldEffDt = DocumentUtils.getLocalDate(newDebtInstm, Field.yieldEffDt);
        Double yieldBidPct = newDebtInstm.getDouble(Field.yieldBidPct);
        Double yieldToCallBidPct = newDebtInstm.getDouble(Field.yieldToCallBidPct);
        Double yieldToMturBidPct = newDebtInstm.getDouble(Field.yieldToMturBidPct);
        Double yieldBidClosePct = newDebtInstm.getDouble(Field.yieldBidClosePct);
        Double yieldOfferPct = newDebtInstm.getDouble(Field.yieldOfferPct);
        Double yieldToCallOfferPct = newDebtInstm.getDouble(Field.yieldToCallOfferPct);
        String yieldToMturOfferText = newDebtInstm.getString(Field.yieldToMturOfferText);
        Double yieldOfferClosePct = newDebtInstm.getDouble(Field.yieldOfferClosePct);

        if (Objects.nonNull(yieldEffDt) &&
                Stream.of(yieldBidPct, yieldToCallBidPct, yieldToMturBidPct, yieldBidClosePct, yieldOfferPct,
                        yieldToCallOfferPct, yieldToMturOfferText, yieldOfferClosePct).anyMatch(Objects::nonNull)) {

            List<Document> yieldHistList = oldDebtInstm.get(Field.yieldHist, new ArrayList<>());

            newDebtInstm.putIfAbsent(Field.yieldDt, LocalDate.now());

            Document yieldHist = yieldHistList.stream()
                    .filter(item -> Objects.equals(DocumentUtils.getLocalDate(item, Field.yieldEffDt), yieldEffDt)
                            && Objects.equals(item.get(Field.yieldTypeCde), YIELD_TYPE_CODE_DAILY))
                    .findFirst()
                    .orElse(new Document());

            if (yieldHist.isEmpty()) {
                yieldHistList.add(yieldHist);
            }

            yieldHist.put(Field.yieldTypeCde, YIELD_TYPE_CODE_DAILY);
            copyDebtInstm(yieldHist, newDebtInstm);
            yieldHist.putIfAbsent(Field.rowid, UUID.randomUUID().toString());
            yieldHist.putIfAbsent(Field.recCreatDtTm, new Date());
            yieldHist.put(Field.recUpdtDtTm, new Date());
            newDebtInstm.put(Field.yieldHist, yieldHistList);
        } else {
            copyDebtInstm(newDebtInstm, oldDebtInstm);
        }
        newDebtInstm.putIfAbsent(Field.prodIssDt, oldDebtInstm.get(Field.prodIssDt));
        newProd.put(Field.debtInstm, newDebtInstm);
    }

    private void copyDebtInstm(Document target, Document source) {
        target.put(Field.yieldDt, source.get(Field.yieldDt));
        target.put(Field.yieldEffDt, source.get(Field.yieldEffDt));
        target.put(Field.yieldBidPct, source.get(Field.yieldBidPct));
        target.put(Field.yieldToCallBidPct, source.get(Field.yieldToCallBidPct));
        target.put(Field.yieldToMturBidPct, source.get(Field.yieldToMturBidPct));
        target.put(Field.yieldBidClosePct, source.get(Field.yieldBidClosePct));
        target.put(Field.yieldOfferPct, source.get(Field.yieldOfferPct));
        target.put(Field.yieldToCallOfferPct, source.get(Field.yieldToCallOfferPct));
        target.put(Field.yieldOfferClosePct, source.get(Field.yieldOfferClosePct));
        target.put(Field.yieldToMturOfferText, source.get(Field.yieldToMturOfferText));
    }

    @Override
    public Collection<String> interestJsonPaths() {
        return interestFields;
    }
}
