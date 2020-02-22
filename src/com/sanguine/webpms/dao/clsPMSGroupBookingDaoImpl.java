package com.sanguine.webpms.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webpms.model.clsPMSGroupBookingModel;
import com.sanguine.webpms.model.clsPMSGroupBookingModel_ID;

@Repository("clsPMSGroupBookingDao")
public class clsPMSGroupBookingDaoImpl implements clsPMSGroupBookingDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, value = "WebPMSTransactionManager")
	public void funAddUpdatePMSGroupBooking(clsPMSGroupBookingModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, value = "WebPMSTransactionManager")
	public clsPMSGroupBookingModel funGetPMSGroupBooking(String docCode,String clientCode){
		return (clsPMSGroupBookingModel) webPMSSessionFactory.getCurrentSession().get(clsPMSGroupBookingModel.class,new clsPMSGroupBookingModel_ID(docCode,clientCode));
	}


}
