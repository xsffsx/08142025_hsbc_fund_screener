/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/GnrcProdLst.java 1.3 2011/12/06 19:08:19CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class GnrcProdLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:19CST $
 */
public class GnrcProdLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _gnrcProdList.
     */
    private java.util.Vector _gnrcProdList;


      //----------------/
     //- Constructors -/
    //----------------/

    public GnrcProdLst() {
        super();
        this._gnrcProdList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vGnrcProd
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addGnrcProd(
            final com.dummy.wpc.batch.xml.GnrcProd vGnrcProd)
    throws java.lang.IndexOutOfBoundsException {
        this._gnrcProdList.addElement(vGnrcProd);
    }

    /**
     * 
     * 
     * @param index
     * @param vGnrcProd
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addGnrcProd(
            final int index,
            final com.dummy.wpc.batch.xml.GnrcProd vGnrcProd)
    throws java.lang.IndexOutOfBoundsException {
        this._gnrcProdList.add(index, vGnrcProd);
    }

    /**
     * Method enumerateGnrcProd.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.GnrcProd elements
     */
    public java.util.Enumeration enumerateGnrcProd(
    ) {
        return this._gnrcProdList.elements();
    }

    /**
     * Method getGnrcProd.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.GnrcProd at
     * the given index
     */
    public com.dummy.wpc.batch.xml.GnrcProd getGnrcProd(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._gnrcProdList.size()) {
            throw new IndexOutOfBoundsException("getGnrcProd: Index value '" + index + "' not in range [0.." + (this._gnrcProdList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.GnrcProd) _gnrcProdList.get(index);
    }

    /**
     * Method getGnrcProd.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.GnrcProd[] getGnrcProd(
    ) {
        com.dummy.wpc.batch.xml.GnrcProd[] array = new com.dummy.wpc.batch.xml.GnrcProd[0];
        return (com.dummy.wpc.batch.xml.GnrcProd[]) this._gnrcProdList.toArray(array);
    }

    /**
     * Method getGnrcProdCount.
     * 
     * @return the size of this collection
     */
    public int getGnrcProdCount(
    ) {
        return this._gnrcProdList.size();
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
    public void removeAllGnrcProd(
    ) {
        this._gnrcProdList.clear();
    }

    /**
     * Method removeGnrcProd.
     * 
     * @param vGnrcProd
     * @return true if the object was removed from the collection.
     */
    public boolean removeGnrcProd(
            final com.dummy.wpc.batch.xml.GnrcProd vGnrcProd) {
        boolean removed = _gnrcProdList.remove(vGnrcProd);
        return removed;
    }

    /**
     * Method removeGnrcProdAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.GnrcProd removeGnrcProdAt(
            final int index) {
        java.lang.Object obj = this._gnrcProdList.remove(index);
        return (com.dummy.wpc.batch.xml.GnrcProd) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vGnrcProd
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setGnrcProd(
            final int index,
            final com.dummy.wpc.batch.xml.GnrcProd vGnrcProd)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._gnrcProdList.size()) {
            throw new IndexOutOfBoundsException("setGnrcProd: Index value '" + index + "' not in range [0.." + (this._gnrcProdList.size() - 1) + "]");
        }
        
        this._gnrcProdList.set(index, vGnrcProd);
    }

    /**
     * 
     * 
     * @param vGnrcProdArray
     */
    public void setGnrcProd(
            final com.dummy.wpc.batch.xml.GnrcProd[] vGnrcProdArray) {
        //-- copy array
        _gnrcProdList.clear();
        
        for (int i = 0; i < vGnrcProdArray.length; i++) {
                this._gnrcProdList.add(vGnrcProdArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.GnrcProdLst
     */
    public static com.dummy.wpc.batch.xml.GnrcProdLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.GnrcProdLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.GnrcProdLst.class, reader);
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
