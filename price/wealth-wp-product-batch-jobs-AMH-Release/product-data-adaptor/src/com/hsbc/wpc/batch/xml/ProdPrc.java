/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ProdPrc.java 1.3.2.1 2012/11/23 20:31:41CST 43701020 Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProdPrc.
 * 
 * @version $Revision: 1.3.2.1 $ $Date: 2012/11/23 20:31:41CST $
 */
public class ProdPrc implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodKeySeg.
     */
    private com.dummy.wpc.batch.xml.ProdKeySeg _prodKeySeg;

    /**
     * Field _prodPrcSegList.
     */
    private java.util.Vector _prodPrcSegList;

    /**
     * Field _sidProdPrcSeg.
     */
    private com.dummy.wpc.batch.xml.SidProdPrcSeg _sidProdPrcSeg;

    /**
     * Field _eliDctSellPctSeg.
     */
    private com.dummy.wpc.batch.xml.EliDctSellPctSeg _eliDctSellPctSeg;

      //----------------/
     //- Constructors -/
    //----------------/

    public ProdPrc() {
        super();
        this._prodPrcSegList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vProdPrcSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdPrcSeg(
            final com.dummy.wpc.batch.xml.ProdPrcSeg vProdPrcSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._prodPrcSegList.addElement(vProdPrcSeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vProdPrcSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdPrcSeg(
            final int index,
            final com.dummy.wpc.batch.xml.ProdPrcSeg vProdPrcSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._prodPrcSegList.add(index, vProdPrcSeg);
    }

    /**
     * Method enumerateProdPrcSeg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.ProdPrcSeg elements
     */
    public java.util.Enumeration enumerateProdPrcSeg(
    ) {
        return this._prodPrcSegList.elements();
    }

    /**
     * Returns the value of field 'eliDctSellPctSeg'.
     * 
     * @return the value of field 'EliDctSellPctSeg'.
     */
    public com.dummy.wpc.batch.xml.EliDctSellPctSeg getEliDctSellPctSeg(
    ) {
        return this._eliDctSellPctSeg;
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
     * Method getProdPrcSeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.ProdPrcSeg
     * at the given index
     */
    public com.dummy.wpc.batch.xml.ProdPrcSeg getProdPrcSeg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodPrcSegList.size()) {
            throw new IndexOutOfBoundsException("getProdPrcSeg: Index value '" + index + "' not in range [0.." + (this._prodPrcSegList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.ProdPrcSeg) _prodPrcSegList.get(index);
    }

    /**
     * Method getProdPrcSeg.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.ProdPrcSeg[] getProdPrcSeg(
    ) {
        com.dummy.wpc.batch.xml.ProdPrcSeg[] array = new com.dummy.wpc.batch.xml.ProdPrcSeg[0];
        return (com.dummy.wpc.batch.xml.ProdPrcSeg[]) this._prodPrcSegList.toArray(array);
    }

    /**
     * Method getProdPrcSegCount.
     * 
     * @return the size of this collection
     */
    public int getProdPrcSegCount(
    ) {
        return this._prodPrcSegList.size();
    }

    /**
     * Returns the value of field 'sidProdPrcSeg'.
     * 
     * @return the value of field 'SidProdPrcSeg'.
     */
    public com.dummy.wpc.batch.xml.SidProdPrcSeg getSidProdPrcSeg(
    ) {
        return this._sidProdPrcSeg;
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
    public void removeAllProdPrcSeg(
    ) {
        this._prodPrcSegList.clear();
    }

    /**
     * Method removeProdPrcSeg.
     * 
     * @param vProdPrcSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeProdPrcSeg(
            final com.dummy.wpc.batch.xml.ProdPrcSeg vProdPrcSeg) {
        boolean removed = _prodPrcSegList.remove(vProdPrcSeg);
        return removed;
    }

    /**
     * Method removeProdPrcSegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.ProdPrcSeg removeProdPrcSegAt(
            final int index) {
        java.lang.Object obj = this._prodPrcSegList.remove(index);
        return (com.dummy.wpc.batch.xml.ProdPrcSeg) obj;
    }

    /**
     * Sets the value of field 'eliDctSellPctSeg'.
     * 
     * @param eliDctSellPctSeg the value of field 'eliDctSellPctSeg'
     */
    public void setEliDctSellPctSeg(
            final com.dummy.wpc.batch.xml.EliDctSellPctSeg eliDctSellPctSeg) {
        this._eliDctSellPctSeg = eliDctSellPctSeg;
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
     * 
     * 
     * @param index
     * @param vProdPrcSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setProdPrcSeg(
            final int index,
            final com.dummy.wpc.batch.xml.ProdPrcSeg vProdPrcSeg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodPrcSegList.size()) {
            throw new IndexOutOfBoundsException("setProdPrcSeg: Index value '" + index + "' not in range [0.." + (this._prodPrcSegList.size() - 1) + "]");
        }
        
        this._prodPrcSegList.set(index, vProdPrcSeg);
    }

    /**
     * 
     * 
     * @param vProdPrcSegArray
     */
    public void setProdPrcSeg(
            final com.dummy.wpc.batch.xml.ProdPrcSeg[] vProdPrcSegArray) {
        //-- copy array
        _prodPrcSegList.clear();
        
        for (int i = 0; i < vProdPrcSegArray.length; i++) {
                this._prodPrcSegList.add(vProdPrcSegArray[i]);
        }
    }

    /**
     * Sets the value of field 'sidProdPrcSeg'.
     * 
     * @param sidProdPrcSeg the value of field 'sidProdPrcSeg'.
     */
    public void setSidProdPrcSeg(
            final com.dummy.wpc.batch.xml.SidProdPrcSeg sidProdPrcSeg) {
        this._sidProdPrcSeg = sidProdPrcSeg;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.dummy.wpc.batch.xml.ProdPrc
     */
    public static com.dummy.wpc.batch.xml.ProdPrc unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProdPrc) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProdPrc.class, reader);
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
