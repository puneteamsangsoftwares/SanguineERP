package com.sanguine.webpms.service;

import com.sanguine.model.clsLocationMasterModel;
import com.sanguine.webpms.model.clsPMSStaffMasterModel;

public interface clsPMSStaffMasterService{

	public void funAddUpdatePMSStaffMaster(clsPMSStaffMasterModel objMaster);

	public clsPMSStaffMasterModel funGetPMSStaffMaster(String docCode,String clientCode);
	
	public clsPMSStaffMasterModel funGetObject(String code, String clientCode);

}
