package com.sanguine.webpms.model;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class clsPMSGroupBookingDtlModel implements Serializable {
	private static final long serialVersionUID = 1L;

	public clsPMSGroupBookingDtlModel() {
	}

	// Variable Declaration
	private String strGroupCode;
	
	private String strPayee;

	private String strRoomPayee;

	private String strFandBPayee;

	private String strTelephonePayee;

	private String strExtraPayee;
	
	private String strUserCreated;
		
	private String strUserEdited;

	private String dteDateCreated;
	
	private String dteDateEdited;
	
	private String strClientCode;
	
	// Setter-Getter Methods
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public String getStrGroupCode() {
		return strGroupCode;
	}



	public void setStrGroupCode(String strGroupCode) {
		this.strGroupCode = strGroupCode;
	}



	public String getStrPayee() {
		return strPayee;
	}



	public void setStrPayee(String strPayee) {
		this.strPayee = strPayee;
	}



	public String getStrRoomPayee() {
		return strRoomPayee;
	}



	public void setStrRoomPayee(String strRoomPayee) {
		this.strRoomPayee = strRoomPayee;
	}



	public String getStrFandBPayee() {
		return strFandBPayee;
	}



	public void setStrFandBPayee(String strFandBPayee) {
		this.strFandBPayee = strFandBPayee;
	}



	public String getStrTelephonePayee() {
		return strTelephonePayee;
	}



	public void setStrTelephonePayee(String strTelephonePayee) {
		this.strTelephonePayee = strTelephonePayee;
	}



	public String getStrExtraPayee() {
		return strExtraPayee;
	}



	public void setStrExtraPayee(String strExtraPayee) {
		this.strExtraPayee = strExtraPayee;
	}



	public String getStrUserCreated() {
		return strUserCreated;
	}



	public void setStrUserCreated(String strUserCreated) {
		this.strUserCreated = strUserCreated;
	}



	public String getStrUserEdited() {
		return strUserEdited;
	}



	public void setStrUserEdited(String strUserEdited) {
		this.strUserEdited = strUserEdited;
	}



	public String getDteDateCreated() {
		return dteDateCreated;
	}



	public void setDteDateCreated(String dteDateCreated) {
		this.dteDateCreated = dteDateCreated;
	}



	public String getDteDateEdited() {
		return dteDateEdited;
	}



	public void setDteDateEdited(String dteDateEdited) {
		this.dteDateEdited = dteDateEdited;
	}



	public String getStrClientCode() {
		return strClientCode;
	}



	public void setStrClientCode(String strClientCode) {
		this.strClientCode = strClientCode;
	}



	// Function to Set Default Values
	private Object setDefaultValue(Object value, Object defaultValue) {
		if (value != null && (value instanceof String && value.toString().length() > 0)) {
			return value;
		} else if (value != null && (value instanceof Double && value.toString().length() > 0)) {
			return value;
		} else if (value != null && (value instanceof Integer && value.toString().length() > 0)) {
			return value;
		} else if (value != null && (value instanceof Long && value.toString().length() > 0)) {
			return value;
		} else {
			return defaultValue;
		}
	}

}
