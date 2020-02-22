package com.sanguine.webpms.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webpms.model.clsPMSGroupBookingHDModel;
import com.sanguine.webpms.model.clsPMSGroupBookingHDModel_ID;

@Repository("clsPMSGroupBookingDao")
public class clsPMSGroupBookingDaoImpl implements clsPMSGroupBookingDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, value = "WebPMSTransactionManager")
	public void funAddUpdatePMSGroupBooking(clsPMSGroupBookingHDModel objMaster){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objMaster);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, value = "WebPMSTransactionManager")
	public clsPMSGroupBookingHDModel funGetPMSGroupBooking(String docCode,String clientCode){
		return (clsPMSGroupBookingHDModel) webPMSSessionFactory.getCurrentSession().get(clsPMSGroupBookingHDModel.class,new clsPMSGroupBookingHDModel_ID(docCode,clientCode));
	}


}
