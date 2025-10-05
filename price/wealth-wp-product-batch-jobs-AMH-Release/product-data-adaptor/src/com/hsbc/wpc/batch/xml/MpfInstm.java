/*
 * This class was automatically generated with <a
 * href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML Schema. $Id:
 * src/com/dummy/wpc/batch/xml/MpfInstm.java 1.3.2.1 2012/11/23 20:31:41CST
 * 43701020 Development $
 */

package com.dummy.wpc.batch.xml;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class MpfInstm.
 * 
 * @version $Revision: 1.3.2.1 $ $Date: 2012/11/23 20:31:41CST $
 */
public class MpfInstm implements java.io.Serializable {


    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /**
     * Field _prodKeySeg.
     */
    private com.dummy.wpc.batch.xml.ProdKeySeg _prodKeySeg;

    /**
     * Field _mpfPerfmSegList.
     */
    private java.util.Vector _mpfPerfmSegList;


    // ----------------/
    // - Constructors -/
    // ----------------/

    public MpfInstm() {
        super();
        this._mpfPerfmSegList = new java.util.Vector();
    }


    // -----------/
    // - Methods -/
    // -----------/

    /**
     * 
     * 
     * @param vMpfInstmSeg
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index given is outside the bounds of the collection
     */
    public void addMpfPerfmSeg(final com.dummy.wpc.batch.xml.MpfPerfmSeg vMpfPerfmSeg) throws java.lang.IndexOutOfBoundsException {
        this._mpfPerfmSegList.addElement(vMpfPerfmSeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vMpfInstmSeg
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index given is outside the bounds of the collection
     */
    public void addMpfPerfmSeg(final int index, final com.dummy.wpc.batch.xml.MpfPerfmSeg vMpfPerfmSeg)
        throws java.lang.IndexOutOfBoundsException {
        this._mpfPerfmSegList.add(index, vMpfPerfmSeg);
    }

    /**
     * Method enumerateMpfPerfmSeg.
     * 
     * @return an Enumeration over all com.dummy.wpc.batch.xml.MpfInstmSeg
     *         elements
     */
    public java.util.Enumeration enumerateMpfPerfmSeg() {
        return this._mpfPerfmSegList.elements();
    }


    /**
     * Returns the value of field 'prodKeySeg'.
     * 
     * @return the value of field 'ProdKeySeg'.
     */
    public com.dummy.wpc.batch.xml.ProdKeySeg getProdKeySeg() {
        return this._prodKeySeg;
    }

    /**
     * Method getMpfPerfmSeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.MpfPerfmSeg at the given
     *         index
     */
    public com.dummy.wpc.batch.xml.MpfPerfmSeg getMpfPerfmSeg(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._mpfPerfmSegList.size()) {
            throw new IndexOutOfBoundsException("getMpfPerfmSeg: Index value '" + index + "' not in range [0.."
                + (this._mpfPerfmSegList.size() - 1) + "]");
        }

        return (com.dummy.wpc.batch.xml.MpfPerfmSeg) this._mpfPerfmSegList.get(index);
    }

    /**
     * Method getMpfPerfmSeg.Returns the contents of the collection in an
     * Array.
     * <p>
     * Note: Just in case the collection contents are changing in another
     * thread, we pass a 0-length Array of the correct type into the API call.
     * This way we <i>know</i> that the Array returned is of exactly the
     * correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.MpfPerfmSeg[] getMpfPerfmSeg() {
        com.dummy.wpc.batch.xml.MpfPerfmSeg[] array = new com.dummy.wpc.batch.xml.MpfPerfmSeg[0];
        return (com.dummy.wpc.batch.xml.MpfPerfmSeg[]) this._mpfPerfmSegList.toArray(array);
    }

    /**
     * Method getMpfInstmSegCount.
     * 
     * @return the size of this collection
     */
    public int getMpfPerfmSegCount() {
        return this._mpfPerfmSegList.size();
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
    public void removeAllMpfPerfmSeg() {
        this._mpfPerfmSegList.clear();
    }

    /**
     * Method removeMpfPerfmSeg.
     * 
     * @param vMpfPerfmSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeMpfPerfmSeg(final com.dummy.wpc.batch.xml.MpfPerfmSeg vMpfPerfmSeg) {
        boolean removed = this._mpfPerfmSegList.remove(vMpfPerfmSeg);
        return removed;
    }

    /**
     * Method removeMpfPerfmSegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.MpfPerfmSeg removeMpfPerfmSegAt(final int index) {
        java.lang.Object obj = this._mpfPerfmSegList.remove(index);
        return (com.dummy.wpc.batch.xml.MpfPerfmSeg) obj;
    }


    /**
     * Sets the value of field 'prodKeySeg'.
     * 
     * @param prodKeySeg
     *            the value of field 'prodKeySeg'.
     */
    public void setProdKeySeg(final com.dummy.wpc.batch.xml.ProdKeySeg prodKeySeg) {
        this._prodKeySeg = prodKeySeg;
    }

    /**
     * 
     * 
     * @param index
     * @param vMpfPerfmSeg
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index given is outside the bounds of the collection
     */
    public void setMpfPerfmSeg(final int index, final com.dummy.wpc.batch.xml.MpfPerfmSeg vMpfPerfmSeg)
        throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._mpfPerfmSegList.size()) {
            throw new IndexOutOfBoundsException("setMpfPerfmSeg: Index value '" + index + "' not in range [0.."
                + (this._mpfPerfmSegList.size() - 1) + "]");
        }

        this._mpfPerfmSegList.set(index, vMpfPerfmSeg);
    }

    /**
     * 
     * 
     * @param vMpfPerfmSegArray
     */
    public void setMpfPerfmSeg(final com.dummy.wpc.batch.xml.MpfPerfmSeg[] vMpfPerfmSegArray) {
        // -- copy array
        this._mpfPerfmSegList.clear();

        for (int i = 0; i < vMpfPerfmSegArray.length; i++) {
            this._mpfPerfmSegList.add(vMpfPerfmSegArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.MpfInstm
     */
    public static com.dummy.wpc.batch.xml.MpfInstm unmarshal(final java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.MpfInstm) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.MpfInstm.class, reader);
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
