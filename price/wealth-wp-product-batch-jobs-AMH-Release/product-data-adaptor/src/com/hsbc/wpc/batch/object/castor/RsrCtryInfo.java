/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/object/castor/RsrCtryInfo.java 1.1 2011/06/15 16:20:07CST Perry Guo (WMDHKG0007) Development  $
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
 * Class RsrCtryInfo.
 * 
 * @version $Revision: 1.1 $ $Date: 2011/06/15 16:20:07CST $
 */
public class RsrCtryInfo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _typeCde
     */
    private java.lang.String _typeCde;

    /**
     * Field _natList
     */
    private java.util.Vector _natList;

    /**
     * Field _ctryResList
     */
    private java.util.Vector _ctryResList;

    /**
     * Field _ctryCorrList
     */
    private java.util.Vector _ctryCorrList;


      //----------------/
     //- Constructors -/
    //----------------/

    public RsrCtryInfo() 
     {
        super();
        _natList = new Vector();
        _ctryResList = new Vector();
        _ctryCorrList = new Vector();
    } //-- com.dummy.hfi.batch.object.castor.RsrCtryInfo()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addCtryCorr
     * 
     * 
     * 
     * @param vCtryCorr
     */
    public void addCtryCorr(com.dummy.wpc.batch.object.castor.CtryCorr vCtryCorr)
        throws java.lang.IndexOutOfBoundsException
    {
        _ctryCorrList.addElement(vCtryCorr);
    } //-- void addCtryCorr(com.dummy.hfi.batch.object.castor.CtryCorr) 

    /**
     * Method addCtryCorr
     * 
     * 
     * 
     * @param index
     * @param vCtryCorr
     */
    public void addCtryCorr(int index, com.dummy.wpc.batch.object.castor.CtryCorr vCtryCorr)
        throws java.lang.IndexOutOfBoundsException
    {
        _ctryCorrList.insertElementAt(vCtryCorr, index);
    } //-- void addCtryCorr(int, com.dummy.hfi.batch.object.castor.CtryCorr) 

    /**
     * Method addCtryRes
     * 
     * 
     * 
     * @param vCtryRes
     */
    public void addCtryRes(com.dummy.wpc.batch.object.castor.CtryRes vCtryRes)
        throws java.lang.IndexOutOfBoundsException
    {
        _ctryResList.addElement(vCtryRes);
    } //-- void addCtryRes(com.dummy.hfi.batch.object.castor.CtryRes) 

    /**
     * Method addCtryRes
     * 
     * 
     * 
     * @param index
     * @param vCtryRes
     */
    public void addCtryRes(int index, com.dummy.wpc.batch.object.castor.CtryRes vCtryRes)
        throws java.lang.IndexOutOfBoundsException
    {
        _ctryResList.insertElementAt(vCtryRes, index);
    } //-- void addCtryRes(int, com.dummy.hfi.batch.object.castor.CtryRes) 

    /**
     * Method addNat
     * 
     * 
     * 
     * @param vNat
     */
    public void addNat(com.dummy.wpc.batch.object.castor.Nat vNat)
        throws java.lang.IndexOutOfBoundsException
    {
        _natList.addElement(vNat);
    } //-- void addNat(com.dummy.hfi.batch.object.castor.Nat) 

    /**
     * Method addNat
     * 
     * 
     * 
     * @param index
     * @param vNat
     */
    public void addNat(int index, com.dummy.wpc.batch.object.castor.Nat vNat)
        throws java.lang.IndexOutOfBoundsException
    {
        _natList.insertElementAt(vNat, index);
    } //-- void addNat(int, com.dummy.hfi.batch.object.castor.Nat) 

    /**
     * Method enumerateCtryCorr
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateCtryCorr()
    {
        return _ctryCorrList.elements();
    } //-- java.util.Enumeration enumerateCtryCorr() 

    /**
     * Method enumerateCtryRes
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateCtryRes()
    {
        return _ctryResList.elements();
    } //-- java.util.Enumeration enumerateCtryRes() 

    /**
     * Method enumerateNat
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateNat()
    {
        return _natList.elements();
    } //-- java.util.Enumeration enumerateNat() 

    /**
     * Method getCtryCorr
     * 
     * 
     * 
     * @param index
     * @return CtryCorr
     */
    public com.dummy.wpc.batch.object.castor.CtryCorr getCtryCorr(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _ctryCorrList.size())) {
            throw new IndexOutOfBoundsException("getCtryCorr: Index value '"+index+"' not in range [0.."+_ctryCorrList.size()+ "]");
        }
        
        return (com.dummy.wpc.batch.object.castor.CtryCorr) _ctryCorrList.elementAt(index);
    } //-- com.dummy.hfi.batch.object.castor.CtryCorr getCtryCorr(int) 

    /**
     * Method getCtryCorr
     * 
     * 
     * 
     * @return CtryCorr
     */
    public com.dummy.wpc.batch.object.castor.CtryCorr[] getCtryCorr()
    {
        int size = _ctryCorrList.size();
        com.dummy.wpc.batch.object.castor.CtryCorr[] mArray = new com.dummy.wpc.batch.object.castor.CtryCorr[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.dummy.wpc.batch.object.castor.CtryCorr) _ctryCorrList.elementAt(index);
        }
        return mArray;
    } //-- com.dummy.hfi.batch.object.castor.CtryCorr[] getCtryCorr() 

    /**
     * Method getCtryCorrCount
     * 
     * 
     * 
     * @return int
     */
    public int getCtryCorrCount()
    {
        return _ctryCorrList.size();
    } //-- int getCtryCorrCount() 

    /**
     * Method getCtryRes
     * 
     * 
     * 
     * @param index
     * @return CtryRes
     */
    public com.dummy.wpc.batch.object.castor.CtryRes getCtryRes(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _ctryResList.size())) {
            throw new IndexOutOfBoundsException("getCtryRes: Index value '"+index+"' not in range [0.."+_ctryResList.size()+ "]");
        }
        
        return (com.dummy.wpc.batch.object.castor.CtryRes) _ctryResList.elementAt(index);
    } //-- com.dummy.hfi.batch.object.castor.CtryRes getCtryRes(int) 

    /**
     * Method getCtryRes
     * 
     * 
     * 
     * @return CtryRes
     */
    public com.dummy.wpc.batch.object.castor.CtryRes[] getCtryRes()
    {
        int size = _ctryResList.size();
        com.dummy.wpc.batch.object.castor.CtryRes[] mArray = new com.dummy.wpc.batch.object.castor.CtryRes[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.dummy.wpc.batch.object.castor.CtryRes) _ctryResList.elementAt(index);
        }
        return mArray;
    } //-- com.dummy.hfi.batch.object.castor.CtryRes[] getCtryRes() 

    /**
     * Method getCtryResCount
     * 
     * 
     * 
     * @return int
     */
    public int getCtryResCount()
    {
        return _ctryResList.size();
    } //-- int getCtryResCount() 

    /**
     * Method getNat
     * 
     * 
     * 
     * @param index
     * @return Nat
     */
    public com.dummy.wpc.batch.object.castor.Nat getNat(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _natList.size())) {
            throw new IndexOutOfBoundsException("getNat: Index value '"+index+"' not in range [0.."+_natList.size()+ "]");
        }
        
        return (com.dummy.wpc.batch.object.castor.Nat) _natList.elementAt(index);
    } //-- com.dummy.hfi.batch.object.castor.Nat getNat(int) 

    /**
     * Method getNat
     * 
     * 
     * 
     * @return Nat
     */
    public com.dummy.wpc.batch.object.castor.Nat[] getNat()
    {
        int size = _natList.size();
        com.dummy.wpc.batch.object.castor.Nat[] mArray = new com.dummy.wpc.batch.object.castor.Nat[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.dummy.wpc.batch.object.castor.Nat) _natList.elementAt(index);
        }
        return mArray;
    } //-- com.dummy.hfi.batch.object.castor.Nat[] getNat() 

    /**
     * Method getNatCount
     * 
     * 
     * 
     * @return int
     */
    public int getNatCount()
    {
        return _natList.size();
    } //-- int getNatCount() 

    /**
     * Returns the value of field 'typeCde'.
     * 
     * @return String
     * @return the value of field 'typeCde'.
     */
    public java.lang.String getTypeCde()
    {
        return this._typeCde;
    } //-- java.lang.String getTypeCde() 

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
     * Method removeAllCtryCorr
     * 
     */
    public void removeAllCtryCorr()
    {
        _ctryCorrList.removeAllElements();
    } //-- void removeAllCtryCorr() 

    /**
     * Method removeAllCtryRes
     * 
     */
    public void removeAllCtryRes()
    {
        _ctryResList.removeAllElements();
    } //-- void removeAllCtryRes() 

    /**
     * Method removeAllNat
     * 
     */
    public void removeAllNat()
    {
        _natList.removeAllElements();
    } //-- void removeAllNat() 

    /**
     * Method removeCtryCorr
     * 
     * 
     * 
     * @param index
     * @return CtryCorr
     */
    public com.dummy.wpc.batch.object.castor.CtryCorr removeCtryCorr(int index)
    {
        java.lang.Object obj = _ctryCorrList.elementAt(index);
        _ctryCorrList.removeElementAt(index);
        return (com.dummy.wpc.batch.object.castor.CtryCorr) obj;
    } //-- com.dummy.hfi.batch.object.castor.CtryCorr removeCtryCorr(int) 

    /**
     * Method removeCtryRes
     * 
     * 
     * 
     * @param index
     * @return CtryRes
     */
    public com.dummy.wpc.batch.object.castor.CtryRes removeCtryRes(int index)
    {
        java.lang.Object obj = _ctryResList.elementAt(index);
        _ctryResList.removeElementAt(index);
        return (com.dummy.wpc.batch.object.castor.CtryRes) obj;
    } //-- com.dummy.hfi.batch.object.castor.CtryRes removeCtryRes(int) 

    /**
     * Method removeNat
     * 
     * 
     * 
     * @param index
     * @return Nat
     */
    public com.dummy.wpc.batch.object.castor.Nat removeNat(int index)
    {
        java.lang.Object obj = _natList.elementAt(index);
        _natList.removeElementAt(index);
        return (com.dummy.wpc.batch.object.castor.Nat) obj;
    } //-- com.dummy.hfi.batch.object.castor.Nat removeNat(int) 

    /**
     * Method setCtryCorr
     * 
     * 
     * 
     * @param index
     * @param vCtryCorr
     */
    public void setCtryCorr(int index, com.dummy.wpc.batch.object.castor.CtryCorr vCtryCorr)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _ctryCorrList.size())) {
            throw new IndexOutOfBoundsException("setCtryCorr: Index value '"+index+"' not in range [0.."+_ctryCorrList.size()+ "]");
        }
        _ctryCorrList.setElementAt(vCtryCorr, index);
    } //-- void setCtryCorr(int, com.dummy.hfi.batch.object.castor.CtryCorr) 

    /**
     * Method setCtryCorr
     * 
     * 
     * 
     * @param ctryCorrArray
     */
    public void setCtryCorr(com.dummy.wpc.batch.object.castor.CtryCorr[] ctryCorrArray)
    {
        //-- copy array
        _ctryCorrList.removeAllElements();
        for (int i = 0; i < ctryCorrArray.length; i++) {
            _ctryCorrList.addElement(ctryCorrArray[i]);
        }
    } //-- void setCtryCorr(com.dummy.hfi.batch.object.castor.CtryCorr) 

    /**
     * Method setCtryRes
     * 
     * 
     * 
     * @param index
     * @param vCtryRes
     */
    public void setCtryRes(int index, com.dummy.wpc.batch.object.castor.CtryRes vCtryRes)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _ctryResList.size())) {
            throw new IndexOutOfBoundsException("setCtryRes: Index value '"+index+"' not in range [0.."+_ctryResList.size()+ "]");
        }
        _ctryResList.setElementAt(vCtryRes, index);
    } //-- void setCtryRes(int, com.dummy.hfi.batch.object.castor.CtryRes) 

    /**
     * Method setCtryRes
     * 
     * 
     * 
     * @param ctryResArray
     */
    public void setCtryRes(com.dummy.wpc.batch.object.castor.CtryRes[] ctryResArray)
    {
        //-- copy array
        _ctryResList.removeAllElements();
        for (int i = 0; i < ctryResArray.length; i++) {
            _ctryResList.addElement(ctryResArray[i]);
        }
    } //-- void setCtryRes(com.dummy.hfi.batch.object.castor.CtryRes) 

    /**
     * Method setNat
     * 
     * 
     * 
     * @param index
     * @param vNat
     */
    public void setNat(int index, com.dummy.wpc.batch.object.castor.Nat vNat)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _natList.size())) {
            throw new IndexOutOfBoundsException("setNat: Index value '"+index+"' not in range [0.."+_natList.size()+ "]");
        }
        _natList.setElementAt(vNat, index);
    } //-- void setNat(int, com.dummy.hfi.batch.object.castor.Nat) 

    /**
     * Method setNat
     * 
     * 
     * 
     * @param natArray
     */
    public void setNat(com.dummy.wpc.batch.object.castor.Nat[] natArray)
    {
        //-- copy array
        _natList.removeAllElements();
        for (int i = 0; i < natArray.length; i++) {
            _natList.addElement(natArray[i]);
        }
    } //-- void setNat(com.dummy.hfi.batch.object.castor.Nat) 

    /**
     * Sets the value of field 'typeCde'.
     * 
     * @param typeCde the value of field 'typeCde'.
     */
    public void setTypeCde(java.lang.String typeCde)
    {
        this._typeCde = typeCde;
    } //-- void setTypeCde(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return RsrCtryInfo
     */
    public static com.dummy.wpc.batch.object.castor.RsrCtryInfo unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.dummy.wpc.batch.object.castor.RsrCtryInfo) Unmarshaller.unmarshal(com.dummy.wpc.batch.object.castor.RsrCtryInfo.class, reader);
    } //-- com.dummy.hfi.batch.object.castor.RsrCtryInfo unmarshal(java.io.Reader) 

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
