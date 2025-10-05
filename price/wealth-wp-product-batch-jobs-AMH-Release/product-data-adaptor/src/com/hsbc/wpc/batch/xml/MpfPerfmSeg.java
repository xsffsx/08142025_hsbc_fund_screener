/*
 */
package com.dummy.wpc.batch.xml;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * <p>
 * <b> TODO : Insert description of the class's responsibility/role. </b>
 * </p>
 */
public class MpfPerfmSeg extends MandatoryProvidentFundPerformanceSegment implements java.io.Serializable {


    // ----------------/
    // - Constructors -/
    // ----------------/

    public MpfPerfmSeg() {
        super();
    }


    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
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
     * @throws org.exolab.castor.xml.MarshalException
     *             if object is null or if any SAXException is thrown during
     *             marshaling
     * @throws org.exolab.castor.xml.ValidationException
     *             if this object is an invalid instance according to the
     *             schema
     */
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException,
        org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException
     *             if an IOException occurs during marshaling
     * @throws org.exolab.castor.xml.ValidationException
     *             if this object is an invalid instance according to the
     *             schema
     * @throws org.exolab.castor.xml.MarshalException
     *             if object is null or if any SAXException is thrown during
     *             marshaling
     */
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException,
        org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException
     *             if object is null or if any SAXException is thrown during
     *             marshaling
     * @throws org.exolab.castor.xml.ValidationException
     *             if this object is an invalid instance according to the
     *             schema
     * @return the unmarshaled com.dummy.wpc.batch.xml.ProductPerformanceSegment
     */
    public static com.dummy.wpc.batch.xml.MandatoryProvidentFundPerformanceSegment unmarshal(final java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.MandatoryProvidentFundPerformanceSegment) Unmarshaller.unmarshal(
            com.dummy.wpc.batch.xml.MpfPerfmSeg.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException
     *             if this object is an invalid instance according to the
     *             schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
