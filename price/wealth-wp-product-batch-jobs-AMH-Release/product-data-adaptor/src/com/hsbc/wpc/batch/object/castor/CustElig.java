/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/object/castor/CustElig.java 1.1 2011/06/15 16:19:59CST Perry Guo (WMDHKG0007) Development  $
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
 * Class CustElig.
 * 
 * @version $Revision: 1.1 $ $Date: 2011/06/15 16:19:59CST $
 */
public class CustElig implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodCde
     */
    private java.lang.String _prodCde;

    /**
     * Field _prodTypeCde
     */
    private java.lang.String _prodTypeCde;

    /**
     * Field _prodSubtpCde
     */
    private java.lang.String _prodSubtpCde;

    /**
     * Field _riskLvlCde
     */
    private java.lang.String _riskLvlCde;

    /**
     * Field _rsrCtryInfoList
     */
    private java.util.Vector _rsrCtryInfoList;

    /**
     * Field _formReqCdeList
     */
    private java.util.Vector _formReqCdeList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CustElig() 
     {
        super();
        _rsrCtryInfoList = new Vector();
        _formReqCdeList = new Vector();
    } //-- com.dummy.hfi.batch.object.castor.CustElig()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addFormReqCde
     * 
     * 
     * 
     * @param vFormReqCde
     */
    public void addFormReqCde(java.lang.String vFormReqCde)
        throws java.lang.IndexOutOfBoundsException
    {
        _formReqCdeList.addElement(vFormReqCde);
    } //-- void addFormReqCde(java.lang.String) 

    /**
     * Method addFormReqCde
     * 
     * 
     * 
     * @param index
     * @param vFormReqCde
     */
    public void addFormReqCde(int index, java.lang.String vFormReqCde)
        throws java.lang.IndexOutOfBoundsException
    {
        _formReqCdeList.insertElementAt(vFormReqCde, index);
    } //-- void addFormReqCde(int, java.lang.String) 

    /**
     * Method addRsrCtryInfo
     * 
     * 
     * 
     * @param vRsrCtryInfo
     */
    public void addRsrCtryInfo(com.dummy.wpc.batch.object.castor.RsrCtryInfo vRsrCtryInfo)
        throws java.lang.IndexOutOfBoundsException
    {
        _rsrCtryInfoList.addElement(vRsrCtryInfo);
    } //-- void addRsrCtryInfo(com.dummy.hfi.batch.object.castor.RsrCtryInfo) 

    /**
     * Method addRsrCtryInfo
     * 
     * 
     * 
     * @param index
     * @param vRsrCtryInfo
     */
    public void addRsrCtryInfo(int index, com.dummy.wpc.batch.object.castor.RsrCtryInfo vRsrCtryInfo)
        throws java.lang.IndexOutOfBoundsException
    {
        _rsrCtryInfoList.insertElementAt(vRsrCtryInfo, index);
    } //-- void addRsrCtryInfo(int, com.dummy.hfi.batch.object.castor.RsrCtryInfo) 

    /**
     * Method enumerateFormReqCde
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateFormReqCde()
    {
        return _formReqCdeList.elements();
    } //-- java.util.Enumeration enumerateFormReqCde() 

    /**
     * Method enumerateRsrCtryInfo
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateRsrCtryInfo()
    {
        return _rsrCtryInfoList.elements();
    } //-- java.util.Enumeration enumerateRsrCtryInfo() 

    /**
     * Method getFormReqCde
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String getFormReqCde(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formReqCdeList.size())) {
            throw new IndexOutOfBoundsException("getFormReqCde: Index value '"+index+"' not in range [0.."+_formReqCdeList.size()+ "]");
        }
        
        return (String)_formReqCdeList.elementAt(index);
    } //-- java.lang.String getFormReqCde(int) 

    /**
     * Method getFormReqCde
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String[] getFormReqCde()
    {
        int size = _formReqCdeList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_formReqCdeList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getFormReqCde() 

    /**
     * Method getFormReqCdeCount
     * 
     * 
     * 
     * @return int
     */
    public int getFormReqCdeCount()
    {
        return _formReqCdeList.size();
    } //-- int getFormReqCdeCount() 

    /**
     * Returns the value of field 'prodCde'.
     * 
     * @return String
     * @return the value of field 'prodCde'.
     */
    public java.lang.String getProdCde()
    {
        return this._prodCde;
    } //-- java.lang.String getProdCde() 

    /**
     * Returns the value of field 'prodSubtpCde'.
     * 
     * @return String
     * @return the value of field 'prodSubtpCde'.
     */
    public java.lang.String getProdSubtpCde()
    {
        return this._prodSubtpCde;
    } //-- java.lang.String getProdSubtpCde() 

    /**
     * Returns the value of field 'prodTypeCde'.
     * 
     * @return String
     * @return the value of field 'prodTypeCde'.
     */
    public java.lang.String getProdTypeCde()
    {
        return this._prodTypeCde;
    } //-- java.lang.String getProdTypeCde() 

    /**
     * Returns the value of field 'riskLvlCde'.
     * 
     * @return String
     * @return the value of field 'riskLvlCde'.
     */
    public java.lang.String getRiskLvlCde()
    {
        return this._riskLvlCde;
    } //-- java.lang.String getRiskLvlCde() 

    /**
     * Method getRsrCtryInfo
     * 
     * 
     * 
     * @param index
     * @return RsrCtryInfo
     */
    public com.dummy.wpc.batch.object.castor.RsrCtryInfo getRsrCtryInfo(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _rsrCtryInfoList.size())) {
            throw new IndexOutOfBoundsException("getRsrCtryInfo: Index value '"+index+"' not in range [0.."+_rsrCtryInfoList.size()+ "]");
        }
        
        return (com.dummy.wpc.batch.object.castor.RsrCtryInfo) _rsrCtryInfoList.elementAt(index);
    } //-- com.dummy.hfi.batch.object.castor.RsrCtryInfo getRsrCtryInfo(int) 

    /**
     * Method getRsrCtryInfo
     * 
     * 
     * 
     * @return RsrCtryInfo
     */
    public com.dummy.wpc.batch.object.castor.RsrCtryInfo[] getRsrCtryInfo()
    {
        int size = _rsrCtryInfoList.size();
        com.dummy.wpc.batch.object.castor.RsrCtryInfo[] mArray = new com.dummy.wpc.batch.object.castor.RsrCtryInfo[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.dummy.wpc.batch.object.castor.RsrCtryInfo) _rsrCtryInfoList.elementAt(index);
        }
        return mArray;
    } //-- com.dummy.hfi.batch.object.castor.RsrCtryInfo[] getRsrCtryInfo() 

    /**
     * Method getRsrCtryInfoCount
     * 
     * 
     * 
     * @return int
     */
    public int getRsrCtryInfoCount()
    {
        return _rsrCtryInfoList.size();
    } //-- int getRsrCtryInfoCount() 

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
     * Method removeAllFormReqCde
     * 
     */
    public void removeAllFormReqCde()
    {
        _formReqCdeList.removeAllElements();
    } //-- void removeAllFormReqCde() 

    /**
     * Method removeAllRsrCtryInfo
     * 
     */
    public void removeAllRsrCtryInfo()
    {
        _rsrCtryInfoList.removeAllElements();
    } //-- void removeAllRsrCtryInfo() 

    /**
     * Method removeFormReqCde
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String removeFormReqCde(int index)
    {
        java.lang.Object obj = _formReqCdeList.elementAt(index);
        _formReqCdeList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeFormReqCde(int) 

    /**
     * Method removeRsrCtryInfo
     * 
     * 
     * 
     * @param index
     * @return RsrCtryInfo
     */
    public com.dummy.wpc.batch.object.castor.RsrCtryInfo removeRsrCtryInfo(int index)
    {
        java.lang.Object obj = _rsrCtryInfoList.elementAt(index);
        _rsrCtryInfoList.removeElementAt(index);
        return (com.dummy.wpc.batch.object.castor.RsrCtryInfo) obj;
    } //-- com.dummy.hfi.batch.object.castor.RsrCtryInfo removeRsrCtryInfo(int) 

    /**
     * Method setFormReqCde
     * 
     * 
     * 
     * @param index
     * @param vFormReqCde
     */
    public void setFormReqCde(int index, java.lang.String vFormReqCde)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formReqCdeList.size())) {
            throw new IndexOutOfBoundsException("setFormReqCde: Index value '"+index+"' not in range [0.."+_formReqCdeList.size()+ "]");
        }
        _formReqCdeList.setElementAt(vFormReqCde, index);
    } //-- void setFormReqCde(int, java.lang.String) 

    /**
     * Method setFormReqCde
     * 
     * 
     * 
     * @param formReqCdeArray
     */
    public void setFormReqCde(java.lang.String[] formReqCdeArray)
    {
        //-- copy array
        _formReqCdeList.removeAllElements();
        for (int i = 0; i < formReqCdeArray.length; i++) {
            _formReqCdeList.addElement(formReqCdeArray[i]);
        }
    } //-- void setFormReqCde(java.lang.String) 

    /**
     * Sets the value of field 'prodCde'.
     * 
     * @param prodCde the value of field 'prodCde'.
     */
    public void setProdCde(java.lang.String prodCde)
    {
        this._prodCde = prodCde;
    } //-- void setProdCde(java.lang.String) 

    /**
     * Sets the value of field 'prodSubtpCde'.
     * 
     * @param prodSubtpCde the value of field 'prodSubtpCde'.
     */
    public void setProdSubtpCde(java.lang.String prodSubtpCde)
    {
        this._prodSubtpCde = prodSubtpCde;
    } //-- void setProdSubtpCde(java.lang.String) 

    /**
     * Sets the value of field 'prodTypeCde'.
     * 
     * @param prodTypeCde the value of field 'prodTypeCde'.
     */
    public void setProdTypeCde(java.lang.String prodTypeCde)
    {
        this._prodTypeCde = prodTypeCde;
    } //-- void setProdTypeCde(java.lang.String) 

    /**
     * Sets the value of field 'riskLvlCde'.
     * 
     * @param riskLvlCde the value of field 'riskLvlCde'.
     */
    public void setRiskLvlCde(java.lang.String riskLvlCde)
    {
        this._riskLvlCde = riskLvlCde;
    } //-- void setRiskLvlCde(java.lang.String) 

    /**
     * Method setRsrCtryInfo
     * 
     * 
     * 
     * @param index
     * @param vRsrCtryInfo
     */
    public void setRsrCtryInfo(int index, com.dummy.wpc.batch.object.castor.RsrCtryInfo vRsrCtryInfo)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _rsrCtryInfoList.size())) {
            throw new IndexOutOfBoundsException("setRsrCtryInfo: Index value '"+index+"' not in range [0.."+_rsrCtryInfoList.size()+ "]");
        }
        _rsrCtryInfoList.setElementAt(vRsrCtryInfo, index);
    } //-- void setRsrCtryInfo(int, com.dummy.hfi.batch.object.castor.RsrCtryInfo) 

    /**
     * Method setRsrCtryInfo
     * 
     * 
     * 
     * @param rsrCtryInfoArray
     */
    public void setRsrCtryInfo(com.dummy.wpc.batch.object.castor.RsrCtryInfo[] rsrCtryInfoArray)
    {
        //-- copy array
        _rsrCtryInfoList.removeAllElements();
        for (int i = 0; i < rsrCtryInfoArray.length; i++) {
            _rsrCtryInfoList.addElement(rsrCtryInfoArray[i]);
        }
    } //-- void setRsrCtryInfo(com.dummy.hfi.batch.object.castor.RsrCtryInfo) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return CustElig
     */
    public static com.dummy.wpc.batch.object.castor.CustElig unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.dummy.wpc.batch.object.castor.CustElig) Unmarshaller.unmarshal(com.dummy.wpc.batch.object.castor.CustElig.class, reader);
    } //-- com.dummy.hfi.batch.object.castor.CustElig unmarshal(java.io.Reader) 

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
