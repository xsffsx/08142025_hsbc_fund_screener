package com.dummy.wpb.wpc.utils.service;

import com.google.common.base.Equivalence;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InnerEquivalence extends Equivalence<Object> {
    @Override
    protected boolean doEquivalent(Object a, Object b) {
        if(a instanceof Date && b instanceof LocalDateTime) {
            Date date1 = (Date)a;
            Date date2  = Date.from(((LocalDateTime)b).atZone(ZoneId.systemDefault()).toInstant());
            return date1.equals(date2);
        } else if(a instanceof Map && b instanceof Map) {
            return compareMap((Map<String, Object>)a, (Map<String, Object>)b);
        } else if(a instanceof List && b instanceof List) {
            return compareList((List<Object>) a, (List<Object>) b);
        }
        return Objects.equals(a, b);
    }

    private boolean compareMap(Map<String, Object> a, Map<String, Object> b) {
        MapDifference<String, Object> diff = Maps.difference(a, b, new InnerEquivalence());
        return diff.areEqual();
    }

    private boolean compareList(List<Object> a, List<Object> b) {
        if(a.isEmpty() && b.isEmpty()) {
            return true;
        } else if(a.size() != b.size()) {
            return false;
        } else if(!(a.get(0) instanceof Map || b.get(0) instanceof Map)) {
            return a.equals(b);
        } else {
            InnerEquivalence equivalence = new InnerEquivalence(); // TO_DO: need to build a key map before compare, otherwise the sequence will be a matter
            for(int i=0; i<a.size(); i++) {
                MapDifference<String, Object> diff = Maps.difference((Map<String, Object>)a.get(i), (Map<String, Object>)b.get(i), equivalence);
                if(!diff.areEqual()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected int doHash(Object o) {
        return o.hashCode();
    }
}
