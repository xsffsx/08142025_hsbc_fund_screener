/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ProdPrcLst.java 1.3 2011/12/06 19:08:25CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProdPrcLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:25CST $
 */
public class ProdPrcLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodPrcList.
     */
    private java.util.Vector _prodPrcList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProdPrcLst() {
        super();
        this._prodPrcList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vProdPrc
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdPrc(
            final com.dummy.wpc.batch.xml.ProdPrc vProdPrc)
    throws java.lang.IndexOutOfBoundsException {
        this._prodPrcList.addElement(vProdPrc);
    }

    /**
     * 
     * 
     * @param index
     * @param vProdPrc
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdPrc(
            final int index,
            final com.dummy.wpc.batch.xml.ProdPrc vProdPrc)
    throws java.lang.IndexOutOfBoundsException {
        this._prodPrcList.add(index, vProdPrc);
    }

    /**
     * Method enumerateProdPrc.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.ProdPrc elements
     */
    public java.util.Enumeration enumerateProdPrc(
    ) {
        return this._prodPrcList.elements();
    }

    /**
     * Method getProdPrc.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.ProdPrc at
     * the given index
     */
    public com.dummy.wpc.batch.xml.ProdPrc getProdPrc(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodPrcList.size()) {
            throw new IndexOutOfBoundsException("getProdPrc: Index value '" + index + "' not in range [0.." + (this._prodPrcList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.ProdPrc) _prodPrcList.get(index);
    }

    /**
     * Method getProdPrc.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.ProdPrc[] getProdPrc(
    ) {
        com.dummy.wpc.batch.xml.ProdPrc[] array = new com.dummy.wpc.batch.xml.ProdPrc[0];
        return (com.dummy.wpc.batch.xml.ProdPrc[]) this._prodPrcList.toArray(array);
    }

    /**
     * Method getProdPrcCount.
     * 
     * @return the size of this collection
     */
    public int getProdPrcCount(
    ) {
        return this._prodPrcList.size();
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
    public void removeAllProdPrc(
    ) {
        this._prodPrcList.clear();
    }

    /**
     * Method removeProdPrc.
     * 
     * @param vProdPrc
     * @return true if the object was removed from the collection.
     */
    public boolean removeProdPrc(
            final com.dummy.wpc.batch.xml.ProdPrc vProdPrc) {
        boolean removed = _prodPrcList.remove(vProdPrc);
        return removed;
    }

    /**
     * Method removeProdPrcAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.ProdPrc removeProdPrcAt(
            final int index) {
        java.lang.Object obj = this._prodPrcList.remove(index);
        return (com.dummy.wpc.batch.xml.ProdPrc) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vProdPrc
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setProdPrc(
            final int index,
            final com.dummy.wpc.batch.xml.ProdPrc vProdPrc)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodPrcList.size()) {
            throw new IndexOutOfBoundsException("setProdPrc: Index value '" + index + "' not in range [0.." + (this._prodPrcList.size() - 1) + "]");
        }
        
        this._prodPrcList.set(index, vProdPrc);
    }

    /**
     * 
     * 
     * @param vProdPrcArray
     */
    public void setProdPrc(
            final com.dummy.wpc.batch.xml.ProdPrc[] vProdPrcArray) {
        //-- copy array
        _prodPrcList.clear();
        
        for (int i = 0; i < vProdPrcArray.length; i++) {
                this._prodPrcList.add(vProdPrcArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.ProdPrcLst
     */
    public static com.dummy.wpc.batch.xml.ProdPrcLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProdPrcLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProdPrcLst.class, reader);
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
