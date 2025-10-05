/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2006 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Class Name		FinDocMapInput
 *
 * Creation Date	Mar 16, 2006
 *
 * Amendment History   (In chronological sequence):
 *
 *    Amendment Date	Mar 16, 2006
 *    CMM/PPCR No.		
 *    Programmer		Andy Man
 *    Description
 * 
 */
package com.dummy.wpb.product.model;

import lombok.ToString;

@ToString
public class FinDocMapInput {
    private String actnCde=null;
    private String ctryCde =null;
    private String orgnCde =null;
    private String prodTypeCde =null;
    private String prodSubtpCde =null;
    private String prodId = null;
    private String docTypeCdeP =null;
    private String langCatCdeP = null;
    private String docSource=null;
    private String docTypeCde =null;
    private String docSubtpCde =null;    
    private String docId =null;
    private String langCatCde = null;
    private String url=null;
    private String emailAdrRpyText =null;
    private String custClassCde = null;
    
    private String inputFileName = null;
    private int recNum;
    
    private Long docSerNum = null ; //corresponding RSRC_ITEM_ID_FIN_DOC in FIN_DOC table
    
    private long prodRealID = 0; //corresponding prod_id in PROD table
    

    /**
     * @return Returns the actnCde.
     */
    public String getActnCde() {
        return actnCde;
    }
    /**
     * @param actnCde The actnCde to set.
     */
    public void setActnCde(String actnCde) {
        this.actnCde = actnCde;
    }
    /**
     * @return Returns the ctryCde.
     */
    public String getCtryCde() {
        return ctryCde;
    }
    /**
     * @param ctryCde The ctryCde to set.
     */
    public void setCtryCde(String ctryCde) {
        this.ctryCde = ctryCde;
    }
    /**
     * @return Returns the docId.
     */
    public String getDocId() {
        return docId;
    }
    /**
     * @param docId The docId to set.
     */
    public void setDocId(String docId) {
        this.docId = docId;
    }
    /**
     * @return Returns the docSource.
     */
    public String getDocSource() {
        return docSource;
    }
    /**
     * @param docSource The docSource to set.
     */
    public void setDocSource(String docSource) {
        this.docSource = docSource;
    }
    /**
     * @return Returns the docSubtpCde.
     */
    public String getDocSubtpCde() {
        return docSubtpCde;
    }
    /**
     * @param docSubtpCde The docSubtpCde to set.
     */
    public void setDocSubtpCde(String docSubtpCde) {
        this.docSubtpCde = docSubtpCde;
    }
    /**
     * @return Returns the docTypeCde.
     */
    public String getDocTypeCde() {
        return docTypeCde;
    }
    /**
     * @param docTypeCde The docTypeCde to set.
     */
    public void setDocTypeCde(String docTypeCde) {
        this.docTypeCde = docTypeCde;
    }
    /**
     * @return Returns the docTypeCde_P.
     */
    public String getDocTypeCdeP() {
        return docTypeCdeP;
    }
    /**
     * @param docTypeCdeP The docTypeCde_P to set.
     */
    public void setDocTypeCdeP(String docTypeCdeP) {
        this.docTypeCdeP = docTypeCdeP;
    }
    /**
     * @return Returns the emailAdrRpyText.
     */
    public String getEmailAdrRpyText() {
        return emailAdrRpyText;
    }
    /**
     * @param emailAdrRpyText The emailAdrRpyText to set.
     */
    public void setEmailAdrRpyText(String emailAdrRpyText) {
        this.emailAdrRpyText = emailAdrRpyText;
    }
    /**
     * @return Returns the langCatCde.
     */
    public String getLangCatCde() {
        return langCatCde;
    }
    /**
     * @param langCatCde The langCatCde to set.
     */
    public void setLangCatCde(String langCatCde) {
        this.langCatCde = langCatCde;
    }
    /**
     * @return Returns the langCatCde_P.
     */
    public String getLangCatCdeP() {
        return langCatCdeP;
    }
    /**
     * @param langCatCdeP The langCatCde_P to set.
     */
    public void setLangCatCdeP(String langCatCdeP) {
        this.langCatCdeP = langCatCdeP;
    }
    /**
     * @return Returns the orgnCde.
     */
    public String getOrgnCde() {
        return orgnCde;
    }
    /**
     * @param orgnCde The orgnCde to set.
     */
    public void setOrgnCde(String orgnCde) {
        this.orgnCde = orgnCde;
    }
    /**
     * @return Returns the prodId.
     */
    public String getProdId() {
        return prodId;
    }
    /**
     * @param prodId The prodId to set.
     */
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }
   /**
    * @return Returns the prodSubtpCde.
    */
   public String getProdSubtpCde() {
       return prodSubtpCde;
   }
   /**
    * @param prodSubtpCde The prodSubtpCde to set.
    */
   public void setProdSubtpCde(String prodSubtpCde) {
       this.prodSubtpCde = prodSubtpCde;
   }
    /**
     * @return Returns the prodTypeCde.
     */
    public String getProdTypeCde() {
        return prodTypeCde;
    }
    /**
     * @param prodTypeCde The prodTypeCde to set.
     */
    public void setProdTypeCde(String prodTypeCde) {
        this.prodTypeCde = prodTypeCde;
    }
    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @return Returns the inputFileName.
     */
    public String getInputFileName() {
        return inputFileName;
    }
    /**
     * @param inputFileName The inputFileName to set.
     */
    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }
    
    /**
     * @return Returns the recNum.
     */
    public int getRecNum() {
        return recNum;
    }
    /**
     * @param recNum The recNum to set.
     */
    public void setRecNum(int recNum) {
        this.recNum = recNum;
    }

	/**
	 * @return the prodRealID
	 */
	public long getProdRealID() {
		return prodRealID;
	}
	/**
	 * @param prodRealID the prodRealID to set
	 */
	public void setProdRealID(long prodRealID) {
		this.prodRealID = prodRealID;
	}
	/**
	 * @return the docSerNum
	 */
	public Long getDocSerNum() {
		return docSerNum;
	}
	/**
	 * @param docSerNum the docSerNum to set
	 */
	public void setDocSerNum(Long docSerNum) {
		this.docSerNum = docSerNum;
	}
    /**
     * @return the custClassCde
     */
    public String getCustClassCde() {
        return this.custClassCde;
    }
    /**
     * @param custClassCde the custClassCde to set
     */
    public void setCustClassCde(String custClassCde) {
        this.custClassCde = custClassCde;
    }

}
