package com.dummy.wmd.wpc.graphql.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorrelationKey {
    private String asetVoltlClassCde;
    private String asetVoltlClassRelCde;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CorrelationKey that = (CorrelationKey) o;
        return Objects.equal(asetVoltlClassCde, that.asetVoltlClassCde) && Objects.equal(asetVoltlClassRelCde, that.asetVoltlClassRelCde)
                || Objects.equal(asetVoltlClassCde, that.asetVoltlClassRelCde) && Objects.equal(asetVoltlClassRelCde, that.asetVoltlClassCde);
    }

    @Override
    public int hashCode() {
        if(asetVoltlClassCde.compareTo(asetVoltlClassRelCde) > 0) {
            return Objects.hashCode(asetVoltlClassRelCde, asetVoltlClassCde);
        } else {
            return Objects.hashCode(asetVoltlClassCde, asetVoltlClassRelCde);
        }
    }
}
