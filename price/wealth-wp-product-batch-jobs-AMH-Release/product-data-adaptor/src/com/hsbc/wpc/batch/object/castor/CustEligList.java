/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/object/castor/CustEligList.java 1.1 2011/06/15 16:20:00CST Perry Guo (WMDHKG0007) Development  $
 */

package com.dummy.wpc.batch.object.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class CustEligList.
 * 
 * @version $Revision: 1.1 $ $Date: 2011/06/15 16:20:00CST $
 */
public class CustEligList implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _size
     */
    private int _size;

    /**
     * keeps track of state for field: _size
     */
    private boolean _has_size;

    /**
     * Field _lastUpdate
     */
    private java.lang.String _lastUpdate;

    /**
     * Field _custEligList
     */
    private java.util.Vector _custEligList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CustEligList() 
     {
        super();
        _custEligList = new Vector();
    } //-- com.dummy.hfi.batch.object.castor.CustEligList()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addCustElig
     * 
     * 
     * 
     * @param vCustElig
     */
    public void addCustElig(com.dummy.wpc.batch.object.castor.CustElig vCustElig)
        throws java.lang.IndexOutOfBoundsException
    {
        _custEligList.addElement(vCustElig);
    } //-- void addCustElig(com.dummy.hfi.batch.object.castor.CustElig) 

    /**
     * Method addCustElig
     * 
     * 
     * 
     * @param index
     * @param vCustElig
     */
    public void addCustElig(int index, com.dummy.wpc.batch.object.castor.CustElig vCustElig)
        throws java.lang.IndexOutOfBoundsException
    {
        _custEligList.insertElementAt(vCustElig, index);
    } //-- void addCustElig(int, com.dummy.hfi.batch.object.castor.CustElig) 

    /**
     * Method deleteSize
     * 
     */
    public void deleteSize()
    {
        this._has_size= false;
    } //-- void deleteSize() 

    /**
     * Method enumerateCustElig
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateCustElig()
    {
        return _custEligList.elements();
    } //-- java.util.Enumeration enumerateCustElig() 

    /**
     * Method getCustElig
     * 
     * 
     * 
     * @param index
     * @return CustElig
     */
    public com.dummy.wpc.batch.object.castor.CustElig getCustElig(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _custEligList.size())) {
            throw new IndexOutOfBoundsException("getCustElig: Index value '"+index+"' not in range [0.."+_custEligList.size()+ "]");
        }
        
        return (com.dummy.wpc.batch.object.castor.CustElig) _custEligList.elementAt(index);
    } //-- com.dummy.hfi.batch.object.castor.CustElig getCustElig(int) 

    /**
     * Method getCustElig
     * 
     * 
     * 
     * @return CustElig
     */
    public com.dummy.wpc.batch.object.castor.CustElig[] getCustElig()
    {
        int size = _custEligList.size();
        com.dummy.wpc.batch.object.castor.CustElig[] mArray = new com.dummy.wpc.batch.object.castor.CustElig[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.dummy.wpc.batch.object.castor.CustElig) _custEligList.elementAt(index);
        }
        return mArray;
    } //-- com.dummy.hfi.batch.object.castor.CustElig[] getCustElig() 

    /**
     * Method getCustEligCount
     * 
     * 
     * 
     * @return int
     */
    public int getCustEligCount()
    {
        return _custEligList.size();
    } //-- int getCustEligCount() 

    /**
     * Returns the value of field 'lastUpdate'.
     * 
     * @return String
     * @return the value of field 'lastUpdate'.
     */
    public java.lang.String getLastUpdate()
    {
        return this._lastUpdate;
    } //-- java.lang.String getLastUpdate() 

    /**
     * Returns the value of field 'size'.
     * 
     * @return int
     * @return the value of field 'size'.
     */
    public int getSize()
    {
        return this._size;
    } //-- int getSize() 

    /**
     * Method hasSize
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasSize()
    {
        return this._has_size;
    } //-- boolean hasSize() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeAllCustElig
     * 
     */
    public void removeAllCustElig()
    {
        _custEligList.removeAllElements();
    } //-- void removeAllCustElig() 

    /**
     * Method removeCustElig
     * 
     * 
     * 
     * @param index
     * @return CustElig
     */
    public com.dummy.wpc.batch.object.castor.CustElig removeCustElig(int index)
    {
        java.lang.Object obj = _custEligList.elementAt(index);
        _custEligList.removeElementAt(index);
        return (com.dummy.wpc.batch.object.castor.CustElig) obj;
    } //-- com.dummy.hfi.batch.object.castor.CustElig removeCustElig(int) 

    /**
     * Method setCustElig
     * 
     * 
     * 
     * @param index
     * @param vCustElig
     */
    public void setCustElig(int index, com.dummy.wpc.batch.object.castor.CustElig vCustElig)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _custEligList.size())) {
            throw new IndexOutOfBoundsException("setCustElig: Index value '"+index+"' not in range [0.."+_custEligList.size()+ "]");
        }
        _custEligList.setElementAt(vCustElig, index);
    } //-- void setCustElig(int, com.dummy.hfi.batch.object.castor.CustElig) 

    /**
     * Method setCustElig
     * 
     * 
     * 
     * @param custEligArray
     */
    public void setCustElig(com.dummy.wpc.batch.object.castor.CustElig[] custEligArray)
    {
        //-- copy array
        _custEligList.removeAllElements();
        for (int i = 0; i < custEligArray.length; i++) {
            _custEligList.addElement(custEligArray[i]);
        }
    } //-- void setCustElig(com.dummy.hfi.batch.object.castor.CustElig) 

    /**
     * Sets the value of field 'lastUpdate'.
     * 
     * @param lastUpdate the value of field 'lastUpdate'.
     */
    public void setLastUpdate(java.lang.String lastUpdate)
    {
        this._lastUpdate = lastUpdate;
    } //-- void setLastUpdate(java.lang.String) 

    /**
     * Sets the value of field 'size'.
     * 
     * @param size the value of field 'size'.
     */
    public void setSize(int size)
    {
        this._size = size;
        this._has_size = true;
    } //-- void setSize(int) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return CustEligList
     */
    public static com.dummy.wpc.batch.object.castor.CustEligList unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.dummy.wpc.batch.object.castor.CustEligList) Unmarshaller.unmarshal(com.dummy.wpc.batch.object.castor.CustEligList.class, reader);
    } //-- com.dummy.hfi.batch.object.castor.CustEligList unmarshal(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
