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
 * Class ProdProdReln.
 * 
 * @version $Revision$ $Date$
 */
public class ProdProdReln implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ctryRecCde.
     */
    private java.lang.String _ctryRecCde;

    /**
     * Field _grpMembrRecCde.
     */
    private java.lang.String _grpMembrRecCde;

    /**
     * Field _prodAltNum.
     */
    private java.lang.String _prodAltNum;

    /**
     * Field _prodCdeAltClassCde.
     */
    private java.lang.String _prodCdeAltClassCde;

    /**
     * Field _prodProdRelSegList.
     */
    private java.util.Vector _prodProdRelSegList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProdProdReln() {
        super();
        this._prodProdRelSegList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vProdProdRelSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdProdRelSeg(
            final com.dummy.wpc.batch.xml.ProdProdRelSeg vProdProdRelSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._prodProdRelSegList.addElement(vProdProdRelSeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vProdProdRelSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdProdRelSeg(
            final int index,
            final com.dummy.wpc.batch.xml.ProdProdRelSeg vProdProdRelSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._prodProdRelSegList.add(index, vProdProdRelSeg);
    }

    /**
     * Method enumerateProdProdRelSeg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.ProdProdRelSeg elements
     */
    public java.util.Enumeration enumerateProdProdRelSeg(
    ) {
        return this._prodProdRelSegList.elements();
    }

    /**
     * Returns the value of field 'ctryRecCde'.
     * 
     * @return the value of field 'CtryRecCde'.
     */
    public java.lang.String getCtryRecCde(
    ) {
        return this._ctryRecCde;
    }

    /**
     * Returns the value of field 'grpMembrRecCde'.
     * 
     * @return the value of field 'GrpMembrRecCde'.
     */
    public java.lang.String getGrpMembrRecCde(
    ) {
        return this._grpMembrRecCde;
    }

    /**
     * Returns the value of field 'prodAltNum'.
     * 
     * @return the value of field 'ProdAltNum'.
     */
    public java.lang.String getProdAltNum(
    ) {
        return this._prodAltNum;
    }

    /**
     * Returns the value of field 'prodCdeAltClassCde'.
     * 
     * @return the value of field 'ProdCdeAltClassCde'.
     */
    public java.lang.String getProdCdeAltClassCde(
    ) {
        return this._prodCdeAltClassCde;
    }

    /**
     * Method getProdProdRelSeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.xml.ProdProdRelSeg at the given index
     */
    public com.dummy.wpc.batch.xml.ProdProdRelSeg getProdProdRelSeg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodProdRelSegList.size()) {
            throw new IndexOutOfBoundsException("getProdProdRelSeg: Index value '" + index + "' not in range [0.." + (this._prodProdRelSegList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.ProdProdRelSeg) _prodProdRelSegList.get(index);
    }

    /**
     * Method getProdProdRelSeg.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.ProdProdRelSeg[] getProdProdRelSeg(
    ) {
        com.dummy.wpc.batch.xml.ProdProdRelSeg[] array = new com.dummy.wpc.batch.xml.ProdProdRelSeg[0];
        return (com.dummy.wpc.batch.xml.ProdProdRelSeg[]) this._prodProdRelSegList.toArray(array);
    }

    /**
     * Method getProdProdRelSegCount.
     * 
     * @return the size of this collection
     */
    public int getProdProdRelSegCount(
    ) {
        return this._prodProdRelSegList.size();
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
    public void removeAllProdProdRelSeg(
    ) {
        this._prodProdRelSegList.clear();
    }

    /**
     * Method removeProdProdRelSeg.
     * 
     * @param vProdProdRelSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeProdProdRelSeg(
            final com.dummy.wpc.batch.xml.ProdProdRelSeg vProdProdRelSeg) {
        boolean removed = _prodProdRelSegList.remove(vProdProdRelSeg);
        return removed;
    }

    /**
     * Method removeProdProdRelSegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.ProdProdRelSeg removeProdProdRelSegAt(
            final int index) {
        java.lang.Object obj = this._prodProdRelSegList.remove(index);
        return (com.dummy.wpc.batch.xml.ProdProdRelSeg) obj;
    }

    /**
     * Sets the value of field 'ctryRecCde'.
     * 
     * @param ctryRecCde the value of field 'ctryRecCde'.
     */
    public void setCtryRecCde(
            final java.lang.String ctryRecCde) {
        this._ctryRecCde = ctryRecCde;
    }

    /**
     * Sets the value of field 'grpMembrRecCde'.
     * 
     * @param grpMembrRecCde the value of field 'grpMembrRecCde'.
     */
    public void setGrpMembrRecCde(
            final java.lang.String grpMembrRecCde) {
        this._grpMembrRecCde = grpMembrRecCde;
    }

    /**
     * Sets the value of field 'prodAltNum'.
     * 
     * @param prodAltNum the value of field 'prodAltNum'.
     */
    public void setProdAltNum(
            final java.lang.String prodAltNum) {
        this._prodAltNum = prodAltNum;
    }

    /**
     * Sets the value of field 'prodCdeAltClassCde'.
     * 
     * @param prodCdeAltClassCde the value of field
     * 'prodCdeAltClassCde'.
     */
    public void setProdCdeAltClassCde(
            final java.lang.String prodCdeAltClassCde) {
        this._prodCdeAltClassCde = prodCdeAltClassCde;
    }

    /**
     * 
     * 
     * @param index
     * @param vProdProdRelSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setProdProdRelSeg(
            final int index,
            final com.dummy.wpc.batch.xml.ProdProdRelSeg vProdProdRelSeg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodProdRelSegList.size()) {
            throw new IndexOutOfBoundsException("setProdProdRelSeg: Index value '" + index + "' not in range [0.." + (this._prodProdRelSegList.size() - 1) + "]");
        }
        
        this._prodProdRelSegList.set(index, vProdProdRelSeg);
    }

    /**
     * 
     * 
     * @param vProdProdRelSegArray
     */
    public void setProdProdRelSeg(
            final com.dummy.wpc.batch.xml.ProdProdRelSeg[] vProdProdRelSegArray) {
        //-- copy array
        _prodProdRelSegList.clear();
        
        for (int i = 0; i < vProdProdRelSegArray.length; i++) {
                this._prodProdRelSegList.add(vProdProdRelSegArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.ProdProdReln
     */
    public static com.dummy.wpc.batch.xml.ProdProdReln unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProdProdReln) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProdProdReln.class, reader);
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
