/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/DpsLst.java 1.3 2011/12/06 19:08:14CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class DpsLst.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:14CST $
 */
public class DpsLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _dpsList.
     */
    private java.util.Vector _dpsList;


      //----------------/
     //- Constructors -/
    //----------------/

    public DpsLst() {
        super();
        this._dpsList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vDps
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDps(
            final com.dummy.wpc.batch.xml.Dps vDps)
    throws java.lang.IndexOutOfBoundsException {
        this._dpsList.addElement(vDps);
    }

    /**
     * 
     * 
     * @param index
     * @param vDps
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDps(
            final int index,
            final com.dummy.wpc.batch.xml.Dps vDps)
    throws java.lang.IndexOutOfBoundsException {
        this._dpsList.add(index, vDps);
    }

    /**
     * Method enumerateDps.
     * 
     * @return an Enumeration over all com.dummy.wpc.batch.xml.Dps
     * elements
     */
    public java.util.Enumeration enumerateDps(
    ) {
        return this._dpsList.elements();
    }

    /**
     * Method getDps.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.Dps at the
     * given index
     */
    public com.dummy.wpc.batch.xml.Dps getDps(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._dpsList.size()) {
            throw new IndexOutOfBoundsException("getDps: Index value '" + index + "' not in range [0.." + (this._dpsList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.Dps) _dpsList.get(index);
    }

    /**
     * Method getDps.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.Dps[] getDps(
    ) {
        com.dummy.wpc.batch.xml.Dps[] array = new com.dummy.wpc.batch.xml.Dps[0];
        return (com.dummy.wpc.batch.xml.Dps[]) this._dpsList.toArray(array);
    }

    /**
     * Method getDpsCount.
     * 
     * @return the size of this collection
     */
    public int getDpsCount(
    ) {
        return this._dpsList.size();
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
    public void removeAllDps(
    ) {
        this._dpsList.clear();
    }

    /**
     * Method removeDps.
     * 
     * @param vDps
     * @return true if the object was removed from the collection.
     */
    public boolean removeDps(
            final com.dummy.wpc.batch.xml.Dps vDps) {
        boolean removed = _dpsList.remove(vDps);
        return removed;
    }

    /**
     * Method removeDpsAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.Dps removeDpsAt(
            final int index) {
        java.lang.Object obj = this._dpsList.remove(index);
        return (com.dummy.wpc.batch.xml.Dps) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vDps
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDps(
            final int index,
            final com.dummy.wpc.batch.xml.Dps vDps)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._dpsList.size()) {
            throw new IndexOutOfBoundsException("setDps: Index value '" + index + "' not in range [0.." + (this._dpsList.size() - 1) + "]");
        }
        
        this._dpsList.set(index, vDps);
    }

    /**
     * 
     * 
     * @param vDpsArray
     */
    public void setDps(
            final com.dummy.wpc.batch.xml.Dps[] vDpsArray) {
        //-- copy array
        _dpsList.clear();
        
        for (int i = 0; i < vDpsArray.length; i++) {
                this._dpsList.add(vDpsArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.DpsLst
     */
    public static com.dummy.wpc.batch.xml.DpsLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.DpsLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.DpsLst.class, reader);
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
