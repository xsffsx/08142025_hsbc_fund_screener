/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/RefDataLst.java 1.3 2011/12/06 19:08:05CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class RefDataLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:05CST $
 */
public class RefDataLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _refDataList.
     */
    private java.util.Vector _refDataList;


      //----------------/
     //- Constructors -/
    //----------------/

    public RefDataLst() {
        super();
        this._refDataList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vRefData
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addRefData(
            final com.dummy.wpc.batch.xml.RefData vRefData)
    throws java.lang.IndexOutOfBoundsException {
        this._refDataList.addElement(vRefData);
    }

    /**
     * 
     * 
     * @param index
     * @param vRefData
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addRefData(
            final int index,
            final com.dummy.wpc.batch.xml.RefData vRefData)
    throws java.lang.IndexOutOfBoundsException {
        this._refDataList.add(index, vRefData);
    }

    /**
     * Method enumerateRefData.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.RefData elements
     */
    public java.util.Enumeration enumerateRefData(
    ) {
        return this._refDataList.elements();
    }

    /**
     * Method getRefData.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.RefData at
     * the given index
     */
    public com.dummy.wpc.batch.xml.RefData getRefData(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._refDataList.size()) {
            throw new IndexOutOfBoundsException("getRefData: Index value '" + index + "' not in range [0.." + (this._refDataList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.RefData) _refDataList.get(index);
    }

    /**
     * Method getRefData.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.RefData[] getRefData(
    ) {
        com.dummy.wpc.batch.xml.RefData[] array = new com.dummy.wpc.batch.xml.RefData[0];
        return (com.dummy.wpc.batch.xml.RefData[]) this._refDataList.toArray(array);
    }

    /**
     * Method getRefDataCount.
     * 
     * @return the size of this collection
     */
    public int getRefDataCount(
    ) {
        return this._refDataList.size();
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
    public void removeAllRefData(
    ) {
        this._refDataList.clear();
    }

    /**
     * Method removeRefData.
     * 
     * @param vRefData
     * @return true if the object was removed from the collection.
     */
    public boolean removeRefData(
            final com.dummy.wpc.batch.xml.RefData vRefData) {
        boolean removed = _refDataList.remove(vRefData);
        return removed;
    }

    /**
     * Method removeRefDataAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.RefData removeRefDataAt(
            final int index) {
        java.lang.Object obj = this._refDataList.remove(index);
        return (com.dummy.wpc.batch.xml.RefData) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vRefData
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setRefData(
            final int index,
            final com.dummy.wpc.batch.xml.RefData vRefData)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._refDataList.size()) {
            throw new IndexOutOfBoundsException("setRefData: Index value '" + index + "' not in range [0.." + (this._refDataList.size() - 1) + "]");
        }
        
        this._refDataList.set(index, vRefData);
    }

    /**
     * 
     * 
     * @param vRefDataArray
     */
    public void setRefData(
            final com.dummy.wpc.batch.xml.RefData[] vRefDataArray) {
        //-- copy array
        _refDataList.clear();
        
        for (int i = 0; i < vRefDataArray.length; i++) {
                this._refDataList.add(vRefDataArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.RefDataLst
     */
    public static com.dummy.wpc.batch.xml.RefDataLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.RefDataLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.RefDataLst.class, reader);
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
