/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/SidLst.java 1.3 2011/12/06 19:07:57CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class SidLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:07:57CST $
 */
public class SidLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _sidList.
     */
    private java.util.Vector _sidList;


      //----------------/
     //- Constructors -/
    //----------------/

    public SidLst() {
        super();
        this._sidList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vSid
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSid(
            final com.dummy.wpc.batch.xml.Sid vSid)
    throws java.lang.IndexOutOfBoundsException {
        this._sidList.addElement(vSid);
    }

    /**
     * 
     * 
     * @param index
     * @param vSid
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSid(
            final int index,
            final com.dummy.wpc.batch.xml.Sid vSid)
    throws java.lang.IndexOutOfBoundsException {
        this._sidList.add(index, vSid);
    }

    /**
     * Method enumerateSid.
     * 
     * @return an Enumeration over all com.dummy.wpc.batch.xml.Sid
     * elements
     */
    public java.util.Enumeration enumerateSid(
    ) {
        return this._sidList.elements();
    }

    /**
     * Method getSid.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.Sid at the
     * given index
     */
    public com.dummy.wpc.batch.xml.Sid getSid(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._sidList.size()) {
            throw new IndexOutOfBoundsException("getSid: Index value '" + index + "' not in range [0.." + (this._sidList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.Sid) _sidList.get(index);
    }

    /**
     * Method getSid.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.Sid[] getSid(
    ) {
        com.dummy.wpc.batch.xml.Sid[] array = new com.dummy.wpc.batch.xml.Sid[0];
        return (com.dummy.wpc.batch.xml.Sid[]) this._sidList.toArray(array);
    }

    /**
     * Method getSidCount.
     * 
     * @return the size of this collection
     */
    public int getSidCount(
    ) {
        return this._sidList.size();
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
     */
    public void removeAllSid(
    ) {
        this._sidList.clear();
    }

    /**
     * Method removeSid.
     * 
     * @param vSid
     * @return true if the object was removed from the collection.
     */
    public boolean removeSid(
            final com.dummy.wpc.batch.xml.Sid vSid) {
        boolean removed = _sidList.remove(vSid);
        return removed;
    }

    /**
     * Method removeSidAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.Sid removeSidAt(
            final int index) {
        java.lang.Object obj = this._sidList.remove(index);
        return (com.dummy.wpc.batch.xml.Sid) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vSid
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setSid(
            final int index,
            final com.dummy.wpc.batch.xml.Sid vSid)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._sidList.size()) {
            throw new IndexOutOfBoundsException("setSid: Index value '" + index + "' not in range [0.." + (this._sidList.size() - 1) + "]");
        }
        
        this._sidList.set(index, vSid);
    }

    /**
     * 
     * 
     * @param vSidArray
     */
    public void setSid(
            final com.dummy.wpc.batch.xml.Sid[] vSidArray) {
        //-- copy array
        _sidList.clear();
        
        for (int i = 0; i < vSidArray.length; i++) {
                this._sidList.add(vSidArray[i]);
        }
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.dummy.wpc.batch.xml.SidLst
     */
    public static com.dummy.wpc.batch.xml.SidLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.SidLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.SidLst.class, reader);
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
