package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prodAltNumSeg")
@DocumentObject("altId")
@Data
public class ProductAlternativeNumberSegment {
    private String prodCdeAltClassCde;

    private String prodAltNum;

    @XmlTransient
    private String prodTypeCde;

    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        try {
            Object unknownObject = FieldUtils.readField(parent, "prodKeySeg", true);
            if (unknownObject instanceof ProductKeySegment) {
                ProductKeySegment prodKeySeg = (ProductKeySegment) unknownObject;
                this.prodTypeCde = prodKeySeg.getProdTypeCde();
            }
        } catch (Exception e) {
            //do nothing
        }
    }
}
