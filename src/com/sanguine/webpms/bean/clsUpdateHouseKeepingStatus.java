package com.sanguine.webpms.bean;

import java.util.ArrayList;
import java.util.List;

public class clsUpdateHouseKeepingStatus {
	// Variable Declaration

	private String strRoomNo;
	private String strRoomFlag;
	private String strHouseKeepingCode;
	private String strHouseKeepingName;
	private String strHouseKeepingFlag;
	
	
	List<clsUpdateHouseKeepingStatus> listUpdateHouseKeepingStatus = new ArrayList<clsUpdateHouseKeepingStatus>();

	
	
	// Setter-Getter Methods
	public String getStrRoomNo() {
		return strRoomNo;
	}
	public void setStrRoomNo(String strRoomNo) {
		this.strRoomNo = strRoomNo;
	}
	public String getStrRoomFlag() {
		return strRoomFlag;
	}
	public void setStrRoomFlag(String strRoomFlag) {
		this.strRoomFlag = strRoomFlag;
	}
	public String getStrHouseKeepingName() {
		return strHouseKeepingName;
	}
	public void setStrHouseKeepingName(String strHouseKeepingName) {
		this.strHouseKeepingName = strHouseKeepingName;
	}
	public String getStrHouseKeepingFlag() {
		return strHouseKeepingFlag;
	}
	public void setStrHouseKeepingFlag(String strHouseKeepingFlag) {
		this.strHouseKeepingFlag = strHouseKeepingFlag;
	}
	

	public String getStrHouseKeepingCode() {
		return strHouseKeepingCode;
	}
	public void setStrHouseKeepingCode(String strHouseKeepingCode) {
		this.strHouseKeepingCode = strHouseKeepingCode;
	}


	public List<clsUpdateHouseKeepingStatus> getListUpdateHouseKeepingStatus() {
		return listUpdateHouseKeepingStatus;
	}
	public void setListUpdateHouseKeepingStatus(
			List<clsUpdateHouseKeepingStatus> listUpdateHouseKeepingStatus) {
		this.listUpdateHouseKeepingStatus = listUpdateHouseKeepingStatus;
	}
	
	
	


	

}
