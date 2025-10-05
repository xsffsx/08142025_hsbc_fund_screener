/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProdProdRelnLst.
 * 
 * @version $Revision$ $Date$
 */
public class ProdProdRelnLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodProdRelnList.
     */
    private java.util.Vector _prodProdRelnList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProdProdRelnLst() {
        super();
        this._prodProdRelnList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vProdProdReln
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdProdReln(
            final com.dummy.wpc.batch.xml.ProdProdReln vProdProdReln)
    throws java.lang.IndexOutOfBoundsException {
        this._prodProdRelnList.addElement(vProdProdReln);
    }

    /**
     * 
     * 
     * @param index
     * @param vProdProdReln
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdProdReln(
            final int index,
            final com.dummy.wpc.batch.xml.ProdProdReln vProdProdReln)
    throws java.lang.IndexOutOfBoundsException {
        this._prodProdRelnList.add(index, vProdProdReln);
    }

    /**
     * Method enumerateProdProdReln.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.ProdProdReln elements
     */
    public java.util.Enumeration enumerateProdProdReln(
    ) {
        return this._prodProdRelnList.elements();
    }

    /**
     * Method getProdProdReln.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.ProdProdReln
     * at the given index
     */
    public com.dummy.wpc.batch.xml.ProdProdReln getProdProdReln(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodProdRelnList.size()) {
            throw new IndexOutOfBoundsException("getProdProdReln: Index value '" + index + "' not in range [0.." + (this._prodProdRelnList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.ProdProdReln) _prodProdRelnList.get(index);
    }

    /**
     * Method getProdProdReln.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.ProdProdReln[] getProdProdReln(
    ) {
        com.dummy.wpc.batch.xml.ProdProdReln[] array = new com.dummy.wpc.batch.xml.ProdProdReln[0];
        return (com.dummy.wpc.batch.xml.ProdProdReln[]) this._prodProdRelnList.toArray(array);
    }

    /**
     * Method getProdProdRelnCount.
     * 
     * @return the size of this collection
     */
    public int getProdProdRelnCount(
    ) {
        return this._prodProdRelnList.size();
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
    public void removeAllProdProdReln(
    ) {
        this._prodProdRelnList.clear();
    }

    /**
     * Method removeProdProdReln.
     * 
     * @param vProdProdReln
     * @return true if the object was removed from the collection.
     */
    public boolean removeProdProdReln(
            final com.dummy.wpc.batch.xml.ProdProdReln vProdProdReln) {
        boolean removed = _prodProdRelnList.remove(vProdProdReln);
        return removed;
    }

    /**
     * Method removeProdProdRelnAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.ProdProdReln removeProdProdRelnAt(
            final int index) {
        java.lang.Object obj = this._prodProdRelnList.remove(index);
        return (com.dummy.wpc.batch.xml.ProdProdReln) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vProdProdReln
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setProdProdReln(
            final int index,
            final com.dummy.wpc.batch.xml.ProdProdReln vProdProdReln)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodProdRelnList.size()) {
            throw new IndexOutOfBoundsException("setProdProdReln: Index value '" + index + "' not in range [0.." + (this._prodProdRelnList.size() - 1) + "]");
        }
        
        this._prodProdRelnList.set(index, vProdProdReln);
    }

    /**
     * 
     * 
     * @param vProdProdRelnArray
     */
    public void setProdProdReln(
            final com.dummy.wpc.batch.xml.ProdProdReln[] vProdProdRelnArray) {
        //-- copy array
        _prodProdRelnList.clear();
        
        for (int i = 0; i < vProdProdRelnArray.length; i++) {
                this._prodProdRelnList.add(vProdProdRelnArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.ProdProdRelnLs
     */
    public static com.dummy.wpc.batch.xml.ProdProdRelnLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProdProdRelnLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProdProdRelnLst.class, reader);
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
