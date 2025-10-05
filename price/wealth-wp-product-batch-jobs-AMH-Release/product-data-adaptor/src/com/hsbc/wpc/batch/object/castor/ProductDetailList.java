/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/object/castor/ProductDetailList.java 1.1 2011/06/15 16:20:04CST Perry Guo (WMDHKG0007) Development  $
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
 * Class ProductDetailList.
 * 
 * @version $Revision: 1.1 $ $Date: 2011/06/15 16:20:04CST $
 */
public class ProductDetailList implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _size
     */
    private java.lang.String _size;

    /**
     * Field _lastUpdate
     */
    private java.lang.String _lastUpdate;

    /**
     * Field _productEntityList
     */
    private java.util.Vector _productEntityList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProductDetailList() 
     {
        super();
        _productEntityList = new Vector();
    } //-- com.dummy.hfi.batch.object.castor.ProductDetailList()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addProductEntity
     * 
     * 
     * 
     * @param vProductEntity
     */
    public void addProductEntity(com.dummy.wpc.batch.object.castor.ProductEntity vProductEntity)
        throws java.lang.IndexOutOfBoundsException
    {
        _productEntityList.addElement(vProductEntity);
    } //-- void addProductEntity(com.dummy.hfi.batch.object.castor.ProductEntity) 

    /**
     * Method addProductEntity
     * 
     * 
     * 
     * @param index
     * @param vProductEntity
     */
    public void addProductEntity(int index, com.dummy.wpc.batch.object.castor.ProductEntity vProductEntity)
        throws java.lang.IndexOutOfBoundsException
    {
        _productEntityList.insertElementAt(vProductEntity, index);
    } //-- void addProductEntity(int, com.dummy.hfi.batch.object.castor.ProductEntity) 

    /**
     * Method enumerateProductEntity
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateProductEntity()
    {
        return _productEntityList.elements();
    } //-- java.util.Enumeration enumerateProductEntity() 

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
     * Method getProductEntity
     * 
     * 
     * 
     * @param index
     * @return ProductEntity
     */
    public com.dummy.wpc.batch.object.castor.ProductEntity getProductEntity(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _productEntityList.size())) {
            throw new IndexOutOfBoundsException("getProductEntity: Index value '"+index+"' not in range [0.."+_productEntityList.size()+ "]");
        }
        
        return (com.dummy.wpc.batch.object.castor.ProductEntity) _productEntityList.elementAt(index);
    } //-- com.dummy.hfi.batch.object.castor.ProductEntity getProductEntity(int) 

    /**
     * Method getProductEntity
     * 
     * 
     * 
     * @return ProductEntity
     */
    public com.dummy.wpc.batch.object.castor.ProductEntity[] getProductEntity()
    {
        int size = _productEntityList.size();
        com.dummy.wpc.batch.object.castor.ProductEntity[] mArray = new com.dummy.wpc.batch.object.castor.ProductEntity[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.dummy.wpc.batch.object.castor.ProductEntity) _productEntityList.elementAt(index);
        }
        return mArray;
    } //-- com.dummy.hfi.batch.object.castor.ProductEntity[] getProductEntity() 

    /**
     * Method getProductEntityCount
     * 
     * 
     * 
     * @return int
     */
    public int getProductEntityCount()
    {
        return _productEntityList.size();
    } //-- int getProductEntityCount() 

    /**
     * Returns the value of field 'size'.
     * 
     * @return String
     * @return the value of field 'size'.
     */
    public java.lang.String getSize()
    {
        return this._size;
    } //-- java.lang.String getSize() 

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
     * Method removeAllProductEntity
     * 
     */
    public void removeAllProductEntity()
    {
        _productEntityList.removeAllElements();
    } //-- void removeAllProductEntity() 

    /**
     * Method removeProductEntity
     * 
     * 
     * 
     * @param index
     * @return ProductEntity
     */
    public com.dummy.wpc.batch.object.castor.ProductEntity removeProductEntity(int index)
    {
        java.lang.Object obj = _productEntityList.elementAt(index);
        _productEntityList.removeElementAt(index);
        return (com.dummy.wpc.batch.object.castor.ProductEntity) obj;
    } //-- com.dummy.hfi.batch.object.castor.ProductEntity removeProductEntity(int) 

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
     * Method setProductEntity
     * 
     * 
     * 
     * @param index
     * @param vProductEntity
     */
    public void setProductEntity(int index, com.dummy.wpc.batch.object.castor.ProductEntity vProductEntity)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _productEntityList.size())) {
            throw new IndexOutOfBoundsException("setProductEntity: Index value '"+index+"' not in range [0.."+_productEntityList.size()+ "]");
        }
        _productEntityList.setElementAt(vProductEntity, index);
    } //-- void setProductEntity(int, com.dummy.hfi.batch.object.castor.ProductEntity) 

    /**
     * Method setProductEntity
     * 
     * 
     * 
     * @param productEntityArray
     */
    public void setProductEntity(com.dummy.wpc.batch.object.castor.ProductEntity[] productEntityArray)
    {
        //-- copy array
        _productEntityList.removeAllElements();
        for (int i = 0; i < productEntityArray.length; i++) {
            _productEntityList.addElement(productEntityArray[i]);
        }
    } //-- void setProductEntity(com.dummy.hfi.batch.object.castor.ProductEntity) 

    /**
     * Sets the value of field 'size'.
     * 
     * @param size the value of field 'size'.
     */
    public void setSize(java.lang.String size)
    {
        this._size = size;
    } //-- void setSize(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return ProductDetailList
     */
    public static com.dummy.wpc.batch.object.castor.ProductDetailList unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.dummy.wpc.batch.object.castor.ProductDetailList) Unmarshaller.unmarshal(com.dummy.wpc.batch.object.castor.ProductDetailList.class, reader);
    } //-- com.dummy.hfi.batch.object.castor.ProductDetailList unmarshal(java.io.Reader) 

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
