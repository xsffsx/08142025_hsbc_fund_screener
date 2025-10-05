/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.dummy.wpc.batch.extSP;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class TemplateType.
 * 
 * @version $Revision$ $Date$
 */
public class TemplateType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tplUID.
     */
    private java.lang.String _tplUID;

    /**
     * Field _tplType.
     */
    private com.dummy.wpc.batch.extSP.types.TplTypeEnumType _tplType;

    /**
     * Field _tplName.
     */
    private java.lang.String _tplName;

    /**
     * Field _underlyingCount.
     */
    private long _underlyingCount;

    /**
     * keeps track of state for field: _underlyingCount
     */
    private boolean _has_underlyingCount;

    /**
     * Field _savedBy.
     */
    private java.lang.String _savedBy;

    /**
     * Field _lastSave.
     */
    private java.util.Date _lastSave;

    /**
     * Field _version.
     */
    private java.lang.String _version;

    /**
     * Field _templateEnums.
     */
    private com.dummy.wpc.batch.extSP.TemplateEnums _templateEnums;

    /**
     * Field _templateSections.
     */
    private com.dummy.wpc.batch.extSP.TemplateSections _templateSections;


      //----------------/
     //- Constructors -/
    //----------------/

    public TemplateType() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteUnderlyingCount(
    ) {
        this._has_underlyingCount= false;
    }

    /**
     * Returns the value of field 'lastSave'.
     * 
     * @return the value of field 'LastSave'.
     */
    public java.util.Date getLastSave(
    ) {
        return this._lastSave;
    }

    /**
     * Returns the value of field 'savedBy'.
     * 
     * @return the value of field 'SavedBy'.
     */
    public java.lang.String getSavedBy(
    ) {
        return this._savedBy;
    }

    /**
     * Returns the value of field 'templateEnums'.
     * 
     * @return the value of field 'TemplateEnums'.
     */
    public com.dummy.wpc.batch.extSP.TemplateEnums getTemplateEnums(
    ) {
        return this._templateEnums;
    }

    /**
     * Returns the value of field 'templateSections'.
     * 
     * @return the value of field 'TemplateSections'.
     */
    public com.dummy.wpc.batch.extSP.TemplateSections getTemplateSections(
    ) {
        return this._templateSections;
    }

    /**
     * Returns the value of field 'tplName'.
     * 
     * @return the value of field 'TplName'.
     */
    public java.lang.String getTplName(
    ) {
        return this._tplName;
    }

    /**
     * Returns the value of field 'tplType'.
     * 
     * @return the value of field 'TplType'.
     */
    public com.dummy.wpc.batch.extSP.types.TplTypeEnumType getTplType(
    ) {
        return this._tplType;
    }

    /**
     * Returns the value of field 'tplUID'.
     * 
     * @return the value of field 'TplUID'.
     */
    public java.lang.String getTplUID(
    ) {
        return this._tplUID;
    }

    /**
     * Returns the value of field 'underlyingCount'.
     * 
     * @return the value of field 'UnderlyingCount'.
     */
    public long getUnderlyingCount(
    ) {
        return this._underlyingCount;
    }

    /**
     * Returns the value of field 'version'.
     * 
     * @return the value of field 'Version'.
     */
    public java.lang.String getVersion(
    ) {
        return this._version;
    }

    /**
     * Method hasUnderlyingCount.
     * 
     * @return true if at least one UnderlyingCount has been added
     */
    public boolean hasUnderlyingCount(
    ) {
        return this._has_underlyingCount;
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
     * Sets the value of field 'lastSave'.
     * 
     * @param lastSave the value of field 'lastSave'.
     */
    public void setLastSave(
            final java.util.Date lastSave) {
        this._lastSave = lastSave;
    }

    /**
     * Sets the value of field 'savedBy'.
     * 
     * @param savedBy the value of field 'savedBy'.
     */
    public void setSavedBy(
            final java.lang.String savedBy) {
        this._savedBy = savedBy;
    }

    /**
     * Sets the value of field 'templateEnums'.
     * 
     * @param templateEnums the value of field 'templateEnums'.
     */
    public void setTemplateEnums(
            final com.dummy.wpc.batch.extSP.TemplateEnums templateEnums) {
        this._templateEnums = templateEnums;
    }

    /**
     * Sets the value of field 'templateSections'.
     * 
     * @param templateSections the value of field 'templateSections'
     */
    public void setTemplateSections(
            final com.dummy.wpc.batch.extSP.TemplateSections templateSections) {
        this._templateSections = templateSections;
    }

    /**
     * Sets the value of field 'tplName'.
     * 
     * @param tplName the value of field 'tplName'.
     */
    public void setTplName(
            final java.lang.String tplName) {
        this._tplName = tplName;
    }

    /**
     * Sets the value of field 'tplType'.
     * 
     * @param tplType the value of field 'tplType'.
     */
    public void setTplType(
            final com.dummy.wpc.batch.extSP.types.TplTypeEnumType tplType) {
        this._tplType = tplType;
    }

    /**
     * Sets the value of field 'tplUID'.
     * 
     * @param tplUID the value of field 'tplUID'.
     */
    public void setTplUID(
            final java.lang.String tplUID) {
        this._tplUID = tplUID;
    }

    /**
     * Sets the value of field 'underlyingCount'.
     * 
     * @param underlyingCount the value of field 'underlyingCount'.
     */
    public void setUnderlyingCount(
            final long underlyingCount) {
        this._underlyingCount = underlyingCount;
        this._has_underlyingCount = true;
    }

    /**
     * Sets the value of field 'version'.
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(
            final java.lang.String version) {
        this._version = version;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.dummy.wpc.batch.extSP.TemplateType
     */
    public static com.dummy.wpc.batch.extSP.TemplateType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.extSP.TemplateType) Unmarshaller.unmarshal(com.dummy.wpc.batch.extSP.TemplateType.class, reader);
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
