/*
 * This class was automatically generated with <a
 * href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML Schema. $Id:
 * src/com/dummy/wpc/batch/xml/MpfInstmLst.java 1.3 2011/12/06 19:08:10CST
 * Navagate Developer 10 (WMDHKG0028) Development $
 */

package com.dummy.wpc.batch.xml;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class MpfInstmLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:10CST $
 */
public class MpfInstmLst implements java.io.Serializable {


    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /**
     * Field _mpfInstmList.
     */
    private java.util.Vector _mpfInstmList;


    // ----------------/
    // - Constructors -/
    // ----------------/

    public MpfInstmLst() {
        super();
        this._mpfInstmList = new java.util.Vector();
    }


    // -----------/
    // - Methods -/
    // -----------/

    /**
     * 
     * 
     * @param vMpfInstm
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index given is outside the bounds of the collection
     */
    public void addMpfInstm(final com.dummy.wpc.batch.xml.MpfInstm vMpfInstm) throws java.lang.IndexOutOfBoundsException {
        this._mpfInstmList.addElement(vMpfInstm);
    }

    /**
     * 
     * 
     * @param index
     * @param vMpfInstm
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index given is outside the bounds of the collection
     */
    public void addMpfInstm(final int index, final com.dummy.wpc.batch.xml.MpfInstm vMpfInstm)
        throws java.lang.IndexOutOfBoundsException {
        this._mpfInstmList.add(index, vMpfInstm);
    }

    /**
     * Method enumerateUtTrstInstm.
     * 
     * @return an Enumeration over all com.dummy.wpc.batch.xml.UtTrstInstm
     *         elements
     */
    public java.util.Enumeration enumerateUtTrstInstm() {
        return this._mpfInstmList.elements();
    }

    /**
     * Method getMpfInstm.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.MpfInstm at the given
     *         index
     */
    public com.dummy.wpc.batch.xml.MpfInstm getMpfInstm(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._mpfInstmList.size()) {
            throw new IndexOutOfBoundsException("getMpfInstm: Index value '" + index + "' not in range [0.."
                + (this._mpfInstmList.size() - 1) + "]");
        }

        return (com.dummy.wpc.batch.xml.MpfInstm) this._mpfInstmList.get(index);
    }

    /**
     * Method getMpfInstm.Returns the contents of the collection in an Array.
     * <p>
     * Note: Just in case the collection contents are changing in another
     * thread, we pass a 0-length Array of the correct type into the API call.
     * This way we <i>know</i> that the Array returned is of exactly the
     * correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.MpfInstm[] getMpfInstm() {
        com.dummy.wpc.batch.xml.MpfInstm[] array = new com.dummy.wpc.batch.xml.MpfInstm[0];
        return (com.dummy.wpc.batch.xml.MpfInstm[]) this._mpfInstmList.toArray(array);
    }

    /**
     * Method getMpfInstmCount.
     * 
     * @return the size of this collection
     */
    public int getMpfInstmCount() {
        return this._mpfInstmList.size();
    }

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
     */
    public void removeAllMpfInstm() {
        this._mpfInstmList.clear();
    }

    /**
     * Method removeMpfInstm.
     * 
     * @param vMpfInstm
     * @return true if the object was removed from the collection.
     */
    public boolean removeMpfInstm(final com.dummy.wpc.batch.xml.MpfInstm vMpfInstm) {
        boolean removed = this._mpfInstmList.remove(vMpfInstm);
        return removed;
    }

    /**
     * Method removeMpfInstmAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.MpfInstm removeMpfInstmAt(final int index) {
        java.lang.Object obj = this._mpfInstmList.remove(index);
        return (com.dummy.wpc.batch.xml.MpfInstm) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vMpfInstm
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index given is outside the bounds of the collection
     */
    public void setMpfInstm(final int index, final com.dummy.wpc.batch.xml.MpfInstm vMpfInstm)
        throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._mpfInstmList.size()) {
            throw new IndexOutOfBoundsException("setMpfInstm: Index value '" + index + "' not in range [0.."
                + (this._mpfInstmList.size() - 1) + "]");
        }

        this._mpfInstmList.set(index, vMpfInstm);
    }

    /**
     * 
     * 
     * @param vMpfInstmArray
     */
    public void setMpfInstm(final com.dummy.wpc.batch.xml.MpfInstm[] vMpfInstmArray) {
        // -- copy array
        this._mpfInstmList.clear();

        for (int i = 0; i < vMpfInstmArray.length; i++) {
            this._mpfInstmList.add(vMpfInstmArray[i]);
        }
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.MpfInstmLst
     */
    public static com.dummy.wpc.batch.xml.MpfInstmLst unmarshal(final java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.MpfInstmLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.MpfInstmLst.class, reader);
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
