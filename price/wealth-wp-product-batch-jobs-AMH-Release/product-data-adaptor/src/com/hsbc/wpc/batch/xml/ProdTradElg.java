/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/ProdTradElg.java 1.3 2011/12/06 19:08:23CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProdTradElg.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:23CST $
 */
public class ProdTradElg implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prodKeySeg.
     */
    private com.dummy.wpc.batch.xml.ProdKeySeg _prodKeySeg;

    /**
     * Field _prodTraEligSeg.
     */
    private com.dummy.wpc.batch.xml.ProdTraEligSeg _prodTraEligSeg;

    /**
     * Field _custCateEligSegList.
     */
    private java.util.Vector _custCateEligSegList;

    /**
     * Field _custFormEligSegList.
     */
    private java.util.Vector _custFormEligSegList;

    /**
     * Field _invstHldChkCriterSegList.
     */
    private java.util.Vector _invstHldChkCriterSegList;

    /**
     * Field _prodRestCustCtrySegList.
     */
    private java.util.Vector _prodRestCustCtrySegList;

    /**
     * Field _recDtTmSeg.
     */
    private com.dummy.wpc.batch.xml.RecDtTmSeg _recDtTmSeg;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProdTradElg() {
        super();
        this._custCateEligSegList = new java.util.Vector();
        this._custFormEligSegList = new java.util.Vector();
        this._invstHldChkCriterSegList = new java.util.Vector();
        this._prodRestCustCtrySegList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vCustCateEligSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCustCateEligSeg(
            final com.dummy.wpc.batch.xml.CustCateEligSeg vCustCateEligSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._custCateEligSegList.addElement(vCustCateEligSeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vCustCateEligSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCustCateEligSeg(
            final int index,
            final com.dummy.wpc.batch.xml.CustCateEligSeg vCustCateEligSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._custCateEligSegList.add(index, vCustCateEligSeg);
    }

    /**
     * 
     * 
     * @param vCustFormEligSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCustFormEligSeg(
            final com.dummy.wpc.batch.xml.CustFormEligSeg vCustFormEligSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._custFormEligSegList.addElement(vCustFormEligSeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vCustFormEligSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCustFormEligSeg(
            final int index,
            final com.dummy.wpc.batch.xml.CustFormEligSeg vCustFormEligSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._custFormEligSegList.add(index, vCustFormEligSeg);
    }

    /**
     * 
     * 
     * @param vInvstHldChkCriterSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInvstHldChkCriterSeg(
            final com.dummy.wpc.batch.xml.InvstHldChkCriterSeg vInvstHldChkCriterSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._invstHldChkCriterSegList.addElement(vInvstHldChkCriterSeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vInvstHldChkCriterSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInvstHldChkCriterSeg(
            final int index,
            final com.dummy.wpc.batch.xml.InvstHldChkCriterSeg vInvstHldChkCriterSeg)
    throws java.lang.IndexOutOfBoundsException {
        this._invstHldChkCriterSegList.add(index, vInvstHldChkCriterSeg);
    }

    /**
     * 
     * 
     * @param vProdRestCustCtrySeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdRestCustCtrySeg(
            final com.dummy.wpc.batch.xml.ProdRestCustCtrySeg vProdRestCustCtrySeg)
    throws java.lang.IndexOutOfBoundsException {
        this._prodRestCustCtrySegList.addElement(vProdRestCustCtrySeg);
    }

    /**
     * 
     * 
     * @param index
     * @param vProdRestCustCtrySeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProdRestCustCtrySeg(
            final int index,
            final com.dummy.wpc.batch.xml.ProdRestCustCtrySeg vProdRestCustCtrySeg)
    throws java.lang.IndexOutOfBoundsException {
        this._prodRestCustCtrySegList.add(index, vProdRestCustCtrySeg);
    }

    /**
     * Method enumerateCustCateEligSeg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.CustCateEligSeg elements
     */
    public java.util.Enumeration enumerateCustCateEligSeg(
    ) {
        return this._custCateEligSegList.elements();
    }

    /**
     * Method enumerateCustFormEligSeg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.CustFormEligSeg elements
     */
    public java.util.Enumeration enumerateCustFormEligSeg(
    ) {
        return this._custFormEligSegList.elements();
    }

    /**
     * Method enumerateInvstHldChkCriterSeg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.InvstHldChkCriterSeg elements
     */
    public java.util.Enumeration enumerateInvstHldChkCriterSeg(
    ) {
        return this._invstHldChkCriterSegList.elements();
    }

    /**
     * Method enumerateProdRestCustCtrySeg.
     * 
     * @return an Enumeration over all
     * com.dummy.wpc.batch.xml.ProdRestCustCtrySeg elements
     */
    public java.util.Enumeration enumerateProdRestCustCtrySeg(
    ) {
        return this._prodRestCustCtrySegList.elements();
    }

    /**
     * Method getCustCateEligSeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.xml.CustCateEligSeg at the given index
     */
    public com.dummy.wpc.batch.xml.CustCateEligSeg getCustCateEligSeg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._custCateEligSegList.size()) {
            throw new IndexOutOfBoundsException("getCustCateEligSeg: Index value '" + index + "' not in range [0.." + (this._custCateEligSegList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.CustCateEligSeg) _custCateEligSegList.get(index);
    }

    /**
     * Method getCustCateEligSeg.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.CustCateEligSeg[] getCustCateEligSeg(
    ) {
        com.dummy.wpc.batch.xml.CustCateEligSeg[] array = new com.dummy.wpc.batch.xml.CustCateEligSeg[0];
        return (com.dummy.wpc.batch.xml.CustCateEligSeg[]) this._custCateEligSegList.toArray(array);
    }

    /**
     * Method getCustCateEligSegCount.
     * 
     * @return the size of this collection
     */
    public int getCustCateEligSegCount(
    ) {
        return this._custCateEligSegList.size();
    }

    /**
     * Method getCustFormEligSeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.xml.CustFormEligSeg at the given index
     */
    public com.dummy.wpc.batch.xml.CustFormEligSeg getCustFormEligSeg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._custFormEligSegList.size()) {
            throw new IndexOutOfBoundsException("getCustFormEligSeg: Index value '" + index + "' not in range [0.." + (this._custFormEligSegList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.CustFormEligSeg) _custFormEligSegList.get(index);
    }

    /**
     * Method getCustFormEligSeg.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.CustFormEligSeg[] getCustFormEligSeg(
    ) {
        com.dummy.wpc.batch.xml.CustFormEligSeg[] array = new com.dummy.wpc.batch.xml.CustFormEligSeg[0];
        return (com.dummy.wpc.batch.xml.CustFormEligSeg[]) this._custFormEligSegList.toArray(array);
    }

    /**
     * Method getCustFormEligSegCount.
     * 
     * @return the size of this collection
     */
    public int getCustFormEligSegCount(
    ) {
        return this._custFormEligSegList.size();
    }

    /**
     * Method getInvstHldChkCriterSeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.xml.InvstHldChkCriterSeg at the given inde
     */
    public com.dummy.wpc.batch.xml.InvstHldChkCriterSeg getInvstHldChkCriterSeg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._invstHldChkCriterSegList.size()) {
            throw new IndexOutOfBoundsException("getInvstHldChkCriterSeg: Index value '" + index + "' not in range [0.." + (this._invstHldChkCriterSegList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.InvstHldChkCriterSeg) _invstHldChkCriterSegList.get(index);
    }

    /**
     * Method getInvstHldChkCriterSeg.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.InvstHldChkCriterSeg[] getInvstHldChkCriterSeg(
    ) {
        com.dummy.wpc.batch.xml.InvstHldChkCriterSeg[] array = new com.dummy.wpc.batch.xml.InvstHldChkCriterSeg[0];
        return (com.dummy.wpc.batch.xml.InvstHldChkCriterSeg[]) this._invstHldChkCriterSegList.toArray(array);
    }

    /**
     * Method getInvstHldChkCriterSegCount.
     * 
     * @return the size of this collection
     */
    public int getInvstHldChkCriterSegCount(
    ) {
        return this._invstHldChkCriterSegList.size();
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
     * Method getProdRestCustCtrySeg.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * com.dummy.wpc.batch.xml.ProdRestCustCtrySeg at the given index
     */
    public com.dummy.wpc.batch.xml.ProdRestCustCtrySeg getProdRestCustCtrySeg(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodRestCustCtrySegList.size()) {
            throw new IndexOutOfBoundsException("getProdRestCustCtrySeg: Index value '" + index + "' not in range [0.." + (this._prodRestCustCtrySegList.size() - 1) + "]");
        }
        
        return (com.dummy.wpc.batch.xml.ProdRestCustCtrySeg) _prodRestCustCtrySegList.get(index);
    }

    /**
     * Method getProdRestCustCtrySeg.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public com.dummy.wpc.batch.xml.ProdRestCustCtrySeg[] getProdRestCustCtrySeg(
    ) {
        com.dummy.wpc.batch.xml.ProdRestCustCtrySeg[] array = new com.dummy.wpc.batch.xml.ProdRestCustCtrySeg[0];
        return (com.dummy.wpc.batch.xml.ProdRestCustCtrySeg[]) this._prodRestCustCtrySegList.toArray(array);
    }

    /**
     * Method getProdRestCustCtrySegCount.
     * 
     * @return the size of this collection
     */
    public int getProdRestCustCtrySegCount(
    ) {
        return this._prodRestCustCtrySegList.size();
    }

    /**
     * Returns the value of field 'prodTraEligSeg'.
     * 
     * @return the value of field 'ProdTraEligSeg'.
     */
    public com.dummy.wpc.batch.xml.ProdTraEligSeg getProdTraEligSeg(
    ) {
        return this._prodTraEligSeg;
    }

    /**
     * Returns the value of field 'recDtTmSeg'.
     * 
     * @return the value of field 'RecDtTmSeg'.
     */
    public com.dummy.wpc.batch.xml.RecDtTmSeg getRecDtTmSeg(
    ) {
        return this._recDtTmSeg;
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
    public void removeAllCustCateEligSeg(
    ) {
        this._custCateEligSegList.clear();
    }

    /**
     */
    public void removeAllCustFormEligSeg(
    ) {
        this._custFormEligSegList.clear();
    }

    /**
     */
    public void removeAllInvstHldChkCriterSeg(
    ) {
        this._invstHldChkCriterSegList.clear();
    }

    /**
     */
    public void removeAllProdRestCustCtrySeg(
    ) {
        this._prodRestCustCtrySegList.clear();
    }

    /**
     * Method removeCustCateEligSeg.
     * 
     * @param vCustCateEligSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeCustCateEligSeg(
            final com.dummy.wpc.batch.xml.CustCateEligSeg vCustCateEligSeg) {
        boolean removed = _custCateEligSegList.remove(vCustCateEligSeg);
        return removed;
    }

    /**
     * Method removeCustCateEligSegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.CustCateEligSeg removeCustCateEligSegAt(
            final int index) {
        java.lang.Object obj = this._custCateEligSegList.remove(index);
        return (com.dummy.wpc.batch.xml.CustCateEligSeg) obj;
    }

    /**
     * Method removeCustFormEligSeg.
     * 
     * @param vCustFormEligSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeCustFormEligSeg(
            final com.dummy.wpc.batch.xml.CustFormEligSeg vCustFormEligSeg) {
        boolean removed = _custFormEligSegList.remove(vCustFormEligSeg);
        return removed;
    }

    /**
     * Method removeCustFormEligSegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.CustFormEligSeg removeCustFormEligSegAt(
            final int index) {
        java.lang.Object obj = this._custFormEligSegList.remove(index);
        return (com.dummy.wpc.batch.xml.CustFormEligSeg) obj;
    }

    /**
     * Method removeInvstHldChkCriterSeg.
     * 
     * @param vInvstHldChkCriterSeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeInvstHldChkCriterSeg(
            final com.dummy.wpc.batch.xml.InvstHldChkCriterSeg vInvstHldChkCriterSeg) {
        boolean removed = _invstHldChkCriterSegList.remove(vInvstHldChkCriterSeg);
        return removed;
    }

    /**
     * Method removeInvstHldChkCriterSegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.InvstHldChkCriterSeg removeInvstHldChkCriterSegAt(
            final int index) {
        java.lang.Object obj = this._invstHldChkCriterSegList.remove(index);
        return (com.dummy.wpc.batch.xml.InvstHldChkCriterSeg) obj;
    }

    /**
     * Method removeProdRestCustCtrySeg.
     * 
     * @param vProdRestCustCtrySeg
     * @return true if the object was removed from the collection.
     */
    public boolean removeProdRestCustCtrySeg(
            final com.dummy.wpc.batch.xml.ProdRestCustCtrySeg vProdRestCustCtrySeg) {
        boolean removed = _prodRestCustCtrySegList.remove(vProdRestCustCtrySeg);
        return removed;
    }

    /**
     * Method removeProdRestCustCtrySegAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public com.dummy.wpc.batch.xml.ProdRestCustCtrySeg removeProdRestCustCtrySegAt(
            final int index) {
        java.lang.Object obj = this._prodRestCustCtrySegList.remove(index);
        return (com.dummy.wpc.batch.xml.ProdRestCustCtrySeg) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vCustCateEligSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setCustCateEligSeg(
            final int index,
            final com.dummy.wpc.batch.xml.CustCateEligSeg vCustCateEligSeg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._custCateEligSegList.size()) {
            throw new IndexOutOfBoundsException("setCustCateEligSeg: Index value '" + index + "' not in range [0.." + (this._custCateEligSegList.size() - 1) + "]");
        }
        
        this._custCateEligSegList.set(index, vCustCateEligSeg);
    }

    /**
     * 
     * 
     * @param vCustCateEligSegArray
     */
    public void setCustCateEligSeg(
            final com.dummy.wpc.batch.xml.CustCateEligSeg[] vCustCateEligSegArray) {
        //-- copy array
        _custCateEligSegList.clear();
        
        for (int i = 0; i < vCustCateEligSegArray.length; i++) {
                this._custCateEligSegList.add(vCustCateEligSegArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vCustFormEligSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setCustFormEligSeg(
            final int index,
            final com.dummy.wpc.batch.xml.CustFormEligSeg vCustFormEligSeg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._custFormEligSegList.size()) {
            throw new IndexOutOfBoundsException("setCustFormEligSeg: Index value '" + index + "' not in range [0.." + (this._custFormEligSegList.size() - 1) + "]");
        }
        
        this._custFormEligSegList.set(index, vCustFormEligSeg);
    }

    /**
     * 
     * 
     * @param vCustFormEligSegArray
     */
    public void setCustFormEligSeg(
            final com.dummy.wpc.batch.xml.CustFormEligSeg[] vCustFormEligSegArray) {
        //-- copy array
        _custFormEligSegList.clear();
        
        for (int i = 0; i < vCustFormEligSegArray.length; i++) {
                this._custFormEligSegList.add(vCustFormEligSegArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vInvstHldChkCriterSeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInvstHldChkCriterSeg(
            final int index,
            final com.dummy.wpc.batch.xml.InvstHldChkCriterSeg vInvstHldChkCriterSeg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._invstHldChkCriterSegList.size()) {
            throw new IndexOutOfBoundsException("setInvstHldChkCriterSeg: Index value '" + index + "' not in range [0.." + (this._invstHldChkCriterSegList.size() - 1) + "]");
        }
        
        this._invstHldChkCriterSegList.set(index, vInvstHldChkCriterSeg);
    }

    /**
     * 
     * 
     * @param vInvstHldChkCriterSegArray
     */
    public void setInvstHldChkCriterSeg(
            final com.dummy.wpc.batch.xml.InvstHldChkCriterSeg[] vInvstHldChkCriterSegArray) {
        //-- copy array
        _invstHldChkCriterSegList.clear();
        
        for (int i = 0; i < vInvstHldChkCriterSegArray.length; i++) {
                this._invstHldChkCriterSegList.add(vInvstHldChkCriterSegArray[i]);
        }
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
     * @param vProdRestCustCtrySeg
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setProdRestCustCtrySeg(
            final int index,
            final com.dummy.wpc.batch.xml.ProdRestCustCtrySeg vProdRestCustCtrySeg)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._prodRestCustCtrySegList.size()) {
            throw new IndexOutOfBoundsException("setProdRestCustCtrySeg: Index value '" + index + "' not in range [0.." + (this._prodRestCustCtrySegList.size() - 1) + "]");
        }
        
        this._prodRestCustCtrySegList.set(index, vProdRestCustCtrySeg);
    }

    /**
     * 
     * 
     * @param vProdRestCustCtrySegArray
     */
    public void setProdRestCustCtrySeg(
            final com.dummy.wpc.batch.xml.ProdRestCustCtrySeg[] vProdRestCustCtrySegArray) {
        //-- copy array
        _prodRestCustCtrySegList.clear();
        
        for (int i = 0; i < vProdRestCustCtrySegArray.length; i++) {
                this._prodRestCustCtrySegList.add(vProdRestCustCtrySegArray[i]);
        }
    }

    /**
     * Sets the value of field 'prodTraEligSeg'.
     * 
     * @param prodTraEligSeg the value of field 'prodTraEligSeg'.
     */
    public void setProdTraEligSeg(
            final com.dummy.wpc.batch.xml.ProdTraEligSeg prodTraEligSeg) {
        this._prodTraEligSeg = prodTraEligSeg;
    }

    /**
     * Sets the value of field 'recDtTmSeg'.
     * 
     * @param recDtTmSeg the value of field 'recDtTmSeg'.
     */
    public void setRecDtTmSeg(
            final com.dummy.wpc.batch.xml.RecDtTmSeg recDtTmSeg) {
        this._recDtTmSeg = recDtTmSeg;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled com.dummy.wpc.batch.xml.ProdTradElg
     */
    public static com.dummy.wpc.batch.xml.ProdTradElg unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.ProdTradElg) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.ProdTradElg.class, reader);
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
