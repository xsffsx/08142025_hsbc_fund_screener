/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/AvcCorl.java 1.1 2012/12/27 18:14:12CST 43701020 Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class AvcCorl.
 * 
 * @version $Revision: 1.1 $ $Date: 2012/12/27 18:14:12CST $
 */
public class AvcCorl implements java.io.Serializable {


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
     * Field _asetVoltlClassCde.
     */
    private java.lang.String _asetVoltlClassCde;

    /**
     * Field _asetVoltlClassRelCde.
     */
    private java.lang.String _asetVoltlClassRelCde;

    /**
     * Field _asetVoltlClassCorlRate.
     */
    private java.lang.String _asetVoltlClassCorlRate;

    /**
     * Field _recDtTmSeg.
     */
    private com.dummy.wpc.batch.xml.RecDtTmSeg _recDtTmSeg;


      //----------------/
     //- Constructors -/
    //----------------/

    public AvcCorl() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'asetVoltlClassCde'.
     * 
     * @return the value of field 'AsetVoltlClassCde'.
     */
    public java.lang.String getAsetVoltlClassCde(
    ) {
        return this._asetVoltlClassCde;
    }

    /**
     * Returns the value of field 'asetVoltlClassCorlRate'.
     * 
     * @return the value of field 'AsetVoltlClassCorlRate'.
     */
    public java.lang.String getAsetVoltlClassCorlRate(
    ) {
        return this._asetVoltlClassCorlRate;
    }

    /**
     * Returns the value of field 'asetVoltlClassRelCde'.
     * 
     * @return the value of field 'AsetVoltlClassRelCde'.
     */
    public java.lang.String getAsetVoltlClassRelCde(
    ) {
        return this._asetVoltlClassRelCde;
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
     * Sets the value of field 'asetVoltlClassCde'.
     * 
     * @param asetVoltlClassCde the value of field
     * 'asetVoltlClassCde'.
     */
    public void setAsetVoltlClassCde(
            final java.lang.String asetVoltlClassCde) {
        this._asetVoltlClassCde = asetVoltlClassCde;
    }

    /**
     * Sets the value of field 'asetVoltlClassCorlRate'.
     * 
     * @param asetVoltlClassCorlRate the value of field
     * 'asetVoltlClassCorlRate'.
     */
    public void setAsetVoltlClassCorlRate(
            final java.lang.String asetVoltlClassCorlRate) {
        this._asetVoltlClassCorlRate = asetVoltlClassCorlRate;
    }

    /**
     * Sets the value of field 'asetVoltlClassRelCde'.
     * 
     * @param asetVoltlClassRelCde the value of field
     * 'asetVoltlClassRelCde'.
     */
    public void setAsetVoltlClassRelCde(
            final java.lang.String asetVoltlClassRelCde) {
        this._asetVoltlClassRelCde = asetVoltlClassRelCde;
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.AvcCorl
     */
    public static com.dummy.wpc.batch.xml.AvcCorl unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.AvcCorl) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.AvcCorl.class, reader);
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
