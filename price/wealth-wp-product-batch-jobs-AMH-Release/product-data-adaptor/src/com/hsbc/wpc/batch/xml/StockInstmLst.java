/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/StockInstmLst.java 1.3 2011/12/06 19:08:09CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class StockInstmLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:09CST $
 */
public class StockInstmLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _stockInstmList.
     */
    private java.util.Vector _stockInstmList;


      //----------------/
     //- Constructors -/
    //----------------/

    public StockInstmLst() {
        super();
        this._stockInstmList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vStockInstm
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addStockInstm(
            final com.dummy.wpc.batch.xml.StockInstm vStockInstm)
    throws java.lang.IndexOutOfBoundsException {
        this._stockInstmList.addElement(vStockInstm);
    }

    /**
     * 
     * 
     * @param index
     * @param vStockInstm
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addStockInstm(
            final int index,
            final com.dummy.wpc.batch.xml.StockInstm vStockInstm)
    throws java.lang.IndexOutOfBoundsException {
        this._stockInstmList.add(index, vStockInstm);
    }

    /**
     * Method enumerateStockInstm.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.StockInstm elements
     */
    public java.util.Enumeration enumerateStockInstm(
    ) {
        return this._stockInstmList.elements();
    }

    /**
     * Method getStockInstm.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.StockInstm
     * at the given index
     */
    public com.dummy.wpc.batch.xml.StockInstm getStockInstm(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._stockInstmList.size()) {
            throw new IndexOutOfBoundsException("getStockInstm: Index value '" + index + "' not in range [0.." + (this._stockInstmList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.StockInstm) _stockInstmList.get(index);
    }

    /**
     * Method getStockInstm.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.StockInstm[] getStockInstm(
    ) {
        com.dummy.wpc.batch.xml.StockInstm[] array = new com.dummy.wpc.batch.xml.StockInstm[0];
        return (com.dummy.wpc.batch.xml.StockInstm[]) this._stockInstmList.toArray(array);
    }

    /**
     * Method getStockInstmCount.
     * 
     * @return the size of this collection
     */
    public int getStockInstmCount(
    ) {
        return this._stockInstmList.size();
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
    public void removeAllStockInstm(
    ) {
        this._stockInstmList.clear();
    }

    /**
     * Method removeStockInstm.
     * 
     * @param vStockInstm
     * @return true if the object was removed from the collection.
     */
    public boolean removeStockInstm(
            final com.dummy.wpc.batch.xml.StockInstm vStockInstm) {
        boolean removed = _stockInstmList.remove(vStockInstm);
        return removed;
    }

    /**
     * Method removeStockInstmAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.StockInstm removeStockInstmAt(
            final int index) {
        java.lang.Object obj = this._stockInstmList.remove(index);
        return (com.dummy.wpc.batch.xml.StockInstm) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vStockInstm
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setStockInstm(
            final int index,
            final com.dummy.wpc.batch.xml.StockInstm vStockInstm)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._stockInstmList.size()) {
            throw new IndexOutOfBoundsException("setStockInstm: Index value '" + index + "' not in range [0.." + (this._stockInstmList.size() - 1) + "]");
        }
        
        this._stockInstmList.set(index, vStockInstm);
    }

    /**
     * 
     * 
     * @param vStockInstmArray
     */
    public void setStockInstm(
            final com.dummy.wpc.batch.xml.StockInstm[] vStockInstmArray) {
        //-- copy array
        _stockInstmList.clear();
        
        for (int i = 0; i < vStockInstmArray.length; i++) {
                this._stockInstmList.add(vStockInstmArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.StockInstmLst
     */
    public static com.dummy.wpc.batch.xml.StockInstmLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.StockInstmLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.StockInstmLst.class, reader);
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
