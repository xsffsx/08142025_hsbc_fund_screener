/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/DebtInstmLst.java 1.3 2011/12/06 19:08:15CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class DebtInstmLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:15CST $
 */
public class DebtInstmLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _debtInstmList.
     */
    private java.util.Vector _debtInstmList;


      //----------------/
     //- Constructors -/
    //----------------/

    public DebtInstmLst() {
        super();
        this._debtInstmList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vDebtInstm
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDebtInstm(
            final com.dummy.wpc.batch.xml.DebtInstm vDebtInstm)
    throws java.lang.IndexOutOfBoundsException {
        this._debtInstmList.addElement(vDebtInstm);
    }

    /**
     * 
     * 
     * @param index
     * @param vDebtInstm
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDebtInstm(
            final int index,
            final com.dummy.wpc.batch.xml.DebtInstm vDebtInstm)
    throws java.lang.IndexOutOfBoundsException {
        this._debtInstmList.add(index, vDebtInstm);
    }

    /**
     * Method enumerateDebtInstm.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.DebtInstm elements
     */
    public java.util.Enumeration enumerateDebtInstm(
    ) {
        return this._debtInstmList.elements();
    }

    /**
     * Method getDebtInstm.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.DebtInstm at
     * the given index
     */
    public com.dummy.wpc.batch.xml.DebtInstm getDebtInstm(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._debtInstmList.size()) {
            throw new IndexOutOfBoundsException("getDebtInstm: Index value '" + index + "' not in range [0.." + (this._debtInstmList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.DebtInstm) _debtInstmList.get(index);
    }

    /**
     * Method getDebtInstm.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.DebtInstm[] getDebtInstm(
    ) {
        com.dummy.wpc.batch.xml.DebtInstm[] array = new com.dummy.wpc.batch.xml.DebtInstm[0];
        return (com.dummy.wpc.batch.xml.DebtInstm[]) this._debtInstmList.toArray(array);
    }

    /**
     * Method getDebtInstmCount.
     * 
     * @return the size of this collection
     */
    public int getDebtInstmCount(
    ) {
        return this._debtInstmList.size();
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
    public void removeAllDebtInstm(
    ) {
        this._debtInstmList.clear();
    }

    /**
     * Method removeDebtInstm.
     * 
     * @param vDebtInstm
     * @return true if the object was removed from the collection.
     */
    public boolean removeDebtInstm(
            final com.dummy.wpc.batch.xml.DebtInstm vDebtInstm) {
        boolean removed = _debtInstmList.remove(vDebtInstm);
        return removed;
    }

    /**
     * Method removeDebtInstmAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.DebtInstm removeDebtInstmAt(
            final int index) {
        java.lang.Object obj = this._debtInstmList.remove(index);
        return (com.dummy.wpc.batch.xml.DebtInstm) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vDebtInstm
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDebtInstm(
            final int index,
            final com.dummy.wpc.batch.xml.DebtInstm vDebtInstm)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._debtInstmList.size()) {
            throw new IndexOutOfBoundsException("setDebtInstm: Index value '" + index + "' not in range [0.." + (this._debtInstmList.size() - 1) + "]");
        }
        
        this._debtInstmList.set(index, vDebtInstm);
    }

    /**
     * 
     * 
     * @param vDebtInstmArray
     */
    public void setDebtInstm(
            final com.dummy.wpc.batch.xml.DebtInstm[] vDebtInstmArray) {
        //-- copy array
        _debtInstmList.clear();
        
        for (int i = 0; i < vDebtInstmArray.length; i++) {
                this._debtInstmList.add(vDebtInstmArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.DebtInstmLst
     */
    public static com.dummy.wpc.batch.xml.DebtInstmLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.DebtInstmLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.DebtInstmLst.class, reader);
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
