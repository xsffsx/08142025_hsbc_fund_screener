/*
 * This class was automatically generated with <a
 * href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML Schema. $Id:
 * src/com/dummy/wpc/batch/xml/descriptors/
 * MandatoryProvidentFundPerformanceSegmentDescriptor.java 1.3.1.1 2013/01/15
 * 10:33:33CST CHRIS CUI (43601081) Development $
 */

package com.dummy.wpc.batch.xml.descriptors;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import com.dummy.wpc.batch.xml.MandatoryProvidentFundPerformanceSegment;

/**
 * Class MandatoryProvidentFundPerformanceSegmentDescriptor.
 * 
 * @version $Revision: 1.3.1.1 $ $Date: 2013/01/15 10:33:33CST $
 */
public class MandatoryProvidentFundPerformanceSegmentDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public MandatoryProvidentFundPerformanceSegmentDescriptor() {
        super();
        this._xmlName = "mandatoryProvidentFundPerformanceSegment";
        this._elementDefinition = false;

        // -- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.mapping.FieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        // -- initialize attribute descriptors

        // -- initialize element descriptors

        // -- _perfmCumPct
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_perfmCumPct", "perfmCumPct",
            org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                return target.getPerfmCumPct();
            }

            public void setValue(final java.lang.Object object, final java.lang.Object value) throws IllegalStateException,
                IllegalArgumentException {
                try {
                    MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                    target.setPerfmCumPct((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        // -- validation code for: _perfmCumPct
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        // -- _perfm1moPct
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_perfm1moPct", "perfm1moPct",
            org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                return target.getPerfm1moPct();
            }

            public void setValue(final java.lang.Object object, final java.lang.Object value) throws IllegalStateException,
                IllegalArgumentException {
                try {
                    MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                    target.setPerfm1moPct((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        // -- validation code for: _perfm1moPct
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        // -- _perfm6moPct
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_perfm6moPct", "perfm6moPct",
            org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                return target.getPerfm6moPct();
            }

            public void setValue(final java.lang.Object object, final java.lang.Object value) throws IllegalStateException,
                IllegalArgumentException {
                try {
                    MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                    target.setPerfm6moPct((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        // -- validation code for: _perfm6moPct
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        // -- _perfm1yrPct
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_perfm1yrPct", "perfm1yrPct",
            org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                return target.getPerfm1yrPct();
            }

            public void setValue(final java.lang.Object object, final java.lang.Object value) throws IllegalStateException,
                IllegalArgumentException {
                try {
                    MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                    target.setPerfm1yrPct((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        // -- validation code for: _perfm1yrPct
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        // -- _perfm3yrPct
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_perfm3yrPct", "perfm3yrPct",
            org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                return target.getPerfm3yrPct();
            }

            public void setValue(final java.lang.Object object, final java.lang.Object value) throws IllegalStateException,
                IllegalArgumentException {
                try {
                    MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                    target.setPerfm3yrPct((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        // -- validation code for: _perfm3yrPct
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        // -- _perfm5yrPct
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_perfm5yrPct", "perfm5yrPct",
            org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                return target.getPerfm5yrPct();
            }

            public void setValue(final java.lang.Object object, final java.lang.Object value) throws IllegalStateException,
                IllegalArgumentException {
                try {
                    MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                    target.setPerfm5yrPct((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        // -- validation code for: _perfm5yrPct
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        // -- _perfmSinceLnchPct
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_perfmSinceLnchPct",
            "perfmSinceLnchPct", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                return target.getPerfmSinceLnchPct();
            }

            public void setValue(final java.lang.Object object, final java.lang.Object value) throws IllegalStateException,
                IllegalArgumentException {
                try {
                    MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                    target.setPerfmSinceLnchPct((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        // -- validation code for: _perfmSinceLnchPct
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);


        // -- _perfmEffDt
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_perfmEffDt", "perfmEffDt",
            org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                return target.getPerfmEffDt();
            }

            public void setValue(final java.lang.Object object, final java.lang.Object value) throws IllegalStateException,
                IllegalArgumentException {
                try {
                    MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                    target.setPerfmEffDt((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        // -- validation code for: _perfmEffDt
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        // -- _disIndc
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_disIndc", "disIndc",
            org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                return target.getDisIndc();
            }

            public void setValue(final java.lang.Object object, final java.lang.Object value) throws IllegalStateException,
                IllegalArgumentException {
                try {
                    MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                    target.setDisIndc((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setSchemaType("string");
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        // -- validation code for: _disIndc
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        // -- _recDtTmSeg
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.dummy.wpc.batch.xml.RecDtTmSeg.class, "_recDtTmSeg",
            "recDtTmSeg", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) throws IllegalStateException {
                MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                return target.getRecDtTmSeg();
            }

            public void setValue(final java.lang.Object object, final java.lang.Object value) throws IllegalStateException,
                IllegalArgumentException {
                try {
                    MandatoryProvidentFundPerformanceSegment target = (MandatoryProvidentFundPerformanceSegment) object;
                    target.setRecDtTmSeg((com.dummy.wpc.batch.xml.RecDtTmSeg) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new com.dummy.wpc.batch.xml.RecDtTmSeg();
            }
        };
        desc.setSchemaType("com.dummy.wpc.batch.xml.RecDtTmSeg");
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        addSequenceElement(desc);

        // -- validation code for: _recDtTmSeg
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
        return com.dummy.wpc.batch.xml.MandatoryProvidentFundPerformanceSegment.class;
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
