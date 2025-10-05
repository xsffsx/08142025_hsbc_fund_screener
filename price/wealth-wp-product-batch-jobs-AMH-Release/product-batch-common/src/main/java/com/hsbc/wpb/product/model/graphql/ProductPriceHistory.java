package com.dummy.wpb.product.model.graphql;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Setter
@Getter
public class ProductPriceHistory {
    private Long prodId;
    private String pdcyPrcCde;
    private LocalDate prcEffDt;
    private LocalDate prcInpDt;
    private String ccyProdMktPrcCde;
    private BigDecimal prodBidPrcAmt;
    private BigDecimal prodOffrPrcAmt;
    private BigDecimal prodMktPrcAmt;
    private BigDecimal prodNavPrcAmt;
    private LocalDateTime recCreatDtTm;
    private LocalDateTime recUpdtDtTm;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductPriceHistory)) {
            return false;
        }
        ProductPriceHistory other = (ProductPriceHistory) o;
        return Objects.equals(this.getProdId(), other.getProdId())
                && Objects.equals(this.getPdcyPrcCde(), other.getPdcyPrcCde())
                && Objects.equals(this.getPrcEffDt(), other.getPrcEffDt())
                && Objects.equals(this.getPrcInpDt(), other.getPrcInpDt())
                && Objects.equals(this.getCcyProdMktPrcCde(), other.getCcyProdMktPrcCde())
                && Objects.equals(this.getProdBidPrcAmt(), other.getProdBidPrcAmt())
                && Objects.equals(this.getProdOffrPrcAmt(), other.getProdOffrPrcAmt())
                && Objects.equals(this.getProdMktPrcAmt(), other.getProdMktPrcAmt())
                && Objects.equals(this.getProdNavPrcAmt(), other.getProdNavPrcAmt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(prodId, pdcyPrcCde, prcEffDt, prcInpDt, ccyProdMktPrcCde, prodBidPrcAmt, prodOffrPrcAmt, prodMktPrcAmt, prodNavPrcAmt);
    }
}
