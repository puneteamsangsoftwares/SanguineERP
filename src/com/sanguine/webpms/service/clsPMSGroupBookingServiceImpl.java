package com.sanguine.webpms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webpms.dao.clsPMSGroupBookingDao;
import com.sanguine.webpms.model.clsPMSGroupBookingModel;

@Service("clsPMSGroupBookingService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, value = "WebPMSTransactionManager")
public class clsPMSGroupBookingServiceImpl implements clsPMSGroupBookingService{
	@Autowired
	private clsPMSGroupBookingDao objPMSGroupBookingDao;

	@Override
	public void funAddUpdatePMSGroupBooking(clsPMSGroupBookingModel objMaster){
		objPMSGroupBookingDao.funAddUpdatePMSGroupBooking(objMaster);
	}

	@Override
	public clsPMSGroupBookingModel funGetPMSGroupBooking(String docCode,String clientCode){
		return objPMSGroupBookingDao.funGetPMSGroupBooking(docCode,clientCode);
	}



}
