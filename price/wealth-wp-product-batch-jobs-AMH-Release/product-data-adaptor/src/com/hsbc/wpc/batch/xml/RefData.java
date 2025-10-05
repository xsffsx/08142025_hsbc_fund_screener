/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/RefData.java 1.3 2011/12/06 19:08:15CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class RefData.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:15CST $
 */
public class RefData implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ctryRecCde.
     */
    private java.lang.String _ctryRecCde;

    /**
     * Field _grpMembrRecCde.
     */
    private java.lang.String _grpMembrRecCde;

    /**
     * Field _cdvTypeCde.
     */
    private java.lang.String _cdvTypeCde;

    /**
     * Field _cdvCde.
     */
    private java.lang.String _cdvCde;

    /**
     * Field _cdvDesc.
     */
    private java.lang.String _cdvDesc;

    /**
     * Field _cdvPllDesc.
     */
    private java.lang.String _cdvPllDesc;

    /**
     * Field _cdvSllDesc.
     */
    private java.lang.String _cdvSllDesc;

    /**
     * Field _cdvDispSeqNum.
     */
    private java.lang.String _cdvDispSeqNum;

    /**
     * Field _cdvParntTypeCde.
     */
    private java.lang.String _cdvParntTypeCde;

    /**
     * Field _cdvParntCde.
     */
    private java.lang.String _cdvParntCde;

    /**
     * Field _recDtTmSeg.
     */
    private com.dummy.wpc.batch.xml.RecDtTmSeg _recDtTmSeg;


      //----------------/
     //- Constructors -/
    //----------------/

    public RefData() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'cdvCde'.
     * 
     * @return the value of field 'CdvCde'.
     */
    public java.lang.String getCdvCde(
    ) {
        return this._cdvCde;
    }

    /**
     * Returns the value of field 'cdvDesc'.
     * 
     * @return the value of field 'CdvDesc'.
     */
    public java.lang.String getCdvDesc(
    ) {
        return this._cdvDesc;
    }

    /**
     * Returns the value of field 'cdvDispSeqNum'.
     * 
     * @return the value of field 'CdvDispSeqNum'.
     */
    public java.lang.String getCdvDispSeqNum(
    ) {
        return this._cdvDispSeqNum;
    }

    /**
     * Returns the value of field 'cdvParntCde'.
     * 
     * @return the value of field 'CdvParntCde'.
     */
    public java.lang.String getCdvParntCde(
    ) {
        return this._cdvParntCde;
    }

    /**
     * Returns the value of field 'cdvParntTypeCde'.
     * 
     * @return the value of field 'CdvParntTypeCde'.
     */
    public java.lang.String getCdvParntTypeCde(
    ) {
        return this._cdvParntTypeCde;
    }

    /**
     * Returns the value of field 'cdvPllDesc'.
     * 
     * @return the value of field 'CdvPllDesc'.
     */
    public java.lang.String getCdvPllDesc(
    ) {
        return this._cdvPllDesc;
    }

    /**
     * Returns the value of field 'cdvSllDesc'.
     * 
     * @return the value of field 'CdvSllDesc'.
     */
    public java.lang.String getCdvSllDesc(
    ) {
        return this._cdvSllDesc;
    }

    /**
     * Returns the value of field 'cdvTypeCde'.
     * 
     * @return the value of field 'CdvTypeCde'.
     */
    public java.lang.String getCdvTypeCde(
    ) {
        return this._cdvTypeCde;
    }

    /**
     * Returns the value of field 'ctryRecCde'.
     * 
     * @return the value of field 'CtryRecCde'.
     */
    public java.lang.String getCtryRecCde(
    ) {
        return this._ctryRecCde;
    }

    /**
     * Returns the value of field 'grpMembrRecCde'.
     * 
     * @return the value of field 'GrpMembrRecCde'.
     */
    public java.lang.String getGrpMembrRecCde(
    ) {
        return this._grpMembrRecCde;
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
     * Sets the value of field 'cdvCde'.
     * 
     * @param cdvCde the value of field 'cdvCde'.
     */
    public void setCdvCde(
            final java.lang.String cdvCde) {
        this._cdvCde = cdvCde;
    }

    /**
     * Sets the value of field 'cdvDesc'.
     * 
     * @param cdvDesc the value of field 'cdvDesc'.
     */
    public void setCdvDesc(
            final java.lang.String cdvDesc) {
        this._cdvDesc = cdvDesc;
    }

    /**
     * Sets the value of field 'cdvDispSeqNum'.
     * 
     * @param cdvDispSeqNum the value of field 'cdvDispSeqNum'.
     */
    public void setCdvDispSeqNum(
            final java.lang.String cdvDispSeqNum) {
        this._cdvDispSeqNum = cdvDispSeqNum;
    }

    /**
     * Sets the value of field 'cdvParntCde'.
     * 
     * @param cdvParntCde the value of field 'cdvParntCde'.
     */
    public void setCdvParntCde(
            final java.lang.String cdvParntCde) {
        this._cdvParntCde = cdvParntCde;
    }

    /**
     * Sets the value of field 'cdvParntTypeCde'.
     * 
     * @param cdvParntTypeCde the value of field 'cdvParntTypeCde'.
     */
    public void setCdvParntTypeCde(
            final java.lang.String cdvParntTypeCde) {
        this._cdvParntTypeCde = cdvParntTypeCde;
    }

    /**
     * Sets the value of field 'cdvPllDesc'.
     * 
     * @param cdvPllDesc the value of field 'cdvPllDesc'.
     */
    public void setCdvPllDesc(
            final java.lang.String cdvPllDesc) {
        this._cdvPllDesc = cdvPllDesc;
    }

    /**
     * Sets the value of field 'cdvSllDesc'.
     * 
     * @param cdvSllDesc the value of field 'cdvSllDesc'.
     */
    public void setCdvSllDesc(
            final java.lang.String cdvSllDesc) {
        this._cdvSllDesc = cdvSllDesc;
    }

    /**
     * Sets the value of field 'cdvTypeCde'.
     * 
     * @param cdvTypeCde the value of field 'cdvTypeCde'.
     */
    public void setCdvTypeCde(
            final java.lang.String cdvTypeCde) {
        this._cdvTypeCde = cdvTypeCde;
    }

    /**
     * Sets the value of field 'ctryRecCde'.
     * 
     * @param ctryRecCde the value of field 'ctryRecCde'.
     */
    public void setCtryRecCde(
            final java.lang.String ctryRecCde) {
        this._ctryRecCde = ctryRecCde;
    }

    /**
     * Sets the value of field 'grpMembrRecCde'.
     * 
     * @param grpMembrRecCde the value of field 'grpMembrRecCde'.
     */
    public void setGrpMembrRecCde(
            final java.lang.String grpMembrRecCde) {
        this._grpMembrRecCde = grpMembrRecCde;
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.RefData
     */
    public static com.dummy.wpc.batch.xml.RefData unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.RefData) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.RefData.class, reader);
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
