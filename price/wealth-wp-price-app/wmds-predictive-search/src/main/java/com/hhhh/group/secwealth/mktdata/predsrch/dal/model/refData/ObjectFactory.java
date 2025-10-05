/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.refData;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.RecDtTmSeg;

/**
 * This object contains factory methods for each Java content interface and
 * Java element interface generated in the com.hhhh.compass.domain.ut package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of
 * the Java representation for XML content. The Java representation of XML
 * content can consist of schema derived interfaces and classes representing
 * the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CtryRecCde_QNAME = new QName("", "ctryRecCde");
    private final static QName _GrpMembrRecCde_QNAME = new QName("", "grpMembrRecCde");
    private final static QName _RecUpdtDtTm_QNAME = new QName("", "recUpdtDtTm");
    private final static QName _RecOnlnUpdtDtTm_QNAME = new QName("", "recOnlnUpdtDtTm");
    private final static QName _ProdStatUpdtDtTm_QNAME = new QName("", "prodStatUpdtDtTm");
    private final static QName _TimeZone_QNAME = new QName("", "timeZone");

    private final static QName _CdvTypeCde_QNAME = new QName("", "cdvTypeCde");
    private final static QName _CdvCde_QNAME = new QName("", "cdvCde");

    @XmlElementDecl(namespace = "", name = "cdvCde")
    public JAXBElement<String> createCdvCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CdvCde_QNAME, String.class, null, value);
    }
    private final static QName _CdvDesc_QNAME = new QName("", "cdvDesc");

    @XmlElementDecl(namespace = "", name = "cdvDesc")
    public JAXBElement<String> createCdvDesc(final String value) {
        return new JAXBElement<String>(ObjectFactory._CdvDesc_QNAME, String.class, null, value);
    }
    private final static QName _CdvPllDesc_QNAME = new QName("", "cdvPllDesc");

    @XmlElementDecl(namespace = "", name = "cdvPllDesc")
    public JAXBElement<String> createCdvPllDesc(final String value) {
        return new JAXBElement<String>(ObjectFactory._CdvPllDesc_QNAME, String.class, null, value);
    }
    private final static QName _CdvSllDesc_QNAME = new QName("", "cdvSllDesc");

    @XmlElementDecl(namespace = "", name = "cdvSllDesc")
    public JAXBElement<String> createCdvSllDesc(final String value) {
        return new JAXBElement<String>(ObjectFactory._CdvSllDesc_QNAME, String.class, null, value);
    }
    private final static QName _CdvDispSeqNum_QNAME = new QName("", "cdvDispSeqNum");

    @XmlElementDecl(namespace = "", name = "cdvDispSeqNum")
    public JAXBElement<String> createCdvDispSeqNum(final String value) {
        return new JAXBElement<String>(ObjectFactory._CdvDispSeqNum_QNAME, String.class, null, value);
    }
    private final static QName _CdvParntTypeCde_QNAME = new QName("", "cdvParntTypeCde");

    @XmlElementDecl(namespace = "", name = "cdvParntTypeCde")
    public JAXBElement<String> createCdvParntTypeCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CdvParntTypeCde_QNAME, String.class, null, value);
    }
    private final static QName _CdvParntCde_QNAME = new QName("", "cdvParntCde");

    @XmlElementDecl(namespace = "", name = "cdvParntCde")
    public JAXBElement<String> createCdvParntCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CdvParntCde_QNAME, String.class, null, value);
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: refData
     * 
     */
    public ObjectFactory() {}

    /**
     * Create an instance of {@link RecDtTmSeg }
     * 
     */
    public RecDtTmSeg createRecDtTmSeg() {
        return new RecDtTmSeg();
    }

    /**
     * Create an instance of {@link RefDataLst }
     * 
     */
    public RefDataLst createRefDataLst() {
        return new RefDataLst();
    }

    /**
     * Create an instance of {@link RefData }
     * 
     */
    public RefData createRefData() {
        return new RefData();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ctryRecCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCtryRecCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CtryRecCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "cdvTypeCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCdvTypeCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CdvTypeCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "timeZone")
    public JAXBElement<String> createTimeZone(final String value) {
        return new JAXBElement<String>(ObjectFactory._TimeZone_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "grpMembrRecCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createGrpMembrRecCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._GrpMembrRecCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodStatUpdtDtTm")
    public JAXBElement<String> createProdStatUpdtDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdStatUpdtDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "recOnlnUpdtDtTm")
    public JAXBElement<String> createRecOnlnUpdtDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._RecOnlnUpdtDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "recUpdtDtTm")
    public JAXBElement<String> createRecUpdtDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._RecUpdtDtTm_QNAME, String.class, null, value);
    }

}
