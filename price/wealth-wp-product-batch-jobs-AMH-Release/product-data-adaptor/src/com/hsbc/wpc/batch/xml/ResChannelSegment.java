/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ResChannelSegment.java 1.3 2011/12/06 19:08:02CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ResChannelSegment.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:02CST $
 */
public class ResChannelSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _resChannelDtlList.
     */
    private java.util.Vector _resChannelDtlList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ResChannelSegment() {
        super();
        this._resChannelDtlList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vResChannelDtl
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addResChannelDtl(
            final com.dummy.wpc.batch.xml.ResChannelDtl vResChannelDtl)
    throws java.lang.IndexOutOfBoundsException {
        this._resChannelDtlList.addElement(vResChannelDtl);
    }

    /**
     * 
     * 
     * @param index
     * @param vResChannelDtl
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addResChannelDtl(
            final int index,
            final com.dummy.wpc.batch.xml.ResChannelDtl vResChannelDtl)
    throws java.lang.IndexOutOfBoundsException {
        this._resChannelDtlList.add(index, vResChannelDtl);
    }

    /**
     * Method enumerateResChannelDtl.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.ResChannelDtl elements
     */
    public java.util.Enumeration enumerateResChannelDtl(
    ) {
        return this._resChannelDtlList.elements();
    }

    /**
     * Method getResChannelDtl.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.xml.ResChannelDtl at the given index
     */
    public com.dummy.wpc.batch.xml.ResChannelDtl getResChannelDtl(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._resChannelDtlList.size()) {
            throw new IndexOutOfBoundsException("getResChannelDtl: Index value '" + index + "' not in range [0.." + (this._resChannelDtlList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.ResChannelDtl) _resChannelDtlList.get(index);
    }

    /**
     * Method getResChannelDtl.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.ResChannelDtl[] getResChannelDtl(
    ) {
        com.dummy.wpc.batch.xml.ResChannelDtl[] array = new com.dummy.wpc.batch.xml.ResChannelDtl[0];
        return (com.dummy.wpc.batch.xml.ResChannelDtl[]) this._resChannelDtlList.toArray(array);
    }

    /**
     * Method getResChannelDtlCount.
     * 
     * @return the size of this collection
     */
    public int getResChannelDtlCount(
    ) {
        return this._resChannelDtlList.size();
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
    public void removeAllResChannelDtl(
    ) {
        this._resChannelDtlList.clear();
    }

    /**
     * Method removeResChannelDtl.
     * 
     * @param vResChannelDtl
     * @return true if the object was removed from the collection.
     */
    public boolean removeResChannelDtl(
            final com.dummy.wpc.batch.xml.ResChannelDtl vResChannelDtl) {
        boolean removed = _resChannelDtlList.remove(vResChannelDtl);
        return removed;
    }

    /**
     * Method removeResChannelDtlAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.ResChannelDtl removeResChannelDtlAt(
            final int index) {
        java.lang.Object obj = this._resChannelDtlList.remove(index);
        return (com.dummy.wpc.batch.xml.ResChannelDtl) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vResChannelDtl
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setResChannelDtl(
            final int index,
            final com.dummy.wpc.batch.xml.ResChannelDtl vResChannelDtl)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._resChannelDtlList.size()) {
            throw new IndexOutOfBoundsException("setResChannelDtl: Index value '" + index + "' not in range [0.." + (this._resChannelDtlList.size() - 1) + "]");
        }
        
        this._resChannelDtlList.set(index, vResChannelDtl);
    }

    /**
     * 
     * 
     * @param vResChannelDtlArray
     */
    public void setResChannelDtl(
            final com.dummy.wpc.batch.xml.ResChannelDtl[] vResChannelDtlArray) {
        //-- copy array
        _resChannelDtlList.clear();
        
        for (int i = 0; i < vResChannelDtlArray.length; i++) {
                this._resChannelDtlList.add(vResChannelDtlArray[i]);
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
     * @return the unmarshaled
     * com.dummy.wpc.batch.xml.ResChannelSegment
     */
    public static com.dummy.wpc.batch.xml.ResChannelSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ResChannelSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ResChannelSegment.class, reader);
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
