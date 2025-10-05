/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/SidProductPriceSegment.java 1.1.1.2 2012/11/22 17:19:01CST 43701020 Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class SidProductPriceSegment.
 * 
 * @version $Revision: 1.1.1.2 $ $Date: 2012/11/22 17:19:01CST $
 */
public class SidProductPriceSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prcEarlyRdmEffDt.
     */
    private java.lang.String _prcEarlyRdmEffDt;

    /**
     * Field _prcEarlyRdmPct.
     */
    private java.lang.String _prcEarlyRdmPct;

    /**
     * Field _recDtTmSeg.
     */
    private com.dummy.wpc.batch.xml.RecDtTmSeg _recDtTmSeg;


      //----------------/
     //- Constructors -/
    //----------------/

    public SidProductPriceSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'prcEarlyRdmEffDt'.
     * 
     * @return the value of field 'PrcEarlyRdmEffDt'.
     */
    public java.lang.String getPrcEarlyRdmEffDt(
    ) {
        return this._prcEarlyRdmEffDt;
    }

    /**
     * Returns the value of field 'prcEarlyRdmPct'.
     * 
     * @return the value of field 'PrcEarlyRdmPct'.
     */
    public java.lang.String getPrcEarlyRdmPct(
    ) {
        return this._prcEarlyRdmPct;
    }

    /**
     * Returns the value of field 'recDtTmSeg'.
     * 
     * @return the value of field 'RecDtTmSeg'.
     */
    public com.dummy.wpc.batch.xml.RecDtTmSeg getRecDtTmSeg(
    ) {
        return this._recDtTmSeg;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'prcEarlyRdmEffDt'.
     * 
     * @param prcEarlyRdmEffDt the value of field 'prcEarlyRdmEffDt'
     */
    public void setPrcEarlyRdmEffDt(
            final java.lang.String prcEarlyRdmEffDt) {
        this._prcEarlyRdmEffDt = prcEarlyRdmEffDt;
    }

    /**
     * Sets the value of field 'prcEarlyRdmPct'.
     * 
     * @param prcEarlyRdmPct the value of field 'prcEarlyRdmPct'.
     */
    public void setPrcEarlyRdmPct(
            final java.lang.String prcEarlyRdmPct) {
        this._prcEarlyRdmPct = prcEarlyRdmPct;
    }

    /**
     * Sets the value of field 'recDtTmSeg'.
     * 
     * @param recDtTmSeg the value of field 'recDtTmSeg'.
     */
    public void setRecDtTmSeg(
            final com.dummy.wpc.batch.xml.RecDtTmSeg recDtTmSeg) {
        this._recDtTmSeg = recDtTmSeg;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * com.dummy.wpc.batch.xml.SidProductPriceSegment
     */
    public static com.dummy.wpc.batch.xml.SidProductPriceSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.SidProductPriceSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.SidProductPriceSegment.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
