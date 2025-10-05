/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ProdTradElgLst.java 1.3 2011/12/06 19:08:27CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProdTradElgLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:27CST $
 */
public class ProdTradElgLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodTradElgList.
     */
    private java.util.Vector _prodTradElgList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProdTradElgLst() {
        super();
        this._prodTradElgList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vProdTradElg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdTradElg(
            final com.dummy.wpc.batch.xml.ProdTradElg vProdTradElg)
    throws java.lang.IndexOutOfBoundsException {
        this._prodTradElgList.addElement(vProdTradElg);
    }

    /**
     * 
     * 
     * @param index
     * @param vProdTradElg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdTradElg(
            final int index,
            final com.dummy.wpc.batch.xml.ProdTradElg vProdTradElg)
    throws java.lang.IndexOutOfBoundsException {
        this._prodTradElgList.add(index, vProdTradElg);
    }

    /**
     * Method enumerateProdTradElg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.ProdTradElg elements
     */
    public java.util.Enumeration enumerateProdTradElg(
    ) {
        return this._prodTradElgList.elements();
    }

    /**
     * Method getProdTradElg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.ProdTradElg
     * at the given index
     */
    public com.dummy.wpc.batch.xml.ProdTradElg getProdTradElg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodTradElgList.size()) {
            throw new IndexOutOfBoundsException("getProdTradElg: Index value '" + index + "' not in range [0.." + (this._prodTradElgList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.ProdTradElg) _prodTradElgList.get(index);
    }

    /**
     * Method getProdTradElg.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.ProdTradElg[] getProdTradElg(
    ) {
        com.dummy.wpc.batch.xml.ProdTradElg[] array = new com.dummy.wpc.batch.xml.ProdTradElg[0];
        return (com.dummy.wpc.batch.xml.ProdTradElg[]) this._prodTradElgList.toArray(array);
    }

    /**
     * Method getProdTradElgCount.
     * 
     * @return the size of this collection
     */
    public int getProdTradElgCount(
    ) {
        return this._prodTradElgList.size();
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
    public void removeAllProdTradElg(
    ) {
        this._prodTradElgList.clear();
    }

    /**
     * Method removeProdTradElg.
     * 
     * @param vProdTradElg
     * @return true if the object was removed from the collection.
     */
    public boolean removeProdTradElg(
            final com.dummy.wpc.batch.xml.ProdTradElg vProdTradElg) {
        boolean removed = _prodTradElgList.remove(vProdTradElg);
        return removed;
    }

    /**
     * Method removeProdTradElgAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.ProdTradElg removeProdTradElgAt(
            final int index) {
        java.lang.Object obj = this._prodTradElgList.remove(index);
        return (com.dummy.wpc.batch.xml.ProdTradElg) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vProdTradElg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setProdTradElg(
            final int index,
            final com.dummy.wpc.batch.xml.ProdTradElg vProdTradElg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodTradElgList.size()) {
            throw new IndexOutOfBoundsException("setProdTradElg: Index value '" + index + "' not in range [0.." + (this._prodTradElgList.size() - 1) + "]");
        }
        
        this._prodTradElgList.set(index, vProdTradElg);
    }

    /**
     * 
     * 
     * @param vProdTradElgArray
     */
    public void setProdTradElg(
            final com.dummy.wpc.batch.xml.ProdTradElg[] vProdTradElgArray) {
        //-- copy array
        _prodTradElgList.clear();
        
        for (int i = 0; i < vProdTradElgArray.length; i++) {
                this._prodTradElgList.add(vProdTradElgArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.ProdTradElgLst
     */
    public static com.dummy.wpc.batch.xml.ProdTradElgLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProdTradElgLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProdTradElgLst.class, reader);
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
