/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/datadaptor/constant/GSMRate.java 1.1 2012/07/11 19:22:01CST 43588220 Development  $
 */

package com.dummy.wpc.datadaptor.constant;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class GSMRate.
 * 
 * @version $Revision: 1.1 $ $Date: 2012/07/11 19:22:01CST $
 */
public class GSMRate implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _precsnCnt
     */
    private java.lang.String _precsnCnt;

    /**
     * Field _amt
     */
    private java.lang.String _amt;

    /**
     * Field _signInd
     */
    private java.lang.String _signInd;


      //----------------/
     //- Constructors -/
    //----------------/

    public GSMRate() {
        super();
    } //-- com.dummy.hfi.gsm.GSMRate()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'amt'.
     * 
     * @return the value of field 'amt'.
     */
    public java.lang.String getAmt()
    {
        return this._amt;
    } //-- java.lang.String getAmt() 

    /**
     * Returns the value of field 'precsnCnt'.
     * 
     * @return the value of field 'precsnCnt'.
     */
    public java.lang.String getPrecsnCnt()
    {
        return this._precsnCnt;
    } //-- java.lang.String getPrecsnCnt() 

    /**
     * Returns the value of field 'signInd'.
     * 
     * @return the value of field 'signInd'.
     */
    public java.lang.String getSignInd()
    {
        return this._signInd;
    } //-- java.lang.String getSignInd() 

    /**
     * Method isValid
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
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'amt'.
     * 
     * @param amt the value of field 'amt'.
     */
    public void setAmt(java.lang.String amt)
    {
        this._amt = amt;
    } //-- void setAmt(java.lang.String) 

    /**
     * Sets the value of field 'precsnCnt'.
     * 
     * @param precsnCnt the value of field 'precsnCnt'.
     */
    public void setPrecsnCnt(java.lang.String precsnCnt)
    {
        this._precsnCnt = precsnCnt;
    } //-- void setPrecsnCnt(java.lang.String) 

    /**
     * Sets the value of field 'signInd'.
     * 
     * @param signInd the value of field 'signInd'.
     */
    public void setSignInd(java.lang.String signInd)
    {
        this._signInd = signInd;
    } //-- void setSignInd(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (GSMRate) Unmarshaller.unmarshal(GSMRate.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
