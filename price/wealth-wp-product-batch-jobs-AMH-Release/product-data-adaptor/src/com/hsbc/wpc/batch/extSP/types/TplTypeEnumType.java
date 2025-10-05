/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.dummy.wpc.batch.extSP.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class TplTypeEnumType.
 * 
 * @version $Revision$ $Date$
 */
public class TplTypeEnumType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The mailED type
     */
    public static final int MAILED_TYPE = 0;

    /**
     * The instance of the mailED type
     */
    public static final TplTypeEnumType MAILED = new TplTypeEnumType(MAILED_TYPE, "mailED");

    /**
     * The streamED type
     */
    public static final int STREAMED_TYPE = 1;

    /**
     * The instance of the streamED type
     */
    public static final TplTypeEnumType STREAMED = new TplTypeEnumType(STREAMED_TYPE, "streamED");

    /**
     * The sophisED type
     */
    public static final int SOPHISED_TYPE = 2;

    /**
     * The instance of the sophisED type
     */
    public static final TplTypeEnumType SOPHISED = new TplTypeEnumType(SOPHISED_TYPE, "sophisED");

    /**
     * The pimsED type
     */
    public static final int PIMSED_TYPE = 3;

    /**
     * The instance of the pimsED type
     */
    public static final TplTypeEnumType PIMSED = new TplTypeEnumType(PIMSED_TYPE, "pimsED");

    /**
     * The ecomED type
     */
    public static final int ECOMED_TYPE = 4;

    /**
     * The instance of the ecomED type
     */
    public static final TplTypeEnumType ECOMED = new TplTypeEnumType(ECOMED_TYPE, "ecomED");

    /**
     * The termsheetED type
     */
    public static final int TERMSHEETED_TYPE = 5;

    /**
     * The instance of the termsheetED type
     */
    public static final TplTypeEnumType TERMSHEETED = new TplTypeEnumType(TERMSHEETED_TYPE, "termsheetED");

    /**
     * Field _memberTable.
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type.
     */
    private final int type;

    /**
     * Field stringValue.
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private TplTypeEnumType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of TplTypeEnumType
     * 
     * @return an Enumeration over all possible instances of
     * TplTypeEnumType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this TplTypeEnumType
     * 
     * @return the type of this TplTypeEnumType
     */
    public int getType(
    ) {
        return this.type;
    }

    /**
     * Method init.
     * 
     * @return the initialized Hashtable for the member table
     */
    private static java.util.Hashtable init(
    ) {
        Hashtable members = new Hashtable();
        members.put("mailED", MAILED);
        members.put("streamED", STREAMED);
        members.put("sophisED", SOPHISED);
        members.put("pimsED", PIMSED);
        members.put("ecomED", ECOMED);
        members.put("termsheetED", TERMSHEETED);
        return members;
    }

    /**
     * Method readResolve. will be called during deserialization to
     * replace the deserialized object with the correct constant
     * instance.
     * 
     * @return this deserialized object
     */
    private java.lang.Object readResolve(
    ) {
        return valueOf(this.stringValue);
    }

    /**
     * Method toString.Returns the String representation of this
     * TplTypeEnumType
     * 
     * @return the String representation of this TplTypeEnumType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new TplTypeEnumType based on the
     * given String value.
     * 
     * @param string
     * @return the TplTypeEnumType value of parameter 'string'
     */
    public static com.dummy.wpc.batch.extSP.types.TplTypeEnumType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid TplTypeEnumType";
            throw new IllegalArgumentException(err);
        }
        return (TplTypeEnumType) obj;
    }

}
