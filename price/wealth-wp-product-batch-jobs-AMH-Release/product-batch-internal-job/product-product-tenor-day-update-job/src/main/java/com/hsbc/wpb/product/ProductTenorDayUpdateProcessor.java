package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.utils.DateUtils;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ProductTenorDayUpdateProcessor implements ItemProcessor<Document, Document> {
    @Override
    public Document process(Document product) throws Exception {
        Long newTermRemainDayCnt = calculateTenorDays(product);
        Long oldTermRemainDayCnt = product.getLong(Field.termRemainDayCnt);

        if (Objects.equals(newTermRemainDayCnt, oldTermRemainDayCnt)) {
            return null;
        }

        return product.append(Field.termRemainDayCnt, newTermRemainDayCnt);
    }

    /**
     * Calculate the latest tenor day of a product
     *
     * @param product
     * @return
     */
    private Long calculateTenorDays(Document product) {
        LocalDate currentDate = LocalDate.now();
        Long termRemainDayCnt;
        if (Objects.isNull(product.getDate(Field.prodMturDt))) {
            // Case 1) If maturity date == null, i.e. evergreen product, Tenor in Days = "blank"
            termRemainDayCnt = null;
        } else {
            LocalDate prodMturDt = DateUtils.toLocalDate(product.getDate(Field.prodMturDt));
            if (currentDate.isAfter(prodMturDt)) {
                // Case 2) If Maturity Date < current date, Tenor in Days = 0
                termRemainDayCnt = 0L;
            } else if (Objects.isNull(product.getDate(Field.prodLnchDt))) {
                // Case 3) If launch date == null, Tenor in Days = Maturity Date - current date
                termRemainDayCnt = countDays(currentDate, prodMturDt);
            } else {
                LocalDate prodLnchDt = DateUtils.toLocalDate(product.getDate(Field.prodLnchDt));
                if (currentDate.isAfter(prodLnchDt)) {
                    // Case 4) If current date >= Launch Date, Tenor in Days = Maturity Date - current date
                    termRemainDayCnt = countDays(currentDate, prodMturDt);
                } else {
                    // Case 5) If current date < Launch Date, Tenors in Days = Maturity Date - Launch Date
                    termRemainDayCnt = countDays(prodLnchDt, prodMturDt);
                }
            }
        }
        return termRemainDayCnt;
    }


    /**
     * Count the days bewteen startDate and endDate
     *
     * @param startDate
     * @param endDate
     * @return
     */
    private Long countDays(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}
