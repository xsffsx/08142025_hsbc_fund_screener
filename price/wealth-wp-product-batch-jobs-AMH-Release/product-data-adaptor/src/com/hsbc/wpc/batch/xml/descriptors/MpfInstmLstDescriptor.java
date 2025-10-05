/*
 * This class was automatically generated with <a
 * href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML Schema. $Id:
 * src/com/dummy/wpc/batch/xml/descriptors/MpfInstmLstDescriptor.java 1.3
 * 2011/12/06 19:08:12CST Navagate Developer 10 (WMDHKG0028) Development $
 */

package com.dummy.wpc.batch.xml.descriptors;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import com.dummy.wpc.batch.xml.MpfInstmLst;

/**
 * Class MpfInstmLstDescriptor.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:12CST $
 */
public class MpfInstmLstDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /**
     * Field _elementDefinition.
     */
    private boolean _elementDefinition;

    /**
     * Field _nsPrefix.
     */
    private java.lang.String _nsPrefix;

    /**
     * Field _nsURI.
     */
    private java.lang.String _nsURI;

    /**
     * Field _xmlName.
     */
    private java.lang.String _xmlName;

    /**
     * Field _identity.
     */
    private org.exolab.castor.xml.XMLFieldDescriptor _identity;


    // ----------------/
    // - Constructors -/
    // ----------------/

    public MpfInstmLstDescriptor() {
        super();
        this._xmlName = "mpfInstmLst";
        this._elementDefinition = true;

        // -- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.mapping.FieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        // -- initialize attribute descriptors

        // -- initialize element descriptors

        // -- _mpfInstmList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.dummy.wpc.batch.xml.UtTrstInstm.class, "_mpfInstmList",
            "mpfInstm", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                MpfInstmLst target = (MpfInstmLst) object;
                return target.getMpfInstm();
            }

            public void setValue(final java.lang.Object object, final java.lang.Object value) throws IllegalStateException,
                IllegalArgumentException {
                try {
                    MpfInstmLst target = (MpfInstmLst) object;
                    target.addMpfInstm((com.dummy.wpc.batch.xml.MpfInstm) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public void resetValue(final Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    MpfInstmLst target = (MpfInstmLst) object;
                    target.removeAllMpfInstm();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new com.dummy.wpc.batch.xml.MpfInstm();
            }
        };
        desc.setSchemaType("com.dummy.wpc.batch.xml.MpfInstm");
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        // -- validation code for: _mpfInstmList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
        }
        desc.setValidator(fieldValidator);
    }


    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method getAccessMode.
     * 
     * @return the access mode specified for this class.
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    }

    /**
     * Method getIdentity.
     * 
     * @return the identity field, null if this class has no identity.
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return this._identity;
    }

    /**
     * Method getJavaClass.
     * 
     * @return the Java class represented by this descriptor.
     */
    public java.lang.Class getJavaClass() {
        return com.dummy.wpc.batch.xml.MpfInstmLst.class;
    }

    /**
     * Method getNameSpacePrefix.
     * 
     * @return the namespace prefix to use when marshaling as XML.
     */
    public java.lang.String getNameSpacePrefix() {
        return this._nsPrefix;
    }

    /**
     * Method getNameSpaceURI.
     * 
     * @return the namespace URI used when marshaling and unmarshaling as XML.
     */
    public java.lang.String getNameSpaceURI() {
        return this._nsURI;
    }

    /**
     * Method getValidator.
     * 
     * @return a specific validator for the class described by this
     *         ClassDescriptor.
     */
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    }

    /**
     * Method getXMLName.
     * 
     * @return the XML Name for the Class being described.
     */
    public java.lang.String getXMLName() {
        return this._xmlName;
    }

    /**
     * Method isElementDefinition.
     * 
     * @return true if XML schema definition of this Class is that of a global
     *         element or element with anonymous type definition.
     */
    public boolean isElementDefinition() {
        return this._elementDefinition;
    }

}
