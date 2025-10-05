/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ProdPrefLst.java 1.3 2011/12/06 19:08:15CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProdPrefLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:15CST $
 */
public class ProdPrefLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodPerfList.
     */
    private java.util.Vector _prodPerfList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProdPrefLst() {
        super();
        this._prodPerfList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vProdPerf
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdPerf(
            final com.dummy.wpc.batch.xml.ProdPerf vProdPerf)
    throws java.lang.IndexOutOfBoundsException {
        this._prodPerfList.addElement(vProdPerf);
    }

    /**
     * 
     * 
     * @param index
     * @param vProdPerf
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdPerf(
            final int index,
            final com.dummy.wpc.batch.xml.ProdPerf vProdPerf)
    throws java.lang.IndexOutOfBoundsException {
        this._prodPerfList.add(index, vProdPerf);
    }

    /**
     * Method enumerateProdPerf.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.ProdPerf elements
     */
    public java.util.Enumeration enumerateProdPerf(
    ) {
        return this._prodPerfList.elements();
    }

    /**
     * Method getProdPerf.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.ProdPerf at
     * the given index
     */
    public com.dummy.wpc.batch.xml.ProdPerf getProdPerf(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodPerfList.size()) {
            throw new IndexOutOfBoundsException("getProdPerf: Index value '" + index + "' not in range [0.." + (this._prodPerfList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.ProdPerf) _prodPerfList.get(index);
    }

    /**
     * Method getProdPerf.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.ProdPerf[] getProdPerf(
    ) {
        com.dummy.wpc.batch.xml.ProdPerf[] array = new com.dummy.wpc.batch.xml.ProdPerf[0];
        return (com.dummy.wpc.batch.xml.ProdPerf[]) this._prodPerfList.toArray(array);
    }

    /**
     * Method getProdPerfCount.
     * 
     * @return the size of this collection
     */
    public int getProdPerfCount(
    ) {
        return this._prodPerfList.size();
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
    public void removeAllProdPerf(
    ) {
        this._prodPerfList.clear();
    }

    /**
     * Method removeProdPerf.
     * 
     * @param vProdPerf
     * @return true if the object was removed from the collection.
     */
    public boolean removeProdPerf(
            final com.dummy.wpc.batch.xml.ProdPerf vProdPerf) {
        boolean removed = _prodPerfList.remove(vProdPerf);
        return removed;
    }

    /**
     * Method removeProdPerfAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.ProdPerf removeProdPerfAt(
            final int index) {
        java.lang.Object obj = this._prodPerfList.remove(index);
        return (com.dummy.wpc.batch.xml.ProdPerf) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vProdPerf
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setProdPerf(
            final int index,
            final com.dummy.wpc.batch.xml.ProdPerf vProdPerf)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodPerfList.size()) {
            throw new IndexOutOfBoundsException("setProdPerf: Index value '" + index + "' not in range [0.." + (this._prodPerfList.size() - 1) + "]");
        }
        
        this._prodPerfList.set(index, vProdPerf);
    }

    /**
     * 
     * 
     * @param vProdPerfArray
     */
    public void setProdPerf(
            final com.dummy.wpc.batch.xml.ProdPerf[] vProdPerfArray) {
        //-- copy array
        _prodPerfList.clear();
        
        for (int i = 0; i < vProdPerfArray.length; i++) {
                this._prodPerfList.add(vProdPerfArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.ProdPrefLst
     */
    public static com.dummy.wpc.batch.xml.ProdPrefLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProdPrefLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProdPrefLst.class, reader);
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
