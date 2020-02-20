package com.sanguine.webpms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webpms.dao.clsPMSStaffMasterDao;
import com.sanguine.webpms.model.clsPMSStaffMasterModel;

@Service("clsPMSStaffMasterService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,value = "WebPMSTransactionManager")
public class clsPMSStaffMasterServiceImpl implements clsPMSStaffMasterService{
	@Autowired
	private clsPMSStaffMasterDao objPMSStaffMasterDao;

	@Override
	public void funAddUpdatePMSStaffMaster(clsPMSStaffMasterModel objMaster){
		objPMSStaffMasterDao.funAddUpdatePMSStaffMaster(objMaster);
	}

	@Override
	public clsPMSStaffMasterModel funGetPMSStaffMaster(String docCode,String clientCode){
		return objPMSStaffMasterDao.funGetPMSStaffMaster(docCode,clientCode);
	}

	
	public clsPMSStaffMasterModel funGetObject(String code, String clientCode) {
		return objPMSStaffMasterDao.funGetObject(code, clientCode);
	}



}
