/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/Sid.java 1.3 2011/12/06 19:07:58CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Sid.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:07:58CST $
 */
public class Sid implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodKeySeg.
     */
    private com.dummy.wpc.batch.xml.ProdKeySeg _prodKeySeg;

    /**
     * Field _prodAltNumSegList.
     */
    private java.util.Vector _prodAltNumSegList;

    /**
     * Field _prodInfoSeg.
     */
    private com.dummy.wpc.batch.xml.ProdInfoSeg _prodInfoSeg;

    /**
     * Field _sidSeg.
     */
    private com.dummy.wpc.batch.xml.SidSeg _sidSeg;

    /**
     * Field _recDtTmSeg.
     */
    private com.dummy.wpc.batch.xml.RecDtTmSeg _recDtTmSeg;


      //----------------/
     //- Constructors -/
    //----------------/

    public Sid() {
        super();
        this._prodAltNumSegList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vProdAltNumSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdAltNumSeg(
            final com.dummy.wpc.batch.xml.ProdAltNumSeg vProdAltNumSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._prodAltNumSegList.addElement(vProdAltNumSeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vProdAltNumSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdAltNumSeg(
            final int index,
            final com.dummy.wpc.batch.xml.ProdAltNumSeg vProdAltNumSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._prodAltNumSegList.add(index, vProdAltNumSeg);
    }

    /**
     * Method enumerateProdAltNumSeg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.ProdAltNumSeg elements
     */
    public java.util.Enumeration enumerateProdAltNumSeg(
    ) {
        return this._prodAltNumSegList.elements();
    }

    /**
     * Method getProdAltNumSeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.xml.ProdAltNumSeg at the given index
     */
    public com.dummy.wpc.batch.xml.ProdAltNumSeg getProdAltNumSeg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodAltNumSegList.size()) {
            throw new IndexOutOfBoundsException("getProdAltNumSeg: Index value '" + index + "' not in range [0.." + (this._prodAltNumSegList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.ProdAltNumSeg) _prodAltNumSegList.get(index);
    }

    /**
     * Method getProdAltNumSeg.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.ProdAltNumSeg[] getProdAltNumSeg(
    ) {
        com.dummy.wpc.batch.xml.ProdAltNumSeg[] array = new com.dummy.wpc.batch.xml.ProdAltNumSeg[0];
        return (com.dummy.wpc.batch.xml.ProdAltNumSeg[]) this._prodAltNumSegList.toArray(array);
    }

    /**
     * Method getProdAltNumSegCount.
     * 
     * @return the size of this collection
     */
    public int getProdAltNumSegCount(
    ) {
        return this._prodAltNumSegList.size();
    }

    /**
     * Returns the value of field 'prodInfoSeg'.
     * 
     * @return the value of field 'ProdInfoSeg'.
     */
    public com.dummy.wpc.batch.xml.ProdInfoSeg getProdInfoSeg(
    ) {
        return this._prodInfoSeg;
    }

    /**
     * Returns the value of field 'prodKeySeg'.
     * 
     * @return the value of field 'ProdKeySeg'.
     */
    public com.dummy.wpc.batch.xml.ProdKeySeg getProdKeySeg(
    ) {
        return this._prodKeySeg;
    }

    /**
     * Returns the value of field 'recDtTmSeg'.
     * 
     * @return the value of field 'RecDtTmSeg'.
     */
    public com.dummy.wpc.batch.xml.RecDtTmSeg getRecDtTmSeg(
    ) {
        return this._recDtTmSeg;
    }

    /**
     * Returns the value of field 'sidSeg'.
     * 
     * @return the value of field 'SidSeg'.
     */
    public com.dummy.wpc.batch.xml.SidSeg getSidSeg(
    ) {
        return this._sidSeg;
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
    public void removeAllProdAltNumSeg(
    ) {
        this._prodAltNumSegList.clear();
    }

    /**
     * Method removeProdAltNumSeg.
     * 
     * @param vProdAltNumSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeProdAltNumSeg(
            final com.dummy.wpc.batch.xml.ProdAltNumSeg vProdAltNumSeg) {
        boolean removed = _prodAltNumSegList.remove(vProdAltNumSeg);
        return removed;
    }

    /**
     * Method removeProdAltNumSegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.ProdAltNumSeg removeProdAltNumSegAt(
            final int index) {
        java.lang.Object obj = this._prodAltNumSegList.remove(index);
        return (com.dummy.wpc.batch.xml.ProdAltNumSeg) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vProdAltNumSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setProdAltNumSeg(
            final int index,
            final com.dummy.wpc.batch.xml.ProdAltNumSeg vProdAltNumSeg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodAltNumSegList.size()) {
            throw new IndexOutOfBoundsException("setProdAltNumSeg: Index value '" + index + "' not in range [0.." + (this._prodAltNumSegList.size() - 1) + "]");
        }
        
        this._prodAltNumSegList.set(index, vProdAltNumSeg);
    }

    /**
     * 
     * 
     * @param vProdAltNumSegArray
     */
    public void setProdAltNumSeg(
            final com.dummy.wpc.batch.xml.ProdAltNumSeg[] vProdAltNumSegArray) {
        //-- copy array
        _prodAltNumSegList.clear();
        
        for (int i = 0; i < vProdAltNumSegArray.length; i++) {
                this._prodAltNumSegList.add(vProdAltNumSegArray[i]);
        }
    }

    /**
     * Sets the value of field 'prodInfoSeg'.
     * 
     * @param prodInfoSeg the value of field 'prodInfoSeg'.
     */
    public void setProdInfoSeg(
            final com.dummy.wpc.batch.xml.ProdInfoSeg prodInfoSeg) {
        this._prodInfoSeg = prodInfoSeg;
    }

    /**
     * Sets the value of field 'prodKeySeg'.
     * 
     * @param prodKeySeg the value of field 'prodKeySeg'.
     */
    public void setProdKeySeg(
            final com.dummy.wpc.batch.xml.ProdKeySeg prodKeySeg) {
        this._prodKeySeg = prodKeySeg;
    }

    /**
     * Sets the value of field 'recDtTmSeg'.
     * 
     * @param recDtTmSeg the value of field 'recDtTmSeg'.
     */
    public void setRecDtTmSeg(
            final com.dummy.wpc.batch.xml.RecDtTmSeg recDtTmSeg) {
        this._recDtTmSeg = recDtTmSeg;
    }

    /**
     * Sets the value of field 'sidSeg'.
     * 
     * @param sidSeg the value of field 'sidSeg'.
     */
    public void setSidSeg(
            final com.dummy.wpc.batch.xml.SidSeg sidSeg) {
        this._sidSeg = sidSeg;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.dummy.wpc.batch.xml.Sid
     */
    public static com.dummy.wpc.batch.xml.Sid unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.Sid) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.Sid.class, reader);
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
