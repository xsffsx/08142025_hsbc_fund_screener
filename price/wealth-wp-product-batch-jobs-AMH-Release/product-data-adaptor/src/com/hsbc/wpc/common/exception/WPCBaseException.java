/*
 */
package com.dummy.wpc.common.exception;

/**
 * <p><b>
 * Insert description of the classes responsibility/role. NOT what uses it.
 * </b></p>
 */
public class WPCBaseException extends Exception {
	
	private final static String LineSeparator = "\n";
	private final static String Separator = ".";
	
	private String in_class;
	private String in_methodName;

    
	public WPCBaseException() {
        super();
    }
	
    public WPCBaseException(String s) {
        super(s);
    }
    
    public WPCBaseException(String arg1, Throwable arg2) {
        super(arg1, arg2);
    }
	
    /**
     * Constructor.
     * @param in_class the class the error occurs
     * @param in_methodName the method the error occurs
     * @param in_errorMessage the error message
     */
    public WPCBaseException(Class in_class,
        String in_methodName,
        String in_errorMessage) {
    	
    	super(conbineString(in_class, in_methodName, in_errorMessage));
    	
    	this.setClassName(in_class.getName());
    	this.setMethodName(in_methodName);
    	
        
    }
    
    public WPCBaseException(Class in_class,
            String in_methodName,
            String in_errorMessage,
            Throwable e) {
        	
        	super(conbineString(in_class, in_methodName, in_errorMessage), e);
        	
        	this.setClassName(in_class.getName());
        	this.setMethodName(in_methodName);
        	
            
        }
    
    private static String conbineString(Class in_class, String in_methodName, String in_errorMessage){
    	
    	StringBuffer stringBuffer = new StringBuffer();
    	
    	stringBuffer.append("exception in ");
    	
    	if (in_class != null && in_methodName != null) {
    		stringBuffer.append(in_class.getName()).append(Separator).append(in_methodName).append(LineSeparator);
    	}
    	
    	if (in_errorMessage != null){
    		stringBuffer.append(in_errorMessage);
    	}
    	
    	return stringBuffer.toString();
    }

	/**
	 * @return the in_class
	 */
	public String getClassName() {
		return in_class;
	}

	/**
	 * @param in_class the in_class to set
	 */
	public void setClassName(String in_class) {
		this.in_class = in_class;
	}

	/**
	 * @return the in_methodName
	 */
	public String getMethodName() {
		return in_methodName;
	}

	/**
	 * @param in_methodName the in_methodName to set
	 */
	public void setMethodName(String in_methodName) {
		this.in_methodName = in_methodName;
	}
	

}
