/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/SidRtrnSegment.java 1.3 2011/12/06 19:07:57CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class SidRtrnSegment.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:07:57CST $
 */
public class SidRtrnSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _rtrnIntrmPaidDt.
     */
    private java.lang.String _rtrnIntrmPaidDt;

    /**
     * Field _rtrnIntrmPct.
     */
    private java.lang.String _rtrnIntrmPct;


      //----------------/
     //- Constructors -/
    //----------------/

    public SidRtrnSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'rtrnIntrmPaidDt'.
     * 
     * @return the value of field 'RtrnIntrmPaidDt'.
     */
    public java.lang.String getRtrnIntrmPaidDt(
    ) {
        return this._rtrnIntrmPaidDt;
    }

    /**
     * Returns the value of field 'rtrnIntrmPct'.
     * 
     * @return the value of field 'RtrnIntrmPct'.
     */
    public java.lang.String getRtrnIntrmPct(
    ) {
        return this._rtrnIntrmPct;
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
     * Sets the value of field 'rtrnIntrmPaidDt'.
     * 
     * @param rtrnIntrmPaidDt the value of field 'rtrnIntrmPaidDt'.
     */
    public void setRtrnIntrmPaidDt(
            final java.lang.String rtrnIntrmPaidDt) {
        this._rtrnIntrmPaidDt = rtrnIntrmPaidDt;
    }

    /**
     * Sets the value of field 'rtrnIntrmPct'.
     * 
     * @param rtrnIntrmPct the value of field 'rtrnIntrmPct'.
     */
    public void setRtrnIntrmPct(
            final java.lang.String rtrnIntrmPct) {
        this._rtrnIntrmPct = rtrnIntrmPct;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.dummy.wpc.batch.xml.SidRtrnSegment
     */
    public static com.dummy.wpc.batch.xml.SidRtrnSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.SidRtrnSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.SidRtrnSegment.class, reader);
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
