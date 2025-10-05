/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/UtTrstInstmLst.java 1.3 2011/12/06 19:08:10CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class UtTrstInstmLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:10CST $
 */
public class UtTrstInstmLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _utTrstInstmList.
     */
    private java.util.Vector _utTrstInstmList;


      //----------------/
     //- Constructors -/
    //----------------/

    public UtTrstInstmLst() {
        super();
        this._utTrstInstmList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vUtTrstInstm
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addUtTrstInstm(
            final com.dummy.wpc.batch.xml.UtTrstInstm vUtTrstInstm)
    throws java.lang.IndexOutOfBoundsException {
        this._utTrstInstmList.addElement(vUtTrstInstm);
    }

    /**
     * 
     * 
     * @param index
     * @param vUtTrstInstm
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addUtTrstInstm(
            final int index,
            final com.dummy.wpc.batch.xml.UtTrstInstm vUtTrstInstm)
    throws java.lang.IndexOutOfBoundsException {
        this._utTrstInstmList.add(index, vUtTrstInstm);
    }

    /**
     * Method enumerateUtTrstInstm.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.UtTrstInstm elements
     */
    public java.util.Enumeration enumerateUtTrstInstm(
    ) {
        return this._utTrstInstmList.elements();
    }

    /**
     * Method getUtTrstInstm.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.UtTrstInstm
     * at the given index
     */
    public com.dummy.wpc.batch.xml.UtTrstInstm getUtTrstInstm(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._utTrstInstmList.size()) {
            throw new IndexOutOfBoundsException("getUtTrstInstm: Index value '" + index + "' not in range [0.." + (this._utTrstInstmList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.UtTrstInstm) _utTrstInstmList.get(index);
    }

    /**
     * Method getUtTrstInstm.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.UtTrstInstm[] getUtTrstInstm(
    ) {
        com.dummy.wpc.batch.xml.UtTrstInstm[] array = new com.dummy.wpc.batch.xml.UtTrstInstm[0];
        return (com.dummy.wpc.batch.xml.UtTrstInstm[]) this._utTrstInstmList.toArray(array);
    }

    /**
     * Method getUtTrstInstmCount.
     * 
     * @return the size of this collection
     */
    public int getUtTrstInstmCount(
    ) {
        return this._utTrstInstmList.size();
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
    public void removeAllUtTrstInstm(
    ) {
        this._utTrstInstmList.clear();
    }

    /**
     * Method removeUtTrstInstm.
     * 
     * @param vUtTrstInstm
     * @return true if the object was removed from the collection.
     */
    public boolean removeUtTrstInstm(
            final com.dummy.wpc.batch.xml.UtTrstInstm vUtTrstInstm) {
        boolean removed = _utTrstInstmList.remove(vUtTrstInstm);
        return removed;
    }

    /**
     * Method removeUtTrstInstmAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.UtTrstInstm removeUtTrstInstmAt(
            final int index) {
        java.lang.Object obj = this._utTrstInstmList.remove(index);
        return (com.dummy.wpc.batch.xml.UtTrstInstm) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vUtTrstInstm
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setUtTrstInstm(
            final int index,
            final com.dummy.wpc.batch.xml.UtTrstInstm vUtTrstInstm)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._utTrstInstmList.size()) {
            throw new IndexOutOfBoundsException("setUtTrstInstm: Index value '" + index + "' not in range [0.." + (this._utTrstInstmList.size() - 1) + "]");
        }
        
        this._utTrstInstmList.set(index, vUtTrstInstm);
    }

    /**
     * 
     * 
     * @param vUtTrstInstmArray
     */
    public void setUtTrstInstm(
            final com.dummy.wpc.batch.xml.UtTrstInstm[] vUtTrstInstmArray) {
        //-- copy array
        _utTrstInstmList.clear();
        
        for (int i = 0; i < vUtTrstInstmArray.length; i++) {
                this._utTrstInstmList.add(vUtTrstInstmArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.UtTrstInstmLst
     */
    public static com.dummy.wpc.batch.xml.UtTrstInstmLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.UtTrstInstmLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.UtTrstInstmLst.class, reader);
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
