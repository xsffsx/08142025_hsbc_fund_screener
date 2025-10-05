/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/AvcCorlLst.java 1.1 2012/12/27 18:14:12CST 43701020 Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class AvcCorlLst.
 * 
 * @version $Revision: 1.1 $ $Date: 2012/12/27 18:14:12CST $
 */
public class AvcCorlLst implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _avcCorlList.
     */
    private java.util.Vector _avcCorlList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AvcCorlLst() {
        super();
        this._avcCorlList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vAvcCorl
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAvcCorl(
            final com.dummy.wpc.batch.xml.AvcCorl vAvcCorl)
    throws java.lang.IndexOutOfBoundsException {
        this._avcCorlList.addElement(vAvcCorl);
    }

    /**
     * 
     * 
     * @param index
     * @param vAvcCorl
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAvcCorl(
            final int index,
            final com.dummy.wpc.batch.xml.AvcCorl vAvcCorl)
    throws java.lang.IndexOutOfBoundsException {
        this._avcCorlList.add(index, vAvcCorl);
    }

    /**
     * Method enumerateAvcCorl.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.AvcCorl elements
     */
    public java.util.Enumeration enumerateAvcCorl(
    ) {
        return this._avcCorlList.elements();
    }

    /**
     * Method getAvcCorl.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the com.dummy.wpc.batch.xml.AvcCorl at
     * the given index
     */
    public com.dummy.wpc.batch.xml.AvcCorl getAvcCorl(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._avcCorlList.size()) {
            throw new IndexOutOfBoundsException("getAvcCorl: Index value '" + index + "' not in range [0.." + (this._avcCorlList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.AvcCorl) _avcCorlList.get(index);
    }

    /**
     * Method getAvcCorl.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.AvcCorl[] getAvcCorl(
    ) {
        com.dummy.wpc.batch.xml.AvcCorl[] array = new com.dummy.wpc.batch.xml.AvcCorl[0];
        return (com.dummy.wpc.batch.xml.AvcCorl[]) this._avcCorlList.toArray(array);
    }

    /**
     * Method getAvcCorlCount.
     * 
     * @return the size of this collection
     */
    public int getAvcCorlCount(
    ) {
        return this._avcCorlList.size();
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
    public void removeAllAvcCorl(
    ) {
        this._avcCorlList.clear();
    }

    /**
     * Method removeAvcCorl.
     * 
     * @param vAvcCorl
     * @return true if the object was removed from the collection.
     */
    public boolean removeAvcCorl(
            final com.dummy.wpc.batch.xml.AvcCorl vAvcCorl) {
        boolean removed = _avcCorlList.remove(vAvcCorl);
        return removed;
    }

    /**
     * Method removeAvcCorlAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.AvcCorl removeAvcCorlAt(
            final int index) {
        java.lang.Object obj = this._avcCorlList.remove(index);
        return (com.dummy.wpc.batch.xml.AvcCorl) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vAvcCorl
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setAvcCorl(
            final int index,
            final com.dummy.wpc.batch.xml.AvcCorl vAvcCorl)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._avcCorlList.size()) {
            throw new IndexOutOfBoundsException("setAvcCorl: Index value '" + index + "' not in range [0.." + (this._avcCorlList.size() - 1) + "]");
        }
        
        this._avcCorlList.set(index, vAvcCorl);
    }

    /**
     * 
     * 
     * @param vAvcCorlArray
     */
    public void setAvcCorl(
            final com.dummy.wpc.batch.xml.AvcCorl[] vAvcCorlArray) {
        //-- copy array
        _avcCorlList.clear();
        
        for (int i = 0; i < vAvcCorlArray.length; i++) {
                this._avcCorlList.add(vAvcCorlArray[i]);
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
     * @return the unmarshaled com.dummy.wpc.batch.xml.AvcCorlLst
     */
    public static com.dummy.wpc.batch.xml.AvcCorlLst unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.AvcCorlLst) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.AvcCorlLst.class, reader);
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
