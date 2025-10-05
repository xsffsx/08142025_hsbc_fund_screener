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
 * Class EltTypeEnumType.
 * 
 * @version $Revision$ $Date$
 */
public class EltTypeEnumType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The unknown type
     */
    public static final int UNKNOWN_TYPE = 0;

    /**
     * The instance of the unknown type
     */
    public static final EltTypeEnumType UNKNOWN = new EltTypeEnumType(UNKNOWN_TYPE, "unknown");

    /**
     * The bool type
     */
    public static final int BOOL_TYPE = 1;

    /**
     * The instance of the bool type
     */
    public static final EltTypeEnumType BOOL = new EltTypeEnumType(BOOL_TYPE, "bool");

    /**
     * The date type
     */
    public static final int DATE_TYPE = 2;

    /**
     * The instance of the date type
     */
    public static final EltTypeEnumType DATE = new EltTypeEnumType(DATE_TYPE, "date");

    /**
     * The long type
     */
    public static final int LONG_TYPE = 3;

    /**
     * The instance of the long type
     */
    public static final EltTypeEnumType LONG = new EltTypeEnumType(LONG_TYPE, "long");

    /**
     * The double type
     */
    public static final int DOUBLE_TYPE = 4;

    /**
     * The instance of the double type
     */
    public static final EltTypeEnumType DOUBLE = new EltTypeEnumType(DOUBLE_TYPE, "double");

    /**
     * The string type
     */
    public static final int STRING_TYPE = 5;

    /**
     * The instance of the string type
     */
    public static final EltTypeEnumType STRING = new EltTypeEnumType(STRING_TYPE, "string");

    /**
     * The table type
     */
    public static final int TABLE_TYPE = 6;

    /**
     * The instance of the table type
     */
    public static final EltTypeEnumType TABLE = new EltTypeEnumType(TABLE_TYPE, "table");

    /**
     * The perUnderlyingTable type
     */
    public static final int PERUNDERLYINGTABLE_TYPE = 7;

    /**
     * The instance of the perUnderlyingTable type
     */
    public static final EltTypeEnumType PERUNDERLYINGTABLE = new EltTypeEnumType(PERUNDERLYINGTABLE_TYPE, "perUnderlyingTable");

    /**
     * The formula type
     */
    public static final int FORMULA_TYPE = 8;

    /**
     * The instance of the formula type
     */
    public static final EltTypeEnumType FORMULA = new EltTypeEnumType(FORMULA_TYPE, "formula");

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

    private EltTypeEnumType(final int type, final java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerate.Returns an enumeration of all possible
     * instances of EltTypeEnumType
     * 
     * @return an Enumeration over all possible instances of
     * EltTypeEnumType
     */
    public static java.util.Enumeration enumerate(
    ) {
        return _memberTable.elements();
    }

    /**
     * Method getType.Returns the type of this EltTypeEnumType
     * 
     * @return the type of this EltTypeEnumType
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
        members.put("unknown", UNKNOWN);
        members.put("bool", BOOL);
        members.put("date", DATE);
        members.put("long", LONG);
        members.put("double", DOUBLE);
        members.put("string", STRING);
        members.put("table", TABLE);
        members.put("perUnderlyingTable", PERUNDERLYINGTABLE);
        members.put("formula", FORMULA);
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
     * EltTypeEnumType
     * 
     * @return the String representation of this EltTypeEnumType
     */
    public java.lang.String toString(
    ) {
        return this.stringValue;
    }

    /**
     * Method valueOf.Returns a new EltTypeEnumType based on the
     * given String value.
     * 
     * @param string
     * @return the EltTypeEnumType value of parameter 'string'
     */
    public static com.dummy.wpc.batch.extSP.types.EltTypeEnumType valueOf(
            final java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = _memberTable.get(string);
        }
        if (obj == null) {
            String err = "" + string + " is not a valid EltTypeEnumType";
            throw new IllegalArgumentException(err);
        }
        return (EltTypeEnumType) obj;
    }

}
