package com.nazdaq.organogram.util;

public interface Constants {
	//status
	public static final String ACTIVE = "1"; 
	public static final String INACTIVE = "0";
	
	//SMS & Email
	public static final String encoding = "UTF-8";
    public static final String ampersand = "&";
    public static final String equal = "=";
    public static final String APPROVAL = "APPROVAL";
	public static final String REJECT = "REJECT";
	public static final String NOTIFICATION = "NOTIFICATION";
	
	//role
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
	
	//System User
	public static final String SYSTEM_USER = "System";
	
	//Company Keyword
	public static final String ACCOUNTS = "ACCOUNTS";
	public static final String SALES = "SALES";
	public static final String GOVT_SALES = "GOVT_SALES";
	public static final String PRIVATE_SALES = "PRIVATE_SALES";
	public static final String PROCUREMENT = "PROCUREMENT";
	public static final String CUSTOMER_SERVICE = "CUSTOMER_SERVICE";
	public static final String TENDER = "TENDER";
	public static final String TENDER_PRE = "TENDER_PRE";
	public static final String HR_ADMIN = "HR_ADMIN";
	public static final String ASSETS = "ASSETS";
	public static final String PRODUCT_SOURCING = "PRODUCT_SOURCING";
	public static final String STORE_LOGISTIC = "STORE_LOGISTIC";
	public static final String IMPORT_EXPORT = "IMPORT_EXPORT";
	public static final String VAT_TAX = "VAT_TAX";
	public static final String OPERATION = "OPERATION";
	public static final String PROJECT_IMPL = "PROJECT_IMPL";
	public static final String 	ADMIN = "ADMIN";	
	public static final String HR = "HR";	
	
	public static enum BUSINESS_TYPE {
	    TRADE,
	    EMERGING
	}
	
}
